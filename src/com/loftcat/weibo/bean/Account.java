/**   
 * Copyright (c) 2013 by Logan.	
 *   
 * 爱分享-微博客户端，是一款运行在android手机上的开源应用，代码和文档已托管在GitHub上，欢迎爱好者加入
 * 1.授权认证：Oauth2.0认证流程
 * 2.服务器访问操作流程
 * 3.新浪微博SDK和腾讯微博SDK
 * 4.HMAC加密算法
 * 5.SQLite数据库相关操作
 * 6.字符串处理，表情识别
 * 7.JSON解析，XML解析：超链接解析，时间解析等
 * 8.Android UI：样式文件，布局
 * 9.异步加载图片，异步处理数据，多线程  
 * 10.第三方开源框架和插件
 *    
 */
package com.loftcat.weibo.bean;

/**
 * 账号实体类
 * 
 * @author Logan <a href="https://github.com/Logan676/JustSharePro"/>
 * 
 * @version 1.0
 */
public class Account implements Vo{

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	// sina
	public Account(String id, String name, String userInfo, String token,
			String expires_in, String timeLine) {
		this.id = id;
		this.userInfo = userInfo;
		this.token = token;
		this.expires_in = expires_in;
		this.name = name;
	}

	public Account() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	private int _id;// SQLite自动生成、维护的列名，自增长
	private String userInfo = "";// 名字-----sina

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}


	private String id = "";// ID号-------sina
	private String token = "";// -------sina:token;tencent:accessToken
	private String expires_in = "";
	private String name = "";// -------sina:token;tencent:accessToken

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
