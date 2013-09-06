package com.simtice.cnbeta.bean;


public class NewsList {
	private String title;
	private String pubtime;
	private long ArticleID;
	private boolean cmtClosed;
	private String summary;
	private String topicLogo;
	private int cmtnum;
	
	private String theme;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPubtime() {
		return pubtime;
	}

	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}

	public long getArticleID() {
		return ArticleID;
	}

	public void setArticleID(long articleID) {
		ArticleID = articleID;
	}

	public boolean isCmtClosed() {
		return cmtClosed;
	}

	public void setCmtClosed(boolean cmtClosed) {
		this.cmtClosed = cmtClosed;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTopicLogo() {
		return topicLogo;
	}

	public void setTopicLogo(String topicLogo) {
		this.topicLogo = topicLogo;
	}

	public int getCmtnum() {
		return cmtnum;
	}

	public void setCmtnum(int cmtnum) {
		this.cmtnum = cmtnum;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

}
