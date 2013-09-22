package com.simtice.cnbeta.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil {

	// private static final CommonLog log = LogFactory.createLog();

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath:
																				// /data/data/
		}
	}

	public static boolean checkNetState(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	public static void showToast(Context context, String tip) {
		Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
	}
	
	public static void showNoNetworkToast(Context context){
		Toast.makeText(context, "网络不可用，请检查网络连接设置", Toast.LENGTH_SHORT).show();
	}

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	public static String[] splitArray(String str) {
		String[] contents = null;
		for (int i = 0; i < str.length(); i++) {
			contents = str.split(",");
		}
		return contents;
	}

	/**
	 * 判断网络是否可用(Wi-Fi/3G/ETHERENT)
	 * 
	 * @return true:可用 false:不可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		// 判断是否正在使用WI-FI网络
		State wifiState = null;
		if (connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {
			wifiState = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取Wi-Fi网络连接状态
		}
		// 判断是否正在使用GPRS网络
		State mobileState = null;
		if (connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
			mobileState = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取GPRS网络连接状态
		}
		// //判断是否正在使用以太网网络
		// State ethernetState = null;
		// if (connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) !=
		// null) {
		// ethernetState =
		// connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState();
		// //获取以太网网络连接状态
		// }
		// if (android.net.NetworkInfo.State.CONNECTED == wifiState
		// || (mobileState != null && android.net.NetworkInfo.State.CONNECTED ==
		// mobileState)
		// || (ethernetState != null && android.net.NetworkInfo.State.CONNECTED
		// == ethernetState)){
		if (android.net.NetworkInfo.State.CONNECTED == wifiState
				|| (mobileState != null && android.net.NetworkInfo.State.CONNECTED == mobileState)) {
			// 有可用网络
			return true;
			// } else if (State.DISCONNECTED == wifiState && State.DISCONNECTED
			// == mobileState && State.DISCONNECTED == ethernetState){
		} else if (State.DISCONNECTED == wifiState && State.DISCONNECTED == mobileState) {
			// 网络不可用
			return false;
		}
		return false;
	}

	/**
	 * 判断wifi是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnect(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	public static void showAlertDialog(Context context, String title, String message, String positiveText,
			DialogInterface.OnClickListener positiveListener, String negativeText,
			DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		if (!TextUtils.isEmpty(message))
			dialog.setMessage(message);
		if (!TextUtils.isEmpty(title))
			dialog.setTitle(title);
		dialog.setPositiveButton(positiveText, positiveListener);
		dialog.setNegativeButton(negativeText, negativeListener);
		dialog.create().show();
	}
	
	/**
	 * 解析摘要
	 * @param summary
	 * @return
	 */
	public static String parseHtml(String summary){
		Document doc = Jsoup.parse(summary);
		StringBuffer sb = new StringBuffer();
		for (Element ele : doc.getElementsByTag("p")) {
			if(!ele.select("strong").toString().equals("")){ 
				sb.append(ele.text()+"\n");
			}else{
				sb.append(ele.text());
			}
		}
		return sb.toString().trim();
	}
}
