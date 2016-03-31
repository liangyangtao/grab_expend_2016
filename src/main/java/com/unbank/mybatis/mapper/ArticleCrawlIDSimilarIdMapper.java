package com.unbank.mybatis.mapper;

import com.unbank.mybatis.entity.ArticleCrawlIDSimilarId;
import com.unbank.mybatis.entity.ArticleCrawlIDSimilarIdExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ArticleCrawlIDSimilarIdMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int countByExample(ArticleCrawlIDSimilarIdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int deleteByExample(ArticleCrawlIDSimilarIdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int insert(ArticleCrawlIDSimilarId record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int insertSelective(ArticleCrawlIDSimilarId record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    List<ArticleCrawlIDSimilarId> selectByExample(ArticleCrawlIDSimilarIdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    ArticleCrawlIDSimilarId selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int updateByExampleSelective(@Param("record") ArticleCrawlIDSimilarId record, @Param("example") ArticleCrawlIDSimilarIdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int updateByExample(@Param("record") ArticleCrawlIDSimilarId record, @Param("example") ArticleCrawlIDSimilarIdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int updateByPrimaryKeySelective(ArticleCrawlIDSimilarId record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawlid_similarid
     *
     * @mbggenerated Wed Mar 11 14:49:44 GMT+08:00 2015
     */
    int updateByPrimaryKey(ArticleCrawlIDSimilarId record);
}