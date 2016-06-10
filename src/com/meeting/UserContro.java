package com.meeting;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dist.wx.user.AllUserList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.onecooo.core.data.mongodb.DbAgent;
import com.onecooo.core.data.mongodb.listCompate.Mongodbtools;
import com.util.HttpUtil;

@Controller
public class UserContro {
	@RequestMapping("userList.do")
	public void userList(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_user");
		DBObject where = new BasicDBObject();
		String role = in.optString("role");
		if (!role.equals("")) {
			where.put("role", role);
		}
		if (!in.optString("name").equals(""))
			where.put("name", dbcall.getLikeStr(in.optString("name")));
		JSONArray list = Mongodbtools
				.DBlist2JSONArray(dbcall.getDBColl().find(where).sort(new BasicDBObject("_id", -1)).toArray());
		HttpUtil.write(response, list.toString());
	}

	@RequestMapping("saveUser.do")
	public void saveUser(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_user");
		DBObject where = new BasicDBObject();
		String id = in.optString("id");
		where.put("userid", id);
		dbcall.update(where, new BasicDBObject("role", in.optString("role")));
		JSONObject result = new JSONObject();
		result.put("code", "1");
		HttpUtil.write(response, result.toString());
	}

	@RequestMapping("synUser.do")
	public void synUser(@RequestBody String param, HttpServletResponse response) {
		DbAgent dbcall = new DbAgent("onecooo", "wx_user");
		AllUserList u = new AllUserList();
		JSONObject ret = u.getUserList();
		JSONArray department = ret.optJSONArray("department");
		for (int i = 0; i < department.length(); i++) {
			JSONObject object = department.optJSONObject(i);
			JSONObject user = new JSONObject();
			user.put("departName", object.optString("name"));
			user.put("order", object.optString("order"));
			user.put("departid", object.optString("id"));
			user.put("parentid", object.optString("parentid"));
			JSONArray userList = object.optJSONArray("Users");
			for (int n = 0; n < userList.length(); n++) {
				user.put("name", userList.optJSONObject(n).optString("name"));
				String userid = userList.optJSONObject(n).optString("userid");
				user.put("wx_userid", userid);
				DBObject where = new BasicDBObject();
				where.put("wx_userid", userid);
				if (dbcall.findByCount(where) == 0) {
					user.put("userid", dbcall.getObjidstr("userid"));
					user.put("role", "user");
					dbcall.add(user);
				} else {
					dbcall.update(where, Mongodbtools.JSONObject2DBObject(user));
				}
			}
		}
		JSONObject result = new JSONObject();
		result.put("code", "1");
		HttpUtil.write(response, result.toString());
	}

}
