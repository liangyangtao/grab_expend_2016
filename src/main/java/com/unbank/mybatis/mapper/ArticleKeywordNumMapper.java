package com.unbank.mybatis.mapper;

import com.unbank.mybatis.entity.ArticleKeywordNum;
import com.unbank.mybatis.entity.ArticleKeywordNumExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ArticleKeywordNumMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int countByExample(ArticleKeywordNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int deleteByExample(ArticleKeywordNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int insert(ArticleKeywordNum record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int insertSelective(ArticleKeywordNum record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	List<ArticleKeywordNum> selectByExample(ArticleKeywordNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	ArticleKeywordNum selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int updateByExampleSelective(@Param("record") ArticleKeywordNum record,
			@Param("example") ArticleKeywordNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int updateByExample(@Param("record") ArticleKeywordNum record,
			@Param("example") ArticleKeywordNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int updateByPrimaryKeySelective(ArticleKeywordNum record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_keyword_num
	 * @mbggenerated  Tue Jan 27 09:49:34 CST 2015
	 */
	int updateByPrimaryKey(ArticleKeywordNum record);
}