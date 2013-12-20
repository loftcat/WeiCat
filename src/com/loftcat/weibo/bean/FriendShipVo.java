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
