package com.unbank.distribute.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.UserErrorPushInfo;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.UserErrorPushInfoMapper;

public class UserErrorInfoUpdater {

	private static Log logger = LogFactory.getLog(UserErrorInfoUpdater.class);

	public boolean updateUserErrorPushInfo(UserErrorPushInfo userErrorPushInfo) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			UserErrorPushInfoMapper userErrorPushInfoMapper = sqlSession
					.getMapper(UserErrorPushInfoMapper.class);
			userErrorPushInfoMapper
					.updateByPrimaryKeySelective(userErrorPushInfo);
			sqlSession.commit();
			return true;
		} catch (Exception e) {
			logger.info("更新传输失败信息出错", e);
			sqlSession.rollback(true);
			return false;
		} finally {
			sqlSession.close();
		}
	}

}
