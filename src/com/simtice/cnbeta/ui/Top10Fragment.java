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
import com.simtice.cnbeta.adapter.Top10Adapter;
import com.simtice.cnbeta.bean.Top10;
import com.simtice.cnbeta.db.DatabaseHelper;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.ExceptionUtil;
import com.simtice.cnbeta.util.HttpUtil;
import com.simtice.cnbeta.util.JsonUtil;

public class Top10Fragment extends SherlockFragment {
	private PullToRefreshListView listView;
	private List<Top10> lists;
	private Top10Adapter adapter;
	private Dao<Top10, Integer> top10Dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		lists = new ArrayList<Top10>();
		adapter = new Top10Adapter(getActivity(), lists);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			top10Dao.delete(top10Dao.queryForAll());
			for (Top10 top10 : lists) {
				top10Dao.create(top10);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.common_list, container, false);
		listView = (PullToRefreshListView) view.findViewById(R.id.pl_common_list);
		listView.setAdapter(adapter);
		try {
			top10Dao = DatabaseHelper.getHelper(getActivity()).getTop10Dao();
			lists.addAll(top10Dao.queryForAll());
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
				getTop10();
			}
		});
		listView.setRefreshing(false);

		return view;
	}

	static class MyHandler extends Handler{
		WeakReference<Top10Fragment> mFragmet;
		MyHandler(Top10Fragment fragment){
			mFragmet = new WeakReference<Top10Fragment>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			Top10Fragment fragment = mFragmet.get();
			if (fragment.getActivity() == null)
				return;

			switch (msg.what) {
			case Constant.REQUEST_SUCCESS:
				Type listType = new TypeToken<ArrayList<Top10>>() {
				}.getType();
				try {
					List<Top10> temp = JsonUtil.parseBeanFromJson((String) msg.obj, listType);
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
	
	private void getTop10() {
		final MyHandler handler = new MyHandler(this);

		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil(getActivity(), handler);
				util.httpGet(Constant.URL_GETTOP10);
			}

		}).start();
	}
}
