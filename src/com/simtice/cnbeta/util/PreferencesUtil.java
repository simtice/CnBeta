package com.simtice.cnbeta.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.simtice.cnbeta.R;

public class PreferencesUtil {
	public static boolean getPicPreference(Context context) {
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
}
