package com.simtice.cnbeta.ui;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.adapter.HMAdapter;
import com.simtice.cnbeta.bean.HMComment;
import com.simtice.cnbeta.db.DatabaseHelper;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.ExceptionUtil;
import com.simtice.cnbeta.util.HttpUtil;
import com.simtice.cnbeta.util.JsonUtil;

public class HMCommentFragment extends SherlockFragment {
	private PullToRefreshListView listView;
	private List<HMComment> lists;
	private HMAdapter adapter;
	private Dao<HMComment, Integer> hmCommentDao = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		lists = new ArrayList<HMComment>();
		adapter = new HMAdapter(getActivity(), lists);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.common_list, container, false);
		listView = (PullToRefreshListView) view.findViewById(R.id.pl_common_list);
		listView.setAdapter(adapter);
		try {
			hmCommentDao = DatabaseHelper.getHelper(getActivity()).getHMCommentDao();
			lists.addAll(hmCommentDao.queryForAll());
			adapter.notifyDataSetChanged();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
				intent.putExtra("ArticleID", lists.get(arg2 - 1).getArticleID());
				intent.putExtra("title", lists.get(arg2 - 1).getTitle());
				startActivity(intent);
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getHMComment();
			}
		});
		listView.setRefreshing(false);

		return view;
	}

	private void getHMComment() {
		final MyHandler handler = new MyHandler(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil(getActivity(), handler);
				util.httpGet(Constant.URL_GETHMCOMMENT);
			}

		}).start();
	}

	static class MyHandler extends Handler {
		WeakReference<HMCommentFragment> mFragment;

		public MyHandler(HMCommentFragment fragment) {
			mFragment = new WeakReference<HMCommentFragment>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			HMCommentFragment fragment = mFragment.get();

			if (fragment.getActivity() == null)
				return;

			switch (msg.what) {
			case Constant.REQUEST_SUCCESS:
				Type listType = new TypeToken<ArrayList<HMComment>>() {
				}.getType();

				try {
					List<HMComment> temp = JsonUtil.parseBeanFromJson((String) msg.obj, listType);
					fragment.lists.clear();
					fragment.lists.addAll(temp);
					fragment.adapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ExceptionUtil.handlException(e, fragment.getActivity());
				}
				fragment.listView.onRefreshComplete();
				break;

			case Constant.REQUEST_FAILED:
				ExceptionUtil.handlException((Exception) msg.obj, fragment.getActivity());
				fragment.listView.onRefreshComplete();
				break;
			case Constant.NO_NETWORK:
				CommonUtil.showNoNetworkToast(fragment.getActivity());
				fragment.listView.onRefreshComplete();
				break;
			}

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			hmCommentDao.delete(hmCommentDao.queryForAll());
			for (HMComment hMComment : lists) {
				hmCommentDao.create(hMComment);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
