package com.simtice.cnbeta.ui;

import java.io.File;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
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
import com.umeng.fb.FeedbackAgent;

/**
 * 系统设置
 * 
 * @author simtice
 * 
 */
public class PreferenceActivity extends  SherlockPreferenceActivity implements OnSharedPreferenceChangeListener, OnPreferenceClickListener {
	private PreferenceScreen psClean;
	private ListPreference lp;
	private CheckBoxPreference cp;

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
		getSupportActionBar().setIcon(R.drawable.icon);
		SharedPreferences pfes = PreferenceManager.getDefaultSharedPreferences(this);
		pfes.registerOnSharedPreferenceChangeListener(this);
		psClean = (PreferenceScreen) findPreference(getString(R.string.preference_clearcache));
		lp = (ListPreference) findPreference(getString(R.string.preference_font));
		cp = (CheckBoxPreference) findPreference(getString(R.string.preference_wifi));

		if (cp.isChecked()) {
			cp.setSummary("在非Wifi下节省流量");
		} else {
			cp.setSummary("显示新闻图片，体验更佳");
		}

		setSummary(PreferencesUtil.getFontPreference(getApplicationContext()));

		psClean.setOnPreferenceClickListener(this);
		findPreference(getString(R.string.preference_feedback)).setOnPreferenceClickListener(this);
		findPreference(getString(R.string.preference_about)).setOnPreferenceClickListener(this);

		File file = new File(FileManager.getCacheRootDir(Constant.dirName));
		try {
			long size = FileHelper.getFolderSize(file);
			psClean.setSummary(FileHelper.setFileSize(size));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			psClean.setSummary("0KB");

		}

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(getString(R.string.preference_font))) {
			setSummary(Integer.parseInt(lp.getValue()));
		} else if (key.equals(getString(R.string.preference_wifi))) {
			if (cp.isChecked()) {
				cp.setSummary("在非Wifi下节省流量");
			} else {
				cp.setSummary("显示新闻图片，体验更佳");
			}
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void setSummary(int size) {
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

	@Override
	public boolean onPreferenceClick(android.preference.Preference preference) {
		if (preference.getKey().equals(getString(R.string.preference_clearcache))) {
			CommonUtil.showAlertDialog(PreferenceActivity.this, "是否要清空缓存?", "", "确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					FileHelper.deleteDirectory(FileManager.getCacheRootDir(Constant.dirName));
					psClean.setSummary("0KB");
					CommonUtil.showToast(getApplicationContext(), "缓存已清理");
				}
			}, "取消", null);
		} else if (preference.getKey().equals(getString(R.string.preference_feedback))) {
			FeedbackAgent agent = new FeedbackAgent(this);
		    agent.startFeedbackActivity();
		}else if (preference.getKey().equals(getString(R.string.preference_about))) {
			startActivity(new Intent(PreferenceActivity.this,AboutActivity.class));
		}
		return false;
	}
}
