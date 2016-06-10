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
 
import com.dist.wx.api.WeixinUtil;
 
public class OAuth2Servlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	 public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  request.setCharacterEncoding("UTF-8");
		  response.setCharacterEncoding("UTF-8"); 
		  PrintWriter out = response.getWriter();
		  String code = request.getParameter("code"); 
		  System.out.println("code:" + code );
		  if (!"authdeny".equals(code)) {
			  System.out.println("code:" + code );
			  String access_token = WeixinUtil.getAccessToken( ).getToken();
			  String UserID = GOauth2Core.GetUserID(access_token, code, "agentid");
			  request.setAttribute("UserID", UserID);
		  }
		  else{
			  out.print("  ");
		  }
		  // index.jsp
		  request.getRequestDispatcher("index.jsp").forward(request, response);
		}	

}
