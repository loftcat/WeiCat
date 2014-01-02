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

public class FriendShipVo implements Vo {

	private long id;// 1418348195,
	private String screen_name; // ": "zaku",
	private boolean followed_by;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public boolean isFollowed_by() {
		return followed_by;
	}

	public void setFollowed_by(boolean followed_by) {
		this.followed_by = followed_by;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isNotifications_enabled() {
		return notifications_enabled;
	}

	public void setNotifications_enabled(boolean notifications_enabled) {
		this.notifications_enabled = notifications_enabled;
	}

	private boolean following;//
	private boolean notifications_enabled;// ": false

}
