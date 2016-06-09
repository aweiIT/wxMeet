package com.dist.wx.user;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dist.wx.api.AccessToken;
import com.dist.wx.api.WeixinUtil;

public class AllUserList {
	
	// 获取部门列表地址
	public static String GROUP_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token={access_token}";
 	//获取部门成员地址
	public static String USER_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token={access_token}&department_id={department_id}&fetch_child=0&status=1";

 	
	
	public JSONObject getGroupList(){
		String access_token = WeixinUtil.getAccessToken().getToken();
		String url = AllUserList.GROUP_LIST_URL;
		url=url.replaceAll("\\{access_token\\}", access_token);
		String output = new String() ;
		JSONObject ret=  WeixinUtil.HttpRequest(url,"GET",output);
 		return  ret  ;
	}
	
	public JSONObject getUserList(){
		
		JSONObject group = getGroupList() ;
		
		String access_token = WeixinUtil.getAccessToken().getToken();
		
		JSONArray groupList = group.optJSONArray("department");
		for(int i=0 ;i < groupList.length() ; i++){
			JSONObject groupItem= groupList.getJSONObject(i);
			String url = AllUserList.USER_LIST_URL;
			url=url.replaceAll("\\{access_token\\}", access_token);
			url=url.replaceAll("\\{department_id\\}", groupItem.optString("id","0"));
			String output = new String() ;
			JSONObject ret=  WeixinUtil.HttpRequest(url,"GET",output);
			groupItem.put("Users", ret.optJSONArray("userlist"));
		}
 		return  group  ;
	}
	
	
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AllUserList u=new AllUserList();
		JSONObject ret = u.getUserList();
		
		System.out.print(ret.toString() );
		
		
	}








}
