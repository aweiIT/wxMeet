package com.meeting;

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.ast.Var;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
		if (!status.equals("noRun") && !status.equals("draft")) {
			return object;
		}
		
		String startTime = object.optString("date") + " " + object.optString("startTime");
		String entTime = object.optString("date") + " " + object.optString("endTime");
		DateUtil.String2Date(startTime, "");
		long start = DateUtil.String2Date(startTime, "yyyy-MM-dd HH:mm").getTime();
		long end = DateUtil.String2Date(entTime, "yyyy-MM-dd HH:mm").getTime();
		long now = System.currentTimeMillis();
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
}
