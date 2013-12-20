package com.loftcat.utils;

import org.apache.http.protocol.HTTP;

import android.net.wifi.WifiConfiguration.Protocol;
import android.util.Log;

/**
 * @Author heb
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
