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
