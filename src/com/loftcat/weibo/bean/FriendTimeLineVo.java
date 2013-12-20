package com.loftcat.weibo.bean;

import java.util.ArrayList;

public class FriendTimeLineVo implements  Vo,java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2625508893185091195L;
	private ArrayList<StatusVo> statuses;
	private boolean hasvisible;
	private int previous_cursor;

	public ArrayList<StatusVo> getStatuses() {
		return statuses;
	}

	public void setStatuses(ArrayList<StatusVo> statuses) {
		this.statuses = statuses;
	}

	public boolean isHasvisible() {
		return hasvisible;
	}

	public void setHasvisible(boolean hasvisible) {
		this.hasvisible = hasvisible;
	}

	public int getPrevious_cursor() {
		return previous_cursor;
	}

	public void setPrevious_cursor(int previous_cursor) {
		this.previous_cursor = previous_cursor;
	}

	public long getNext_cursor() {
		return next_cursor;
	}

	public void setNext_cursor(long next_cursor) {
		this.next_cursor = next_cursor;
	}

	public int getTotal_number() {
		return total_number;
	}

	public void setTotal_number(int total_number) {
		this.total_number = total_number;
	}

	private long next_cursor;
	private int total_number;
}
