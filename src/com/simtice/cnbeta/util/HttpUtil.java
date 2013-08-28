package com.simtice.cnbeta.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simtice.cnbeta.bean.Truncated;

public class HttpUtil {

	private String urlStr = "http://api.cnbeta.com/api/getNewsList.php?limit=5";
	String TAG = "HttpUtil";

	public void urlConn() {
		try {
			// URL
			URL url = new URL(urlStr);
			// HttpURLConnection
			HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
			httpconn.setRequestProperty("Connection", "Keep-Alive");
			httpconn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android;async-http/1.4.1)");
			httpconn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");

			httpconn.connect();

			if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Log.i(TAG, "请求成功");
				InputStream inStream = httpconn.getInputStream();
				StringBuffer content = new StringBuffer();

				BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
				String inputLine = null;
				while (((inputLine = buffer.readLine()) != null)) {
					content.append(inputLine);
				}

				inStream.close();
				// Log.i(TAG, content.toString());
				parseBeanFromJson(content.toString());
			}
			httpconn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private String subStringStr(String str,String tag1,String tag2,String tag3,String tag4) {
////		String temp = str;
////		StringBuffer sb = new StringBuffer();
////		sb.append(sub(str,tag1,tag2))
//		
////		String str1 = temp.substring(temp.indexOf("<p>") + "<p>".length(), temp.indexOf("<strong>"));
////		String str2 = temp.substring(temp.indexOf("<strong>") + "<strong>".length(), temp.indexOf("</strong>"));
////		String str3 = temp.substring(temp.indexOf("</strong>") + "</strong>".length(), temp.indexOf("</p>"));
//
//		return str1 + str2 + str3;
//	}
	
	private String sub(String str,String tag1,String tag2){
		return str.substring(str.indexOf(tag1) + tag1.length(), str.indexOf(tag2));
	}

	private void parseBeanFromJson(String jsonData) throws UnsupportedEncodingException {
		Type listType = new TypeToken<ArrayList<Truncated>>() {
		}.getType();
		Gson gson = new Gson();
		ArrayList<Truncated> users = gson.fromJson(jsonData, listType);
		for (Truncated truncated : users) {
//			// System.out.println(truncated.getPubtime());
//			System.out.println(truncated.getTitle());
//			// System.out.println(truncated.getTopicLogo());
			System.out.println(truncated.getSummary());
		}

	}
}
