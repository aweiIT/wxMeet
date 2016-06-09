package com.meeting;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.onecooo.core.data.mongodb.DbAgent;
import com.onecooo.core.data.mongodb.listCompate.Mongodbtools;
import com.util.HttpUtil;

@Controller
public class RoomContro {
	@RequestMapping("roomList.do")
	public void roomList(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_room");
		DBObject where = new BasicDBObject();
		String id = in.optString("id");
		if (!id.equals("")) {
			where.put("id", id);
		}
		if (in.optString("id").equals(""))
			where.put("name", dbcall.getLikeStr(in.optString("name")));
		JSONArray list = Mongodbtools
				.DBlist2JSONArray(dbcall.getDBColl().find(where).sort(new BasicDBObject("_id", -1)).toArray());
		HttpUtil.write(response, list.toString());
	}

	@RequestMapping("saveRoom.do")
	public void saveRoom(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_room");
		String id = in.optString("id");
		if (id.equals("")) {
			id = dbcall.getObjidstr("roomID");
			in.put("id", id);
			dbcall.add(in);
		} else {
			dbcall.update(new BasicDBObject("id", id), Mongodbtools.JSONObject2DBObject(in));
		}
		JSONObject msg = new JSONObject();
		msg.put("code", "1");
		HttpUtil.write(response, msg.toString());
	}
}
