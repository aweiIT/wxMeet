package com.dist.wx.servlet;

/** 
 * Oauth2 Servlet 
 * @author Engineer.Jsp
 * @date 2014.10.13 
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.ast.Var;
import org.json.JSONObject;

import com.dist.wx.api.WeixinUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.onecooo.core.data.mongodb.DbAgent;

public class OAuth2Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String code = request.getParameter("code");
		String userID = "";
		if (!"authdeny".equals(code)) {
			System.out.println("code:" + code);
			String access_token = WeixinUtil.getAccessToken().getToken();
			userID = GOauth2Core.GetUserID(access_token, code, "6");
			System.out.println(userID);
		} else {
			out.print("get userID error");
		}
		// index.jsp
		String pageStr = "";
		DbAgent dbcall = new DbAgent("onecooo", "wx_user");
		DBObject where = new BasicDBObject();
		where.put("wx_userid", userID);
		DBObject object = dbcall.getDBColl().findOne(where);
		System.out.println(where);
		System.out.println(object.toString());
		String role = object.get("role").toString();
		if (role.equals("user")) {
			pageStr = "personal.html";
		}
		if (role.equals("manager")) {
			pageStr = "index.html";
		}
		request.getRequestDispatcher(pageStr + "?userid=" + userID).forward(request, response);
	}

}
