package com.simtice.cnbeta.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.simtice.cnbeta.R;

public class PreferencesUtil {
	public static boolean isNoPicmode(Context context) {
		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(context);
		return pfes.getBoolean(context.getString(R.string.preference_nopic), false);

	}

	public static int getFontPreference(Context context) {
		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(context);
		String text = pfes.getString(context.getString(R.string.preference_font), "1");
		return Integer.parseInt(text);
	}
	
	public static void setTextSize(Context context, int size) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(context.getString(R.string.preference_font), String.valueOf(size));
		editor.commit();
	}
	
	public static boolean isAutouClean(Context context){
		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(context);
		return pfes.getBoolean(context.getString(R.string.preference_auto_clearcache), false);
	}
	
	//2G/3G模式下禁止加载图片
	public static boolean isNoShowPicIn2G(Context context){
		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(context);
		return pfes.getBoolean(context.getString(R.string.preference_wifi), false);
	}
	
//	public static boolean isFirstart(Context context){
//		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(context);
//		return pfes.getBoolean(context.getString(R.string.preference_first), true);
//	}
//	
//	public static void setIsFirstart(Context context){
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//		Editor editor = prefs.edit();
//		editor.putBoolean(context.getString(R.string.preference_first), false);
//		editor.commit();
//	}
}
