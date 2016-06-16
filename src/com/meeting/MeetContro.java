package com.meeting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.ast.Var;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dist.wx.msg.send.SMessage;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.onecooo.core.data.mongodb.DbAgent;
import com.onecooo.core.data.mongodb.listCompate.Mongodbtools;
import com.onecooo.core.netport.beehive.baseUtil.DateUtil;
import com.util.HttpUtil;

@Controller
public class MeetContro {
	@RequestMapping("meetInfo.do")
	public void meetInfo(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_meet");
		DBObject where = new BasicDBObject();
		String id = in.optString("id");
		String status = in.optString("status");
		if (!id.equals("")) {
			where.put("id", id);
		}
		if (!status.equals("")) {
			where.put("status", status);
		}
		JSONArray list = Mongodbtools
				.DBlist2JSONArray(dbcall.getDBColl().find(where).sort(new BasicDBObject("_id", -1)).toArray());
		HttpUtil.write(response, list.toString());
	}

	@RequestMapping("getMeet.do")
	public void getMeet(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_meet");
		DBObject where = new BasicDBObject();
		String id = in.optString("id");
		JSONObject object = Mongodbtools.DBObject2JSON(dbcall.getDBColl().findOne(new BasicDBObject("id", id)));
		DbAgent roomDB = new DbAgent("onecooo", "wx_room");
		DBObject roomObj = roomDB.getDBColl()
				.findOne(new BasicDBObject("id", object.optJSONObject("s_room").optString("id")));
		object.put("address", roomObj.get("address"));
		HttpUtil.write(response, object.toString());
	}

	@RequestMapping("sendMeet.do")
	public void sendMeet(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		JSONObject user = in.optJSONObject("user");
		Iterator users = user.keys();
		String id = in.optString("id");
		String type = in.optString("type");
		while (users.hasNext()) {
			String userID = (String) users.next();
			SMessage.SendMessage(user.optJSONObject(userID).optString("wx_userid"), in.optString("title"),
					in.optString("subtitle"),
					"http://static.leiphone.com/uploads/2014/07/1404377084199.jpg?imageMogr2/format/jpg/quality/80",
					"http://office.hxedu.tj.cn:8080/wxmeet/msg_addMeet.html?id=" + id + "&type=" + type);
		}
		DbAgent dbcall = new DbAgent("onecooo", "wx_meet");
		String status = "";
		if (type.equals("add")) {
			status = "noRun";
		}
		if (type.equals("can")) {
			status = "can";
		}
		dbcall.update(new BasicDBObject("id", id), new BasicDBObject("status", status));
		JSONObject object = new JSONObject();
		object.put("code", "1");
		HttpUtil.write(response, object.toString());
	}

	@RequestMapping("saveMeet.do")
	public void saveMeet(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_meet");
		String id = in.optString("id");
		if (id.equals("")) {
			id = dbcall.getObjidstr("meetID");
			in.put("id", id);
			dbcall.add(in);
		} else {
			dbcall.update(new BasicDBObject("id", id), Mongodbtools.JSONObject2DBObject(in));
		}
		JSONObject msg = new JSONObject();
		msg.put("code", "1");
		msg.put("obj", in);
		HttpUtil.write(response, msg.toString());
	}

