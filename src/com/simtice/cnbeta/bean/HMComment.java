package com.simtice.cnbeta.bean;

import com.j256.ormlite.field.DatabaseField;

public class HMComment {
	@DatabaseField(id = true)
	private long HMID;
	@DatabaseField
	private String comment;
	@DatabaseField
	private long ArticleID;
	@DatabaseField
	private String name;
	@DatabaseField
	private String title;
	@DatabaseField
	private int cmtClosed;
	@DatabaseField
	private int cmtnum;
	@DatabaseField
	private boolean isFavorite;

	public long getHMID() {
		return HMID;
	}

	public void setHMID(long hMID) {
		HMID = hMID;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getArticleID() {
		return ArticleID;
	}

	public void setArticleID(long articleID) {
		ArticleID = articleID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
