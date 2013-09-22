package com.simtice.cnbeta.bean;

import com.j256.ormlite.field.DatabaseField;

public class Top10 {
	@DatabaseField(id = true)
	private long ArticleID;
	@DatabaseField
	private long counter;
	@DatabaseField
	private String title;
	@DatabaseField
	private int cmtClosed;
	@DatabaseField
	private int cmtnum;
	@DatabaseField
	private boolean isFavorite;

	public long getArticleID() {
		return ArticleID;
	}

	public void setArticleID(long articleID) {
		ArticleID = articleID;
	}

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCmtClosed() {
		return cmtClosed;
	}

	public void setCmtClosed(int cmtClosed) {
		this.cmtClosed = cmtClosed;
	}

	public int getCmtnum() {
		return cmtnum;
	}

	public void setCmtnum(int cmtnum) {
		this.cmtnum = cmtnum;
	}

}
