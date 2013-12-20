package com.loftcat.weibo.bean;

public class StatusVo implements  Vo,java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7555894758840608170L;
	/**
	 * 
	 */
	private String created_at; // 微博创建时间
	private long id; // 微博ID
	private long mid;// 微博MID
	private String idstr;// 字符串型的微博ID
	private String text;// 微博信息内容
	private String source;// 微博来源
	private boolean favorited;// 是否已收藏，true：是，false：否
	private boolean truncated;// 是否被截断，true：是，false：否
	private String in_reply_to_status_id; // （暂未支持）回复ID
	private String in_reply_to_user_id;// （暂未支持）回复人UID
	private String in_reply_to_screen_name;// （暂未支持）回复人昵称
	private String thumbnail_pic;// 缩略图片地址，没有时不返回此字段
	private String bmiddle_pic;// 中等尺寸图片地址，没有时不返回此字段
	private String original_pic;// 原始图片地址，没有时不返回此字段
	private UserVO user;// 微博作者的用户信息字段 详细

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public String getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(String in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public String getIn_reply_to_user_id() {
		return in_reply_to_user_id;
	}

	public void setIn_reply_to_user_id(String in_reply_to_user_id) {
		this.in_reply_to_user_id = in_reply_to_user_id;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}


	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public StatusVo getRetweeted_status() {
		return retweeted_status;
	}

	public void setRetweeted_status(StatusVo retweeted_status) {
		this.retweeted_status = retweeted_status;
	}

	public int getReposts_count() {
		return reposts_count;
	}

	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public int getAttitudes_count() {
		return attitudes_count;
	}

	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}

	public int getMlevel() {
		return mlevel;
	}

	public void setMlevel(int mlevel) {
		this.mlevel = mlevel;
	}


	private StatusVo retweeted_status;// 被转发的原微博信息字段，当该微博为转发微博时返回 详细
	private int reposts_count;// 转发数
	private int comments_count;// 评论数
	private int attitudes_count;// 表态数
	private int mlevel;// 暂未支持

}
