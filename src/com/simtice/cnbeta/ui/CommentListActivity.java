package com.simtice.cnbeta.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.adapter.CommentAdapter;
import com.simtice.cnbeta.bean.Comment;
import com.simtice.cnbeta.db.DatabaseHelper;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.ExceptionUtil;
import com.simtice.cnbeta.util.HttpUtil;
import com.simtice.cnbeta.util.JsonUtil;

public class CommentListActivity extends SherlockActivity implements ActionBar.OnNavigationListener {
	private PullToRefreshListView listView;
	private long articleID;
	private CommentAdapter adapter;
	private List<Comment> lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Sherlock___Theme_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_list);
		init();
		initView();
	}

	private void init() {
		articleID = getIntent().getLongExtra("ArticleID", 0);
		lists = new ArrayList<Comment>();
	}

	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);

		ArrayAdapter<CharSequence> navigationAdapter = ArrayAdapter.createFromResource(getSupportActionBar().getThemedContext(),
				R.array.comment, R.layout.sherlock_spinner_item);
		navigationAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		this.getSupportActionBar().setTitle("");
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(navigationAdapter, this);
		listView = (PullToRefreshListView) this.findViewById(R.id.pl_common_list);
		adapter = new CommentAdapter(this, lists);
		listView.setAdapter(adapter);

		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getCommentList(articleID);
			}
		});

		listView.setRefreshing(false);// 设置正在刷新 需要修改PullToRefreshListView
		// onRefreshing方法中把adapter.isEmpty()去掉，否则第一次进来不会刷新
	}

	private void getCommentList(final long articleID) {
		final Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (getApplicationContext() == null)
					return;
				switch (msg.what) {
				case Constant.REQUEST_SUCCESS:
					try {
						Type listType = new TypeToken<ArrayList<Comment>>() {
						}.getType();
						List<Comment> list = JsonUtil.parseBeanFromJson((String) msg.obj, listType);
						for (Comment comment : list) {
							comment.setArticleID(articleID);
							DatabaseHelper.getHelper(getApplicationContext()).getCommentDao().create(comment);
						}
						notifyDataSetChanged(0);
						if (lists.size() == 0) {
							CommonUtil.showToast(getApplicationContext(), "暂无评论");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ExceptionUtil.handlException(e, CommentListActivity.this);
					}
					listView.onRefreshComplete();
					break;
				case Constant.REQUEST_FAILED:
					ExceptionUtil.handlException((Exception) msg.obj, getApplicationContext());
					listView.onRefreshComplete();
					break;
				case Constant.NO_NETWORK:
					CommonUtil.showNoNetworkToast(getApplicationContext());
					listView.onRefreshComplete();
					break;
				}
			};
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil(CommentListActivity.this, handler);
				util.httpGet(Constant.URL_GETCOMMENT + articleID);
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

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		notifyDataSetChanged(itemPosition);
		return false;
	}

	private void notifyDataSetChanged(int type) {
		lists.clear();
		lists.addAll(queryComment(type));
		adapter.notifyDataSetChanged();
	}

	/**
	 * 查询评论
	 * 
	 * @param type
	 *            1：所有评论 2：热门评论
	 * @return
	 */
	private List<Comment> queryComment(int type) {
		try {
			Dao<Comment, Integer> commentDao = DatabaseHelper.getHelper(getApplicationContext()).getCommentDao();
			QueryBuilder<Comment, Integer> builder = commentDao.queryBuilder();
			builder.where().eq("ArticleID", articleID);
			switch (type) {
			case 0:
				builder.orderBy("tid", false);
				break;
			case 1:
				builder.orderBy("support", false).orderBy("tid", false);
				break;
			}

			return builder.query();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
