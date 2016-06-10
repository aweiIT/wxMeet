package com.dist.wx.api;
/**
 * @author Engineer-Jsp
 * @date 2014.10.09
 * 请求数据通用类*/
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

 
public class WeixinUtil {
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
	public static  AccessToken accessToken ;
 	
	public static  AccessToken getAccessToken(){
		if(accessToken==null){
			String url = AccessToken.TOKEN_URL;
			url=url.replaceAll("\\{corpid\\}", ParamesAPI.corpId);
			url=url.replaceAll("\\{corpsecret\\}", ParamesAPI.corpsecret);
			String output = new String() ;
			JSONObject ret=  HttpRequest(url,"GET",output);
			if(ret.optString("access_token","" ).length()> 0){
				accessToken = new AccessToken();
				accessToken.setToken(ret.optString("access_token","" ));
				accessToken.setExpiresIn(ret.optInt("expires_in", 7200) );
			}
		}
		return  accessToken ;
	}
	
	
	public static JSONObject HttpRequest(String request , String RequestMethod , String output ){
		@SuppressWarnings("unused")
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		System.out.println(request);
		try {
			//建立连接
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod(RequestMethod);
			if(output!=null){
				OutputStream out = connection.getOutputStream();
				out.write(output.getBytes("UTF-8"));
				out.close();
			}
			//流处理
			InputStream input = connection.getInputStream();
			InputStreamReader inputReader = new InputStreamReader(input,"UTF-8");
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
			while((line=reader.readLine())!=null){
				buffer.append(line);
			}
			//关闭连接、释放资源
			reader.close();
			inputReader.close();
			input.close();
			input = null;
			connection.disconnect();
			jsonObject = new org.json.JSONObject(buffer.toString()) ;//  JSONObject.fromObject(buffer.toString());
		} catch (Exception e) {
		}
		return jsonObject;
	}

	public static String URLEncoder(String str){
		String result = str ;
		try {
		result = java.net.URLEncoder.encode(result,"UTF-8");	
		} catch (Exception e) {
        e.printStackTrace();
		}
		return result;
	}
	
 	  
 
  	  
 
	 
}  
