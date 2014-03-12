package com.simtice.cnbeta.util;

import java.net.SocketTimeoutException;

import android.content.Context;

public class ExceptionUtil {
	/**
	 * 处理异常
	 * 
	 * @param e
	 * @param context
	 */
	public static void handlException(Exception e, Context context) {
		String prompt = "";
		if (e instanceof SocketTimeoutException) {
			prompt = "网络不给力";
		} else {
			prompt = "数据获取失败";
		}
		CommonUtil.showToast(context, prompt);
	}

}
