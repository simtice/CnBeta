package com.simtice.cnbeta.ui;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.adapter.NewsListAdapter;
import com.simtice.cnbeta.adapter.NewsListNoPicAdapter;
import com.simtice.cnbeta.bean.NewsList;
import com.simtice.cnbeta.util.CommonLog;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.HttpUtil;
import com.simtice.cnbeta.util.JsonUtil;
import com.simtice.cnbeta.util.PreferencesUtil;

public class MainNewsListActivity extends SherlockActivity {
	private PullToRefreshListView listView;
	private List<NewsList> list;
	private BaseAdapter adapter;
	private CommonLog log;
	private boolean noPic;// 是否为无图模式

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.REQUEST_SUCCESS:
				log.d(msg.obj);
				List<NewsList> temp = JsonUtil.parseBeanFromJson((String) msg.obj, NewsList.class);
				if (msg.arg1 == Constant.TPYE_PULL_DOWN)
					list.clear();// 如果是下拉刷新就先清除数据
				list.addAll(temp);
				adapter.notifyDataSetChanged();
				listView.setMode(Mode.BOTH);
				listView.onRefreshComplete();
				break;
			case Constant.REQUEST_FAILED:
				Exception e = (Exception) msg.obj;
				String tip = "";
				if (e instanceof SocketTimeoutException) {
					tip = "网络不稳定，请稍候再试";
				} else {
					tip = "数据获取失败";
				}
				listView.onRefreshComplete();
				CommonUtil.showToask(getApplicationContext(), tip);
				break;
			case Constant.NO_NETWORK:
				CommonUtil.showToask(getApplicationContext(), "网络不可用，请检查网络连接设置");
				listView.onRefreshComplete();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTheme(R.style.Sherlock___Theme_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_list);
		log = new CommonLog("MainNewsListActivity");
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean temp = PreferencesUtil.getPicPreference(getApplicationContext());
		
		if (noPic != temp) {
			if (temp) {
				adapter = new NewsListNoPicAdapter(list, this);
			} else {
				adapter = new NewsListAdapter(list, this, listView);
			}
			this.listView.setAdapter(adapter);
			noPic = temp;
		}

	}

	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		listView = (PullToRefreshListView) this.findViewById(R.id.pl_main_list);
		list = new ArrayList<NewsList>();
		noPic = PreferencesUtil.getPicPreference(getApplicationContext());
		if (noPic) {
			adapter = new NewsListNoPicAdapter(list, this);
		} else {
			adapter = new NewsListAdapter(list, this, listView);
		}
		listView.setAdapter(adapter);

		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getNewsList(Constant.TPYE_PULL_DOWN, 0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getNewsList(Constant.TPYE_PULL_UP, list.get(list.size() - 1).getArticleID());
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(MainNewsListActivity.this, NewsDetail.class);
				intent.putExtra("ArticleID", list.get(arg2 - 1).getArticleID());// 因为listview下拉刷新的机制，要-1
				startActivity(intent);
			}
		});

	}

	private void getNewsList(final int type, final long articleID) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil(getApplicationContext(), handler);
				util.requestNewsList(Constant.URL_GETNEWSLIST, type, articleID);
			}

		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Preference.class));
			break;
		case android.R.id.home:

			break;
		}
		return true;
	}

}
