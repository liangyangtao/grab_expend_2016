package com.unbank.mybatis.entity;

import java.util.Date;

public class ArticleKeywordNum {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_crawl_keyword_num.id
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_crawl_keyword_num.keyword
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	private String keyword;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_crawl_keyword_num.crawl_time
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	private Date crawlTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_crawl_keyword_num.num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	private Integer num;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_crawl_keyword_num.id
	 * @return  the value of ptf_crawl_keyword_num.id
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_crawl_keyword_num.id
	 * @param id  the value for ptf_crawl_keyword_num.id
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_crawl_keyword_num.keyword
	 * @return  the value of ptf_crawl_keyword_num.keyword
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_crawl_keyword_num.keyword
	 * @param keyword  the value for ptf_crawl_keyword_num.keyword
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_crawl_keyword_num.crawl_time
	 * @return  the value of ptf_crawl_keyword_num.crawl_time
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	public Date getCrawlTime() {
		return crawlTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_crawl_keyword_num.crawl_time
	 * @param crawlTime  the value for ptf_crawl_keyword_num.crawl_time
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	public void setCrawlTime(Date crawlTime) {
		this.crawlTime = crawlTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_crawl_keyword_num.num
	 * @return  the value of ptf_crawl_keyword_num.num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	public Integer getNum() {
		return num;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_crawl_keyword_num.num
	 * @param num  the value for ptf_crawl_keyword_num.num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
}