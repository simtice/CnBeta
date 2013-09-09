package com.simtice.cnbeta.ui;

import java.io.File;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.PreferencesUtil;
import com.simtice.cnbeta.util.imagecache.FileHelper;
import com.simtice.cnbeta.util.imagecache.FileManager;

/**
 * 系统设置
 * @author simtice
 *
 */
public class Preference extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
	private PreferenceScreen ps;
	private ListPreference lp;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle arg0) {
		setTheme(R.style.Sherlock___Theme_Light);
		super.onCreate(arg0);
		addPreferencesFromResource(R.xml.preferences);
		initView();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("系统设置");
		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(this);
		pfes.registerOnSharedPreferenceChangeListener(this);
		ps = (PreferenceScreen) findPreference(getString(R.string.preference_clearcache));
		lp = (ListPreference) findPreference(getString(R.string.preference_font));
		setSummary(PreferencesUtil.getFontPreference(getApplicationContext()));
		
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
		if (key == getString(R.string.preference_font)) {
			setSummary(Integer.parseInt(lp.getValue()));
		}
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == android.R.id.home){
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void setSummary(int size){
		String text = "";
		switch (size) {
		case 0:
			text = "大";
			break;
			
		case 1:
			text = "中";
			break;
		case 2:
			text = "小";
			break;
		}
		
		lp.setSummary(text);
	}
}
