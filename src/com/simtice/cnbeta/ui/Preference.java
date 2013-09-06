package com.simtice.cnbeta.ui;

import java.io.File;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.imagecache.FileHelper;
import com.simtice.cnbeta.util.imagecache.FileManager;

public class Preference extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
	private PreferenceScreen ps;

	@Override
	protected void onCreate(Bundle arg0) {
		setTheme(R.style.Sherlock___Theme_Light);
		super.onCreate(arg0);
		addPreferencesFromResource(R.xml.preferences);
		initView();
	}

	private void initView() {
		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(this);
		pfes.registerOnSharedPreferenceChangeListener(this);
		ps = (PreferenceScreen) findPreference(getString(R.string.preference_clearcache));
		ps.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(android.preference.Preference preference) {
				// TODO Auto-generated method stub

				CommonUtil.showAlertDialog(Preference.this, "是否要清空缓存?", "", "确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						FileHelper.deleteDirectory(FileManager.getCacheRootDir(Constant.dirName));
						ps.setSummary("0M");
						CommonUtil.showToask(getApplicationContext(), "缓存已清理");
					}
				}, "取消", null);

				return false;
			}
		});

		File file = new File(FileManager.getCacheRootDir(Constant.dirName));
		try {
			long size = FileHelper.getFolderSize(file);
			ps.setSummary(FileHelper.setFileSize(size));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ps.setSummary("0M");

		}

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key == getString(R.string.preference_clearcache)) {
		}
	}
}
