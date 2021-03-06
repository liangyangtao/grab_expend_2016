package com.unbank.mybatis.mapper;

import com.unbank.mybatis.entity.UserPushInfo;
import com.unbank.mybatis.entity.UserPushInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserPushInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int countByExample(UserPushInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int deleteByExample(UserPushInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int insert(UserPushInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int insertSelective(UserPushInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    List<UserPushInfo> selectByExample(UserPushInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    UserPushInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int updateByExampleSelective(@Param("record") UserPushInfo record, @Param("example") UserPushInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int updateByExample(@Param("record") UserPushInfo record, @Param("example") UserPushInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int updateByPrimaryKeySelective(UserPushInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_push_info
     *
     * @mbggenerated Fri Jun 19 10:15:51 GMT+08:00 2015
     */
    int updateByPrimaryKey(UserPushInfo record);
}