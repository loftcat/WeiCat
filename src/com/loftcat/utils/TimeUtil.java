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
package com.loftcat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * 微博发表时间处理类
 * 
 * @author Logan <a href="https://github.com/Logan676/JustSharePro"/>
 * 
 * @version 1.0
 * 
 */
public class TimeUtil {

	public static String converTime(long timestamp) {
		long currentSeconds = System.currentTimeMillis() / 1000;
		long interval = currentSeconds - timestamp;// 与现在时间相差秒数
		String timeStr = null;
		if (interval > 24 * 60 * 60) {// 1天以上
			timeStr = interval / (24 * 60 * 60) + "天前";
		} else if (interval > 60 * 60) {// 1小时-24小时
			timeStr = interval / (60 * 60) + "小时前";
		} else if (interval > 60) {// 1分钟-59分钟
			timeStr = interval / 60 + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

	public static String getStandardTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		Date date = new Date(timestamp * 1000);
		sdf.format(date);
		return sdf.format(date);
	}
}