	@RequestMapping("meetList.do") // 会议列表
	public void meetList(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_meet");
		DBObject where = new BasicDBObject();
		String id = in.optString("id");
		String methem = in.optString("methem");
		String status = in.optString("status");
		if (!id.equals("")) {
			where.put("id", id);
		}
		if (!status.equals("")) {
			where.put("status", status);
		} else {
			if (in.optString("type").equals("meetList")) {
				List<Object> condlist = new ArrayList<Object>();
				condlist.add("noRun");
				condlist.add("run");
				condlist.add("over");
				where.put("status", dbcall.getin(condlist));
			}
		}
		if (!methem.equals("")) {
			where.put("methem", dbcall.getLikeStr(methem));
		}
		JSONArray list = Mongodbtools
				.DBlist2JSONArray(dbcall.group(new BasicDBObject("date", 1), where, null, null, null));
		List<Object> dateList = new ArrayList<Object>();
		JSONObject dateObj = new JSONObject();
		for (int i = 0; i < list.length(); i++) {
			String date = list.optJSONObject(i).optString("date");
			dateObj.put(date, new JSONArray());
			dateList.add(date);
		}
		where.put("date", dbcall.getin(dateList));
		JSONArray list2 = Mongodbtools.DBlist2JSONArray(dbcall.getDBColl().find(where).toArray());
		/**
		 * DbAgent roomDB = new DbAgent("onecooo", "wx_room"); JSONArray
		 * roomList =
		 * Mongodbtools.DBlist2JSONArray(roomDB.getDBColl().find().toArray());
		 * JSONObject roomObj = new JSONObject(); for (int i = 0; i <
		 * roomList.length(); i++) {
		 * roomObj.put(roomList.optJSONObject(i).optString("id"),
		 * roomList.optJSONObject(i).optString("name")); }
		 */
		for (int i = 0; i < list2.length(); i++) {
			JSONObject object = list2.optJSONObject(i);
			JSONArray array = dateObj.optJSONArray(object.optString("date"));
			object = checkStatus(object);
			array.put(object);
			dateObj.put(object.optString("date"), array);
		}
		HttpUtil.write(response, dateObj.toString());
	}

	JSONObject checkStatus(JSONObject object) {
		String status = object.optString("status");
		if (!status.equals("noRun") && !status.equals("draft")&& !status.equals("run")) {
			return object;
		}

		String startTime = object.optString("date") + " " + object.optString("startTime");
		String entTime = object.optString("date") + " " + object.optString("endTime");
		DateUtil.String2Date(startTime, "");
		long start = DateUtil.String2Date(startTime, "yyyy-MM-dd HH:mm").getTime();
		long end = DateUtil.String2Date(entTime, "yyyy-MM-dd HH:mm").getTime();
		long now = System.currentTimeMillis();
		System.out.println(start + "--" + end + "---" + status);
		if (start < now && status.equals("draft")) {
			object.put("status", "can");
			task(object.optString("id"), "can");
			return object;
		}
		if (start < now && status.equals("noRun")) {
			object.put("status", "run");
			task(object.optString("id"), "run");
			return object;
		}
		if (end < now && status.equals("run")) {
			object.put("status", "over");
			task(object.optString("id"), "over");
			return object;
		}
		if (end < now) {
			object.put("status", "can");
			task(object.optString("id"), "can");
			return object;
		}

		return object;
	}

	void task(String id, String status) {
		final String idString = id;
		final String statusString = status;
		new Thread(new Runnable() {
			@Override
			public void run() {
				DbAgent dbcall = new DbAgent("onecooo", "wx_meet");
				dbcall.update(new BasicDBObject("id", idString), new BasicDBObject("status", statusString));
			}
		}).start();
	}

	@RequestMapping("checkMeetTime.do")
	public void checkMeetTime(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_meet");
		DBObject where = new BasicDBObject();
		where.put("s_room.id", in.optString("roomID"));
		where.put("date", in.optString("date"));
		String start = in.optString("startTime");
		String end = in.optString("endTime");
		JSONArray list = Mongodbtools.DBlist2JSONArray(dbcall.getDBColl().find(where).toArray());
		JSONObject msg = new JSONObject();
		msg.put("code", "1");
		for (int i = 0; i < list.length(); i++) {
			JSONObject object = list.optJSONObject(i);
			if (object.optString("status").equals("can") || object.optString("status").equals("over"))
				continue;// 结束或者取消的状态
			String startTime = object.optString("startTime");
			String endTime = object.optString("endTime");
			if (start.compareTo(startTime) > 0 && start.compareTo(endTime) < 0) {
				// 在使用状态
				msg.put("code", "-1");
			}
			if (end.compareTo(startTime) > 0 && end.compareTo(endTime) < 0) {
				// 在使用状态
				msg.put("code", "-1");
			}
		}
		HttpUtil.write(response, msg.toString());
	}
}
