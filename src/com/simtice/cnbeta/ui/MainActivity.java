package com.simtice.cnbeta.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.adapter.MainDrawerAdapter;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.PreferencesUtil;
import com.simtice.cnbeta.util.imagecache.FileHelper;
import com.simtice.cnbeta.util.imagecache.FileManager;
import com.umeng.fb.FeedbackAgent;

/**
 * 主界面
 * 
 * @author simtice
 * 
 */
public class MainActivity extends SherlockFragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private List<String> list;
	private List<String> listTag;
	private MainDrawerAdapter adapter;

	private int currentPosition= 1;
	private boolean isClickList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Sherlock___Theme_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
		new FeedbackAgent(this).sync();// 同步用户反馈
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.settings).setVisible(drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerList = (ListView) findViewById(R.id.lv_main_drawer);
		list = new ArrayList<String>();
		listTag = new ArrayList<String>();
		list.add(getString(R.string.TAG_NEWS));
		listTag.add(getString(R.string.TAG_NEWS));
		list.add(getString(R.string.TYPE_ALL));
		list.add(getString(R.string.TYPE_HOTCM));
		list.add(getString(R.string.TYPE_TOP10));

		adapter = new MainDrawerAdapter(this, list, listTag);
		mDrawerList.setAdapter(adapter);
		
//		if(PreferencesUtil.isFirstart(getApplicationContext())){
//			mDrawerLayout.openDrawer(mDrawerList);
//		}
		
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selectItem(position);
			}
		});

		mDrawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				supportInvalidateOptionsMenu();
				if(isClickList){//只有点击了list才更新fragment
					Fragment fragment = null;
					switch (currentPosition) {
					case 1:
						fragment = new NewsListFragment();
						break;
					case 2:
						fragment = new HMCommentFragment();
						break;
					case 3:
						fragment = new Top10Fragment();
						break;
					}
					updateFragment(fragment);
					isClickList = false;
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		});
		
		//程序启动默认显示的Fragment
		selectItem(1);
		updateFragment(new NewsListFragment());
		
	}
	
	private void updateFragment(Fragment fragment){
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
		getSupportActionBar().setTitle(list.get(currentPosition));
		
		adapter.setSelectedPosition(currentPosition);
		adapter.notifyDataSetChanged();
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		mDrawerLayout.closeDrawer(mDrawerList);
		if(position!=currentPosition){
			currentPosition = position;
			isClickList = true;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawers();
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			break;
		case R.id.settings:
			startActivity(new Intent(this, PreferenceActivity.class));
			break;
		}
		return true;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mDrawerLayout.closeDrawer(mDrawerList);// 当跳转到设置界面，回调onStop之后再关闭抽屉，防止在startActivity的同时关闭抽屉造成界面卡顿
	}

	long waitTime = 2000;
	long touchTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			} else {
				if (PreferencesUtil.isAutouClean(getApplicationContext())) {
					FileHelper.deleteDirectory(FileManager.getCacheRootDir(Constant.dirName));
				}
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
