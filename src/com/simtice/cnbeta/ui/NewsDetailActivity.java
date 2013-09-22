package com.simtice.cnbeta.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.util.CommonLog;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.Constant;
import com.simtice.cnbeta.util.ExceptionUtil;
import com.simtice.cnbeta.util.HttpUtil;
import com.simtice.cnbeta.util.PreferencesUtil;

/**
 * 新闻详情
 * 
 * @author simtice
 * 
 */
public class NewsDetailActivity extends SherlockActivity {
	private WebView webView;
	private CommonLog log;
	private long articleID;
	private Button btReload;
	private String title;
	private WebSettings wSet;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (getApplicationContext() == null)
				return;

			switch (msg.what) {
			case Constant.REQUEST_SUCCESS:
				log.d(msg.obj);
				webView.loadDataWithBaseURL(null, (String) msg.obj, "text/html", "utf-8", null);
				break;
			case Constant.REQUEST_FAILED:
				ExceptionUtil.handlException((Exception) msg.obj, getApplicationContext());
				setSupportProgress(0);
				btReload.setVisibility(View.VISIBLE);
				break;
			case Constant.NO_NETWORK:
				CommonUtil.showNoNetworkToast(getApplicationContext());
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_PROGRESS);
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
		title = intent.getStringExtra("title");
		requestHtml();
	}

	private void requestHtml() {
		setSupportProgress(2000);// actionbar进度条满格为10000 1000代表10%的进度
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil(getApplicationContext(), handler);
				util.httpGet(Constant.URL_NEWDDETAIL + articleID);
			}
		}).start();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);
		btReload = (Button) this.findViewById(R.id.btn_detail_bt);
		webView = (WebView) findViewById(R.id.wv_newsdetail);
		wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		wSet.setDefaultTextEncodingName("utf-8");
		webView.setWebChromeClient(new WebChromeClient() {

			public void onProgressChanged(WebView view, int progress) {
				setSupportProgress(progress * 100);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			@Override
			// 页面加载错误
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				view.stopLoading();
				view.clearView();
				btReload.setVisibility(View.VISIBLE);
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				System.out.println(url);
				view.loadUrl(url);
				return true;
			}

		});

		// 点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
		// webView.setOnKeyListener(new View.OnKeyListener() {
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// if (event.getAction() == KeyEvent.ACTION_DOWN) {
		// if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { //
		// 表示按返回键时的操作
		// webView.goBack(); // 后退
		// return true; // 已处理
		// }
		// }
		// return false;
		// }
		// });

		setTextSize(PreferencesUtil.getFontPreference(getApplicationContext()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		String url = "http://www.cnbeta.com/articles/" + articleID + ".htm";
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.comment:
			Intent intent = new Intent(NewsDetailActivity.this, CommentListActivity.class);
			intent.putExtra("ArticleID", articleID);
			startActivity(intent);

			break;
		case R.id.share:
			Intent intent1 = new Intent(Intent.ACTION_SEND);
			intent1.setType("text/plain");
			intent1.putExtra(Intent.EXTRA_TEXT, title + "\n" + url + "\n[cnBeta资讯]");
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent1, "将资讯分享到"));

			break;
		case R.id.web:
			Uri u = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, u);
			startActivity(it);
			break;
		case R.id.font:
			String[] items = getResources().getStringArray(R.array.entries_preference_font);
			AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("字体设置").setSingleChoiceItems(items,
					PreferencesUtil.getFontPreference(getApplicationContext()), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							setTextSize(which);
							PreferencesUtil.setTextSize(getApplicationContext(), which);
							dialog.dismiss();
						}
					});
			dialog.setNegativeButton("取消", null);
			dialog.create().show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setTextSize(int fontsize) {
		switch (fontsize) {
		case 0:
			wSet.setTextSize(WebSettings.TextSize.LARGER);
			break;

		case 1:
			wSet.setTextSize(WebSettings.TextSize.NORMAL);
			break;

		case 2:
			wSet.setTextSize(WebSettings.TextSize.SMALLER);
			break;
		}
		PreferencesUtil.setTextSize(this, fontsize);
	}

	public void reLoad(View v) {
		webView.clearView();
		requestHtml();
		btReload.setVisibility(View.GONE);
	}

}
