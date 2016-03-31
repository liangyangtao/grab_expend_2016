package com.unbank.distribute.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.UserErrorPushInfo;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.UserErrorPushInfoMapper;

public class UserErrorPushInfoStore {

	private static Log logger = LogFactory.getLog(UserErrorPushInfoStore.class);

	public void saveUserErrorPushInfo(UserErrorPushInfo userErrorPushInfo) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			UserErrorPushInfoMapper userErrorPushInfoMapper = sqlSession
					.getMapper(UserErrorPushInfoMapper.class);
			userErrorPushInfoMapper.insertSelective(userErrorPushInfo);
			sqlSession.commit();
		} catch (Exception e) {
			logger.info("保存传输失败信息出错", e);
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}
}
