package com.unbank.mybatis.mapper;

import com.unbank.mybatis.entity.Sectionlabel;
import com.unbank.mybatis.entity.SectionlabelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SectionlabelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int countByExample(SectionlabelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int deleteByExample(SectionlabelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int insert(Sectionlabel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int insertSelective(Sectionlabel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    List<Sectionlabel> selectByExample(SectionlabelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    Sectionlabel selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int updateByExampleSelective(@Param("record") Sectionlabel record, @Param("example") SectionlabelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int updateByExample(@Param("record") Sectionlabel record, @Param("example") SectionlabelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int updateByPrimaryKeySelective(Sectionlabel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plate_label
     *
     * @mbggenerated Wed Mar 11 14:17:45 GMT+08:00 2015
     */
    int updateByPrimaryKey(Sectionlabel record);
}