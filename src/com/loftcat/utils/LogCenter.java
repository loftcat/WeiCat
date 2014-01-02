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

import org.apache.http.protocol.HTTP;

import android.net.wifi.WifiConfiguration.Protocol;
import android.util.Log;

/**
 * @Author HeBin
 * @Class_name LogCenter.java
 * @Description Log信息中心(待完善)未来可在此基础上收集LOG信息，或者删除所有log
 * @Create_date 2012-8-13
 * @version 1.3
 */
public class LogCenter {
	private static LogCenter logCenter = new LogCenter();

	private LogCenter() {
	}

	public static LogCenter getInstance() {
		return logCenter;
	}

	public void error(String tag, String msg) {
		if (logCenter != null)
			Log.e(tag, msg);
	}

	public void info(String tag, String msg) {
		if (logCenter != null)
			Log.i(tag, msg);
	}

	public void debug(String tag, String msg) {
		if (logCenter != null)
			Log.d(tag, msg);
	}

	public void verbose(String tag, String msg) {
		if (logCenter != null)
			Log.v(tag, msg);
	}

	public void warn(String tag, String msg) {
		if (logCenter != null)
			Log.w(tag, msg);
	}

	public void debugID(String msg) {
		if (logCenter != null)
			Log.d("ID", msg);
	}

	public void debugRESULT(String msg) {
		if (logCenter != null)
			Log.d("RESULT", msg);
	}

	public void debugJSON(String msg) {
		if (logCenter != null)
			Log.d("JSON", msg);
	}

}
