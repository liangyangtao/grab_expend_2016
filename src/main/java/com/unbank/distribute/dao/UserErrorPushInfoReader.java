package com.unbank.distribute.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.UserErrorPushInfo;
import com.unbank.mybatis.entity.UserErrorPushInfoExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.UserErrorPushInfoMapper;

public class UserErrorPushInfoReader {

	private static Log logger = LogFactory
			.getLog(UserErrorPushInfoReader.class);

	public List<UserErrorPushInfo> readUserErrorPushInfo() {

		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		List<UserErrorPushInfo> userErrorPushInfos = null;
		try {
			UserErrorPushInfoMapper userErrorPushInfoMapper = sqlSession
					.getMapper(UserErrorPushInfoMapper.class);
			UserErrorPushInfoExample userErrorPushInfoExample = new UserErrorPushInfoExample();
			userErrorPushInfoExample.or().andIstaskEqualTo(0);
			userErrorPushInfoExample.setOrderByClause("id desc limit 1000");
			userErrorPushInfos = userErrorPushInfoMapper
					.selectByExampleWithBLOBs(userErrorPushInfoExample);
			sqlSession.commit();
		} catch (Exception e) {
			logger.info("查询恢复日志出错", e);
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return userErrorPushInfos;

	}
}
