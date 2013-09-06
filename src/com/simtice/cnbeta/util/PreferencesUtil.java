package com.simtice.cnbeta.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.simtice.cnbeta.R;

public class PreferencesUtil {
	public static boolean getPicPreference(Context context){
		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(context);
		return pfes.getBoolean(context.getString(R.string.preference_nopic), false);
	}
}
