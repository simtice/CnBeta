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
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.adapter.NewsListAdapter;
import com.simtice.cnbeta.bean.NewsList;
import com.simtice.cnbeta.db.DatabaseHelper;
import com.simtice.cnbeta.util.CommonLog;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.ExceptionUtil;
import com.simtice.cnbeta.util.HttpUtil;
import com.simtice.cnbeta.util.JsonUtil;

/**
 * 新闻列表界面
 * 
 * @author simtice
 * 
 */
public class NewsListFragment extends SherlockFragment {
	private PullToRefreshListView listView;
	private List<NewsList> list;
	private BaseAdapter adapter;
	private CommonLog log;
	private Dao<NewsList, Integer> newsListDao = null;

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			newsListDao.delete(newsListDao.queryForAll());
			for (NewsList newsList : list) {
				newsListDao.create(newsList);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log = new CommonLog("MainNewsListActivity");
	}

	private void getNewsList(final int type, final long articleID) {
		final MyHandler handler = new MyHandler(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil(getActivity().getApplicationContext(), handler);
				util.requestNewsList(Constant.URL_GETNEWSLIST, type, articleID);
			}

		}).start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.news_list, container, false);

		listView = (PullToRefreshListView) view.findViewById(R.id.pl_main_list);
		list = new ArrayList<NewsList>();

		adapter = new NewsListAdapter(list, getActivity(), listView);
		listView.setAdapter(adapter);
		try {
			newsListDao = DatabaseHelper.getHelper(getActivity()).getNewslistDataDao();
			list.addAll(newsListDao.queryForAll());
			for (NewsList news : list) {
				log.i(news.getTheme());
				log.i(news.getTopicLogo());
				log.i("-------------------------------");
			}
			adapter.notifyDataSetChanged();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
				intent.putExtra("ArticleID", list.get(arg2 - 1).getArticleID());// 因为listview下拉刷新的机制，要-1
				intent.putExtra("title", list.get(arg2 - 1).getTitle());
				startActivity(intent);
			}
		});

		listView.setRefreshing(false);// 设置正在刷新 需要修改PullToRefreshListView
										// onRefreshing方法中把adapter.isEmpty()去掉，否则第一次进来不会刷新
										// 必须要在setAdapter之后

		return view;
	}

	/**
	 * ADT20以后加入了一条新的检查规则：确保类内部的handler不含有对外部类的隐式引用 。
	 * Handler类应该应该为static类型，否则有可能造成泄露
	 * 。在程序消息队列中排队的消息保持了对目标Handler类的应用。如果Handler是个内部类
	 * ，那么它也会保持它所在的外部类的引用。为了避免泄露这个外部类，应该将Handler声明为static嵌套类，并且使用对外部类的弱应用。
	 * 
	 * @author simtice
	 * 
	 */
	static class MyHandler extends Handler {
		WeakReference<NewsListFragment> mFragment;

		MyHandler(NewsListFragment fragment) {
			mFragment = new WeakReference<NewsListFragment>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			NewsListFragment fragment = mFragment.get();

			if (fragment.getActivity() == null)
				return;
			switch (msg.what) {
			case Constant.REQUEST_SUCCESS:
				fragment.log.d(msg.obj);
				Type listType = new TypeToken<ArrayList<NewsList>>() {
				}.getType();
				try {
					List<NewsList> temp = JsonUtil.parseBeanFromJson((String) msg.obj, listType);
					if (msg.arg1 == Constant.TPYE_PULL_DOWN) {
						fragment.list.clear();// 如果是下拉刷新就先清除数据
						fragment.list.addAll(temp);
						fragment.listView.setMode(Mode.BOTH);
					} else if (msg.arg1 == Constant.TPYE_PULL_UP)
						fragment.list.addAll(temp);
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

}
