package com.simtice.cnbeta.ui;

import java.lang.reflect.Type;
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
import com.google.gson.reflect.TypeToken;
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
import com.simtice.cnbeta.util.ExceptionUtil;
import com.simtice.cnbeta.util.HttpUtil;
import com.simtice.cnbeta.util.JsonUtil;
import com.simtice.cnbeta.util.PreferencesUtil;

/**
 * 新闻列表界面
 * 
 * @author simtice
 * 
 */
public class MainNewsListActivity extends SherlockActivity {
	private PullToRefreshListView listView;
	private List<NewsList> list;
	private BaseAdapter adapter;
	private CommonLog log;
	private boolean noPic;// 当前是否为无图模式

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.REQUEST_SUCCESS:
				log.d(msg.obj);
				Type listType = new TypeToken<ArrayList<NewsList>>() {
				}.getType();
				List<NewsList> temp = JsonUtil.parseBeanFromJson((String) msg.obj, listType);
				if (msg.arg1 == Constant.TPYE_PULL_DOWN)
					list.clear();// 如果是下拉刷新就先清除数据
				list.addAll(temp);
				adapter.notifyDataSetChanged();
				listView.setMode(Mode.BOTH);
				listView.onRefreshComplete();
				break;
			case Constant.REQUEST_FAILED:
				ExceptionUtil.handlException((Exception) msg.obj, getApplicationContext());
				listView.onRefreshComplete();
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_list);
		log = new CommonLog("MainNewsListActivity");
		initView();
		getNewsList(Constant.TPYE_PULL_DOWN, 0);
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
				intent.putExtra("title", list.get(arg2 - 1).getTitle());
				startActivity(intent);
			}
		});

		listView.setRefreshing(false);// 设置正在刷新 需要修改PullToRefreshListView
										// onRefreshing方法中把adapter.isEmpty()去掉，否则第一次进来不会有刷新的效果
										//必须要在setAdapter之后

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
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Preference.class));
			break;
		}
		return true;
	}

	long waitTime = 2000;
	long touchTime = 0;

}
