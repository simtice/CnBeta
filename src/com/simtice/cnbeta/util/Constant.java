package com.simtice.cnbeta.util;

public class Constant {

	public static final String dirName = "mycnBeta";//缓存目录名

	public static final boolean DEBUG = false;

	public static final String URL_BASE = "http://api.cnbeta.com";
	
	public static final String URL_NEWDDETAIL = "/capi/phone/newscontent?articleId=";
	
	public static final String URL_GETNEWSLIST = "/api/getNewsList.php?limit=20";
	public static final String URL_GETCOMMENT = "/api/getComment.php?article=";
	public static final String URL_GETHMCOMMENT = "/api/getHMComment.php?limit=20";
	public static final String URL_GETTOP10 = "/api/getTop10.php";

	public static final int REQUEST_SUCCESS = 1;//请求成功
	public static final int REQUEST_FAILED = -1;//请求失败
	public static final int NO_NETWORK = 0;//无网络
	
	public static final int TPYE_PULL_UP = 1;
	public static final int TPYE_PULL_DOWN = 2;
	
	public static final String UM_APPKEY = "52396cbf56240b4bdd0008c8";
}
