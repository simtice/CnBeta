package com.simtice.cnbeta.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.util.CommonLog;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.HttpUtil;

public class NewsDetail extends SherlockActivity{
	private WebView webView;
	private WebSettings wSet;
	private CommonLog log;
	private long articleID;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.REQUEST_SUCCESS:
				log.d(msg.obj);
				// webView.loadData((String) msg.obj, "text/html", "utf-8");
				webView.loadDataWithBaseURL(null, (String) msg.obj, "text/html", "utf-8", null);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setTheme(R.style.Sherlock___Theme_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		initView();
		init();

	}

	@Override
	public void onActionModeFinished(ActionMode mode) {
		// TODO Auto-generated method stub
		super.onActionModeFinished(mode);
		
	}
	private void init() {
		log = new CommonLog("NewsDetail");
		Intent intent = getIntent();
		articleID = intent.getLongExtra("ArticleID", 0);
		if (articleID == 0) {
			CommonUtil.showToask(getApplicationContext(), "加载新闻失败");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil(getApplicationContext(), handler);
				util.requestNewsDetail(articleID);
			}
		}).start();
	}

	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		webView = (WebView) findViewById(R.id.wv_newsdetail);
		wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		wSet.setDefaultTextEncodingName("utf-8");
		webView.setWebViewClient(new MyWebviewClient());
	}

	private class MyWebviewClient extends WebViewClient {
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// // TODO Auto-generated method stub
		// if (url.contains("loadgroupimg.do")) {
		// Intent intent = new Intent(NewsDetailAct.this,
		// GroupPicsAct.class);
		// intent.putExtra("title", "国家主席习近平微服私访乘坐出租车");
		// startActivity(intent);
		// return true;
		// }
		// return super.shouldOverrideUrlLoading(view, url);
		// }

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			setSupportProgressBarIndeterminateVisibility(false);

		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			setSupportProgressBarIndeterminateVisibility(true);
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.web:
			if(articleID!=0){
				String url = "http://www.cnbeta.com/articles/" + articleID + ".htm";
				Uri u = Uri.parse(url);
				Intent it = new Intent(Intent.ACTION_VIEW, u);
				startActivity(it);
			}
			break;
		case R.id.share:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
//			intent.putExtra(Intent.EXTRA_TEXT, text.getText());
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//			startActivity(Intent.createChooser(intent, "将资讯分享到"));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
