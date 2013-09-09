package com.simtice.cnbeta.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.adapter.CommentAdapter;
import com.simtice.cnbeta.bean.CommentLists;
import com.simtice.cnbeta.util.CommonLog;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.ExceptionUtil;
import com.simtice.cnbeta.util.HttpUtil;
import com.simtice.cnbeta.util.JsonUtil;

public class CommentList extends SherlockActivity {
	private CommonLog log;
	private PullToRefreshListView listView;
	private long articleID;
	private CommentAdapter adapter;
	private List<CommentLists> lists;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.REQUEST_SUCCESS:
				Type listType = new TypeToken<ArrayList<CommentLists>>() {
				}.getType();
				List<CommentLists> list = JsonUtil.parseBeanFromJson((String) msg.obj, listType);
				lists.addAll(list);
				if (lists.size() == 0) {
					CommonUtil.showToask(getApplicationContext(), "暂无评论");
				}
				adapter.notifyDataSetChanged();
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
		setTheme(R.style.Sherlock___Theme_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);
		log = new CommonLog("CommentList");
		init();
		initView();
		getCommentList(articleID);
	}

	private void init() {
		articleID = getIntent().getLongExtra("ArticleID", 0);
		lists = new ArrayList<CommentLists>();
		adapter = new CommentAdapter(this, lists);
	}

	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.getSupportActionBar().setTitle("评论");
		listView = (PullToRefreshListView) this.findViewById(R.id.pl_comment_list);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getCommentList(articleID);
			}
		});

		listView.setRefreshing(false);// 设置正在刷新 需要修改PullToRefreshListView
		// onRefreshing方法中把adapter.isEmpty()去掉，否则第一次进来不会有刷新的效果
	}

	private void getCommentList(final long articleID) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil(CommentList.this, handler);
				util.requestCommentList(articleID);
			}

		}).start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
