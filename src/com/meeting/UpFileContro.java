package com.meeting;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
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
import com.onecooo.core.netport.beehive.baseUtil.DateUtil;
import com.util.Base64Util;
import com.util.HttpUtil;

@Controller
public class UpFileContro {
	@RequestMapping("upFile.do")
	public void actionList(@RequestBody String param, HttpServletRequest request, HttpServletResponse response) {
		String realDir = request.getSession().getServletContext().getRealPath("/uploads");
		String realPath = "";
		try {
			if (realDir.contains(":"))
				realPath = realDir + "\\";
			else {
				if (realDir.endsWith("/"))
					realPath = realDir;
				else {
					realPath = realDir + "/";
				}
			}
			System.out.println("file::" + realPath);
		} catch (Exception e) {
		}

		JSONObject in = new JSONObject(param);
		String content = in.optString("content");
		writeFile(realPath, in.optString("name"), content);
		JSONObject object = new JSONObject();
		object.put("code", "1");
		HttpUtil.write(response, object.toString());
	}

	@RequestMapping("fileList.do")
	public void fileList(@RequestBody String param, HttpServletResponse response) {
		JSONObject in = new JSONObject(param);
		DbAgent dbcall = new DbAgent("onecooo", "wx_file");
		DBObject where = new BasicDBObject();
		if (!in.optString("type").equals("")) {
			where.put("type", in.optString("type"));
		}
		where.put("name", dbcall.getLikeStr(in.optString("name")));
		JSONArray list = Mongodbtools
				.DBlist2JSONArray(dbcall.getDBColl().find(where).sort(new BasicDBObject("_id", -1)).toArray());
		HttpUtil.write(response, list.toString());
	}

	void writeFile(String path, String name, String str) {

		String type = name.split("\\.")[1];
		type = getType(type);
		String fileName = DateUtil.getNow("yyyyMMddHHmmss") + new Random().nextInt(100) + "." + type;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(path + fileName));
			byte bytes[] = Base64Util.decode(str);
			out.write(bytes);
			out.close();

		} catch (Exception e) {
			System.out.println("error");
		}
		DbAgent dbcall = new DbAgent("onecooo", "wx_file");
		JSONObject object = new JSONObject();
		object.put("id", dbcall.getObjidstr("fileID"));
		object.put("type", type);
		object.put("name", name.split("\\.")[0]);
		object.put("upDate", DateUtil.getNow("yyyy-MM-dd HH:mm"));
		object.put("nowUrl", "/wxmeet/uploads/" + fileName);
		dbcall.add(object);
	}

	String getType(String type) {
		if (type.toLowerCase().contains("doc") || type.toLowerCase().contains("docx")) {
			return "docx";
		}
		if (type.toLowerCase().contains("xls") || type.toLowerCase().contains("xlsx")) {
			return "xlsx";
		}
		if (type.toLowerCase().contains("pdf")) {
			return "pdf";
		}
		if (type.toLowerCase().contains("ppt") || type.toLowerCase().contains("pptx")) {
			return "ppt";
		}
		if (type.toLowerCase().contains("txt")) {
			return "txt";
		}
		return "";
	}
}
