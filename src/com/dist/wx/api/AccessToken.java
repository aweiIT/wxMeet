package com.dist.wx.api;

/** 
 * 微信通用接口凭证 
 *  
 * @author Engineer-Jsp 
 * @date 2014.06.23 
 */  
public class AccessToken {  
    
	//创建成员地址
	public static String TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpid}&corpsecret={corpsecret}";
 
	// 获取到的凭证  
	
	
    private String token;  
    // 凭证有效时间，单位：秒  
    private int expiresIn;  

    
    
    public String getToken() {  
        return token;  
    }  
  
    public void setToken(String token) {  
        this.token = token;  
    }  
  
    public int getExpiresIn() {  
        return expiresIn;  
    }  
  
    public void setExpiresIn(int expiresIn) {  
        this.expiresIn = expiresIn;  
    }  
}  