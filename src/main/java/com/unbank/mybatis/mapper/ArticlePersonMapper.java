package com.unbank.mybatis.mapper;

import com.unbank.mybatis.entity.ArticlePerson;
import com.unbank.mybatis.entity.ArticlePersonExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ArticlePersonMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int countByExample(ArticlePersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int deleteByExample(ArticlePersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int insert(ArticlePerson record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int insertSelective(ArticlePerson record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    List<ArticlePerson> selectByExample(ArticlePersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    ArticlePerson selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int updateByExampleSelective(@Param("record") ArticlePerson record, @Param("example") ArticlePersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int updateByExample(@Param("record") ArticlePerson record, @Param("example") ArticlePersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int updateByPrimaryKeySelective(ArticlePerson record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_crawl_person
     *
     * @mbggenerated Sat Feb 28 10:33:05 CST 2015
     */
    int updateByPrimaryKey(ArticlePerson record);
}