package com.simtice.cnbeta.util;

import android.util.Log;

public class LogUtil {

	public static void e(String tag, String msg) {
		if (Constant.DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (Constant.DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (Constant.DEBUG) {
			Log.w(tag, msg);
		}
	}
}
