package com.unbank.mybatis.mapper;

import com.unbank.mybatis.entity.ClassWebsiteid;
import com.unbank.mybatis.entity.ClassWebsiteidExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClassWebsiteidMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int countByExample(ClassWebsiteidExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int deleteByExample(ClassWebsiteidExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int insert(ClassWebsiteid record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int insertSelective(ClassWebsiteid record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    List<ClassWebsiteid> selectByExample(ClassWebsiteidExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    ClassWebsiteid selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int updateByExampleSelective(@Param("record") ClassWebsiteid record, @Param("example") ClassWebsiteidExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int updateByExample(@Param("record") ClassWebsiteid record, @Param("example") ClassWebsiteidExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int updateByPrimaryKeySelective(ClassWebsiteid record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table class_websiteid
     *
     * @mbggenerated Fri Mar 27 11:40:50 GMT+08:00 2015
     */
    int updateByPrimaryKey(ClassWebsiteid record);
}