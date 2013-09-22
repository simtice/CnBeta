package com.simtice.cnbeta.bean;

import com.j256.ormlite.field.DatabaseField;

public class Comment {

	@DatabaseField(id = true)
	private long tid;
	@DatabaseField
	private String name;
	@DatabaseField
	private String date;
	@DatabaseField
	private String comment;
	@DatabaseField
	private int support;
	@DatabaseField
	private int against;
	@DatabaseField
	private long ArticleID;

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

	public String getDate() {
		return date;
	}

	public void setDate(String data) {
		this.date = data;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getTid() {
		return tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public int getSupport() {
		return support;
	}

	public void setSupport(int support) {
		this.support = support;
	}

	public int getAgainst() {
		return against;
	}

	public void setAgainst(int against) {
		this.against = against;
	}

}
