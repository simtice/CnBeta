package com.simtice.cnbeta.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class HttpUtil {

	private CommonLog log = null;
	private Handler handler;
	private final int TIMEOUT = 10 * 1000;
	private int mType;
	private Context context;

	public HttpUtil(Context context, Handler handler) {
		this.log = new CommonLog("HttpUtil");
		this.handler = handler;
		this.context = context;
	}
	
	public void requestNewsList(String curUrl, int type, long articleID) {
		this.mType = type;
		String urlStr = "";
		if (articleID == 0) {
			urlStr = Constant.URL_BASE + curUrl;
		} else {
			urlStr = Constant.URL_BASE + "/api/getNewsList.php?fromArticleId=" + articleID + "&limit=10";
		}
		urlConn(urlStr);
	}

	private void sendMessage(int what, Object obj, int arg1) {
		Message msg = this.handler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		msg.arg1 = arg1;
		this.handler.sendMessage(msg);

	}

	public void httpGet(String url) {
		String temp = Constant.URL_BASE + url;
		urlConn(temp);
	}

	private void urlConn(String urlStr) {
		if (CommonUtil.isNetworkAvailable(this.context)) {
			log.i("request------------------------->" + urlStr);
			InputStream inStream = null;
			HttpURLConnection httpconn = null;
			try {
				URL url = new URL(urlStr);
				httpconn = (HttpURLConnection) url.openConnection();
				httpconn.setRequestProperty("Connection", "Keep-Alive");
				httpconn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android;async-http/1.4.1)");
				httpconn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
				httpconn.setConnectTimeout(TIMEOUT);
				httpconn.setReadTimeout(TIMEOUT);
				inStream = httpconn.getInputStream();
				if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					StringBuffer content = new StringBuffer();
					BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
					String inputLine = null;
					while (((inputLine = buffer.readLine()) != null)) {
						content.append(inputLine);
					}
					sendMessage(Constant.REQUEST_SUCCESS, content.toString(), this.mType);
					log.i(content.toString());
				} else {

				}
			} catch (Exception e) {
				e.printStackTrace();
				sendMessage(Constant.REQUEST_FAILED, e, 0);
			}
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendMessage(Constant.REQUEST_FAILED, e, 0);
				}
			}
			httpconn.disconnect();
		} else {
			this.handler.sendEmptyMessage(Constant.NO_NETWORK);
		}
	}

}
