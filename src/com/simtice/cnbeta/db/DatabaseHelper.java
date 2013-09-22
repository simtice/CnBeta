package com.simtice.cnbeta.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.simtice.cnbeta.bean.Comment;
import com.simtice.cnbeta.bean.HMComment;
import com.simtice.cnbeta.bean.NewsList;
import com.simtice.cnbeta.bean.Top10;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "cnBeta.db";
	private static final int DATABASE_VERSION = 1;
	private static DatabaseHelper databaseHelper = null;

	private Dao<NewsList, Integer> newsListDao = null;
	private Dao<Comment, Integer> commentDao = null;
	private Dao<HMComment, Integer> hmCommentDao = null;
	private Dao<Top10, Integer> top10Dao = null;
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, NewsList.class);
			TableUtils.createTable(connectionSource, Comment.class);
			TableUtils.createTable(connectionSource, Top10.class);
			TableUtils.createTable(connectionSource, HMComment.class);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}
	
	public Dao<NewsList, Integer> getNewslistDataDao() throws SQLException {
		if (newsListDao == null) {
			newsListDao = getDao(NewsList.class);
		}
		return newsListDao;
	}
	public Dao<Comment, Integer> getCommentDao() throws SQLException {
		if (commentDao == null) {
			commentDao = getDao(Comment.class);
		}
		return commentDao;
	}
	public Dao<HMComment,Integer> getHMCommentDao() throws SQLException {
		if (hmCommentDao == null) {
			hmCommentDao = getDao(HMComment.class);
		}
		return hmCommentDao;
	}
	public Dao<Top10,Integer> getTop10Dao() throws SQLException {
		if (top10Dao == null) {
			top10Dao = getDao(Top10.class);
		}
		return top10Dao;
	}

	public static DatabaseHelper getHelper(Context context) {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		}
		return databaseHelper;
	}
	
	
	
	@Override
	public void close() {
		super.close();
		newsListDao = null;
		commentDao = null;
		top10Dao = null;
		hmCommentDao = null;
	}
}
