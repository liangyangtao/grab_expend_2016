package com.unbank.mybatis.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ArticleCrawlSimilarExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public ArticleCrawlSimilarExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value,
				String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property
						+ " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1,
				Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property
						+ " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andIdIsNull() {
			addCriterion("id is null");
			return (Criteria) this;
		}

		public Criteria andIdIsNotNull() {
			addCriterion("id is not null");
			return (Criteria) this;
		}

		public Criteria andIdEqualTo(Integer value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(Integer value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(Integer value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(Integer value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(Integer value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<Integer> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<Integer> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(Integer value1, Integer value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(Integer value1, Integer value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andCrawlIdIsNull() {
			addCriterion("crawl_id is null");
			return (Criteria) this;
		}

		public Criteria andCrawlIdIsNotNull() {
			addCriterion("crawl_id is not null");
			return (Criteria) this;
		}

		public Criteria andCrawlIdEqualTo(Integer value) {
			addCriterion("crawl_id =", value, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdNotEqualTo(Integer value) {
			addCriterion("crawl_id <>", value, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdGreaterThan(Integer value) {
			addCriterion("crawl_id >", value, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("crawl_id >=", value, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdLessThan(Integer value) {
			addCriterion("crawl_id <", value, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdLessThanOrEqualTo(Integer value) {
			addCriterion("crawl_id <=", value, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdIn(List<Integer> values) {
			addCriterion("crawl_id in", values, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdNotIn(List<Integer> values) {
			addCriterion("crawl_id not in", values, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdBetween(Integer value1, Integer value2) {
			addCriterion("crawl_id between", value1, value2, "crawlId");
			return (Criteria) this;
		}

		public Criteria andCrawlIdNotBetween(Integer value1, Integer value2) {
			addCriterion("crawl_id not between", value1, value2, "crawlId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdIsNull() {
			addCriterion("similar_id is null");
			return (Criteria) this;
		}

		public Criteria andSimilarIdIsNotNull() {
			addCriterion("similar_id is not null");
			return (Criteria) this;
		}

		public Criteria andSimilarIdEqualTo(Integer value) {
			addCriterion("similar_id =", value, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdNotEqualTo(Integer value) {
			addCriterion("similar_id <>", value, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdGreaterThan(Integer value) {
			addCriterion("similar_id >", value, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("similar_id >=", value, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdLessThan(Integer value) {
			addCriterion("similar_id <", value, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdLessThanOrEqualTo(Integer value) {
			addCriterion("similar_id <=", value, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdIn(List<Integer> values) {
			addCriterion("similar_id in", values, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdNotIn(List<Integer> values) {
			addCriterion("similar_id not in", values, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdBetween(Integer value1, Integer value2) {
			addCriterion("similar_id between", value1, value2, "similarId");
			return (Criteria) this;
		}

		public Criteria andSimilarIdNotBetween(Integer value1, Integer value2) {
			addCriterion("similar_id not between", value1, value2, "similarId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdIsNull() {
			addCriterion("webiste_id is null");
			return (Criteria) this;
		}

		public Criteria andWebisteIdIsNotNull() {
			addCriterion("webiste_id is not null");
			return (Criteria) this;
		}

		public Criteria andWebisteIdEqualTo(Integer value) {
			addCriterion("webiste_id =", value, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdNotEqualTo(Integer value) {
			addCriterion("webiste_id <>", value, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdGreaterThan(Integer value) {
			addCriterion("webiste_id >", value, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("webiste_id >=", value, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdLessThan(Integer value) {
			addCriterion("webiste_id <", value, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdLessThanOrEqualTo(Integer value) {
			addCriterion("webiste_id <=", value, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdIn(List<Integer> values) {
			addCriterion("webiste_id in", values, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdNotIn(List<Integer> values) {
			addCriterion("webiste_id not in", values, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdBetween(Integer value1, Integer value2) {
			addCriterion("webiste_id between", value1, value2, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebisteIdNotBetween(Integer value1, Integer value2) {
			addCriterion("webiste_id not between", value1, value2, "webisteId");
			return (Criteria) this;
		}

		public Criteria andWebNameIsNull() {
			addCriterion("web_name is null");
			return (Criteria) this;
		}

		public Criteria andWebNameIsNotNull() {
			addCriterion("web_name is not null");
			return (Criteria) this;
		}

		public Criteria andWebNameEqualTo(String value) {
			addCriterion("web_name =", value, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameNotEqualTo(String value) {
			addCriterion("web_name <>", value, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameGreaterThan(String value) {
			addCriterion("web_name >", value, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameGreaterThanOrEqualTo(String value) {
			addCriterion("web_name >=", value, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameLessThan(String value) {
			addCriterion("web_name <", value, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameLessThanOrEqualTo(String value) {
			addCriterion("web_name <=", value, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameLike(String value) {
			addCriterion("web_name like", value, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameNotLike(String value) {
			addCriterion("web_name not like", value, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameIn(List<String> values) {
			addCriterion("web_name in", values, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameNotIn(List<String> values) {
			addCriterion("web_name not in", values, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameBetween(String value1, String value2) {
			addCriterion("web_name between", value1, value2, "webName");
			return (Criteria) this;
		}

		public Criteria andWebNameNotBetween(String value1, String value2) {
			addCriterion("web_name not between", value1, value2, "webName");
			return (Criteria) this;
		}

		public Criteria andClassnameIsNull() {
			addCriterion("classname is null");
			return (Criteria) this;
		}

		public Criteria andClassnameIsNotNull() {
			addCriterion("classname is not null");
			return (Criteria) this;
		}

		public Criteria andClassnameEqualTo(String value) {
			addCriterion("classname =", value, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameNotEqualTo(String value) {
			addCriterion("classname <>", value, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameGreaterThan(String value) {
			addCriterion("classname >", value, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameGreaterThanOrEqualTo(String value) {
			addCriterion("classname >=", value, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameLessThan(String value) {
			addCriterion("classname <", value, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameLessThanOrEqualTo(String value) {
			addCriterion("classname <=", value, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameLike(String value) {
			addCriterion("classname like", value, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameNotLike(String value) {
			addCriterion("classname not like", value, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameIn(List<String> values) {
			addCriterion("classname in", values, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameNotIn(List<String> values) {
			addCriterion("classname not in", values, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameBetween(String value1, String value2) {
			addCriterion("classname between", value1, value2, "classname");
			return (Criteria) this;
		}

		public Criteria andClassnameNotBetween(String value1, String value2) {
			addCriterion("classname not between", value1, value2, "classname");
			return (Criteria) this;
		}

		public Criteria andHotnumIsNull() {
			addCriterion("hotnum is null");
			return (Criteria) this;
		}

		public Criteria andHotnumIsNotNull() {
			addCriterion("hotnum is not null");
			return (Criteria) this;
		}

		public Criteria andHotnumEqualTo(Integer value) {
			addCriterion("hotnum =", value, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumNotEqualTo(Integer value) {
			addCriterion("hotnum <>", value, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumGreaterThan(Integer value) {
			addCriterion("hotnum >", value, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumGreaterThanOrEqualTo(Integer value) {
			addCriterion("hotnum >=", value, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumLessThan(Integer value) {
			addCriterion("hotnum <", value, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumLessThanOrEqualTo(Integer value) {
			addCriterion("hotnum <=", value, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumIn(List<Integer> values) {
			addCriterion("hotnum in", values, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumNotIn(List<Integer> values) {
			addCriterion("hotnum not in", values, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumBetween(Integer value1, Integer value2) {
			addCriterion("hotnum between", value1, value2, "hotnum");
			return (Criteria) this;
		}

		public Criteria andHotnumNotBetween(Integer value1, Integer value2) {
			addCriterion("hotnum not between", value1, value2, "hotnum");
			return (Criteria) this;
		}

		public Criteria andIstaskIsNull() {
			addCriterion("istask is null");
			return (Criteria) this;
		}

		public Criteria andIstaskIsNotNull() {
			addCriterion("istask is not null");
			return (Criteria) this;
		}

		public Criteria andIstaskEqualTo(Integer value) {
			addCriterion("istask =", value, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskNotEqualTo(Integer value) {
			addCriterion("istask <>", value, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskGreaterThan(Integer value) {
			addCriterion("istask >", value, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskGreaterThanOrEqualTo(Integer value) {
			addCriterion("istask >=", value, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskLessThan(Integer value) {
			addCriterion("istask <", value, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskLessThanOrEqualTo(Integer value) {
			addCriterion("istask <=", value, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskIn(List<Integer> values) {
			addCriterion("istask in", values, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskNotIn(List<Integer> values) {
			addCriterion("istask not in", values, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskBetween(Integer value1, Integer value2) {
			addCriterion("istask between", value1, value2, "istask");
			return (Criteria) this;
		}

		public Criteria andIstaskNotBetween(Integer value1, Integer value2) {
			addCriterion("istask not between", value1, value2, "istask");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeIsNull() {
			addCriterion("crawl_time is null");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeIsNotNull() {
			addCriterion("crawl_time is not null");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeEqualTo(Date value) {
			addCriterion("crawl_time =", value, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeNotEqualTo(Date value) {
			addCriterion("crawl_time <>", value, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeGreaterThan(Date value) {
			addCriterion("crawl_time >", value, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeGreaterThanOrEqualTo(Date value) {
			addCriterion("crawl_time >=", value, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeLessThan(Date value) {
			addCriterion("crawl_time <", value, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeLessThanOrEqualTo(Date value) {
			addCriterion("crawl_time <=", value, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeIn(List<Date> values) {
			addCriterion("crawl_time in", values, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeNotIn(List<Date> values) {
			addCriterion("crawl_time not in", values, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeBetween(Date value1, Date value2) {
			addCriterion("crawl_time between", value1, value2, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andCrawlTimeNotBetween(Date value1, Date value2) {
			addCriterion("crawl_time not between", value1, value2, "crawlTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeIsNull() {
			addCriterion("news_time is null");
			return (Criteria) this;
		}

		public Criteria andNewsTimeIsNotNull() {
			addCriterion("news_time is not null");
			return (Criteria) this;
		}

		public Criteria andNewsTimeEqualTo(Date value) {
			addCriterion("news_time =", value, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeNotEqualTo(Date value) {
			addCriterion("news_time <>", value, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeGreaterThan(Date value) {
			addCriterion("news_time >", value, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeGreaterThanOrEqualTo(Date value) {
			addCriterion("news_time >=", value, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeLessThan(Date value) {
			addCriterion("news_time <", value, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeLessThanOrEqualTo(Date value) {
			addCriterion("news_time <=", value, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeIn(List<Date> values) {
			addCriterion("news_time in", values, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeNotIn(List<Date> values) {
			addCriterion("news_time not in", values, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeBetween(Date value1, Date value2) {
			addCriterion("news_time between", value1, value2, "newsTime");
			return (Criteria) this;
		}

		public Criteria andNewsTimeNotBetween(Date value1, Date value2) {
			addCriterion("news_time not between", value1, value2, "newsTime");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table ptf_crawl_similar
	 * @mbggenerated  Thu Apr 30 11:39:45 GMT+08:00 2015
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue,
				String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table ptf_crawl_similar
     *
     * @mbggenerated do_not_delete_during_merge Wed Apr 22 19:00:45 GMT+08:00 2015
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}