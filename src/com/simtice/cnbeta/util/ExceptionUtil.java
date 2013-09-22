package com.simtice.cnbeta.util;

import java.net.SocketTimeoutException;

import android.content.Context;

public class ExceptionUtil {
	/**
	 * 处理异常
	 * @param e
	 * @param context
	 */
	public static void handlException(Exception e, Context context) {
		String tip = "";
		if (e instanceof SocketTimeoutException) {
			tip = "网络不稳定，请稍候再试";
		} else {
			tip = "数据获取失败";
		}
		CommonUtil.showToast(context, tip);
	}

}
