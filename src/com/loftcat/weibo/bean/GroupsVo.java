package com.loftcat.weibo.bean;

import java.io.Serializable;

public class GroupsVo implements Vo, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5530290032705591592L;

	private long id;// 221012250012006340,
	private String idstr;// : "221012250012006330",
	private String name;// "同学",
	private String mode;// : "private",
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getMember_count() {
		return member_count;
	}

	public void setMember_count(int member_count) {
		this.member_count = member_count;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private int visible;// : 0,
	private int like_count;// : 0,

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	private int member_count;// ": 41,
	private String description;// : "",
	private String profile_image_url;// :
	private UserVO user;//
	private String created_at;// : "Sat Dec 25 14:12:35 +0800 2010"
}
