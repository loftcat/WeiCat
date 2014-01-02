/*
 * Copyright (c) 2013 HeBin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
