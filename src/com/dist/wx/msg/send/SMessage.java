package com.dist.wx.msg.send;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dist.wx.api.WeixinUtil;
import com.dist.wx.msg.resp.Article;
 
/** 
 * 发送消息类 
 * @author Engineer.Jsp
 * @date 2014.10.11 
 */
public class SMessage {
	//发送接口
	public static String POST_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
	  
	
	// toUser 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	public static boolean SendMessage(String toUser, String Title , String Content, String imgUrl, String url){
		   
		   // 调取凭证
		   String access_token = WeixinUtil.getAccessToken( ).getToken();

			Article article = new Article();
		   article.setTitle(Title);
		   article.setDescription(Content);
		   if(imgUrl!=null)
		   article.setPicUrl(imgUrl);
		   if(url!=null)
		   article.setUrl(url);
		   
		   List<Article> list = new ArrayList<Article>();
			   list.add(article);
			   JSONArray articlesList = new JSONArray(list);
			   // Post的数据
				JSONObject send = new  JSONObject();
				send.put("touser", toUser);
				send.put("toparty", "");
				send.put("totag", "");
				send.put("msgtype", "news");
				send.put("agentid", 0);
				JSONObject news = new JSONObject();
				news.put("articles", articlesList);
				send.put("news", news);
			   String PostData = send.toString()   ;//SNewsMsg("bill.cheng", "", "", "0", articlesList);
			   int result = WeixinUtil.PostMessage(access_token, "POST", POST_URL, PostData);
			   // 打印结果
				if(0==result){
					System.out.println("操作成功");
					return true ;
				}
				else {
					System.out.println("操作失败");
					return false ;
				}
		   

		
		
	}
	
	
	//示例
   public static void main(String[] args) {
	   
	   SMessage.SendMessage("xiezhiwei", "消息测试abc123",
			   "测试一下消息发送是否成功！！！",
			   "http://static.leiphone.com/uploads/2014/07/1404377084199.jpg?imageMogr2/format/jpg/quality/80",
			   "http://news.ifeng.com/");
   }
}
