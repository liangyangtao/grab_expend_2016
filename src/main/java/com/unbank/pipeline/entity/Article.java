package com.unbank.pipeline.entity;

public class Article {

	private Integer crawlId;
	private String title;
	private String content;
	private Long newstime;
	private String labels;
	private Integer websiteId;
	private String keywords;

	public Integer getCrawlId() {
		return crawlId;
	}

	public void setCrawlId(Integer crawlId) {
		this.crawlId = crawlId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getNewstime() {
		return newstime;
	}

	public void setNewstime(Long newstime) {
		this.newstime = newstime;
	}

}
