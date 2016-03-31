package com.unbank.mybatis.mapper;

import com.unbank.mybatis.entity.ClassKeyword;
import com.unbank.mybatis.entity.ClassKeywordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClassKeywordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int countByExample(ClassKeywordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int deleteByExample(ClassKeywordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int insert(ClassKeyword record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int insertSelective(ClassKeyword record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    List<ClassKeyword> selectByExample(ClassKeywordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    ClassKeyword selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int updateByExampleSelective(@Param("record") ClassKeyword record, @Param("example") ClassKeywordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int updateByExample(@Param("record") ClassKeyword record, @Param("example") ClassKeywordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int updateByPrimaryKeySelective(ClassKeyword record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_keyword
     *
     * @mbggenerated Tue Apr 14 10:14:15 GMT+08:00 2015
     */
    int updateByPrimaryKey(ClassKeyword record);
}