package com.makeup.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.CustUser;
import com.unbank.mybatis.entity.CustUserExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.CustUserMapper;

public class CustUserReader {
	public List<CustUser> readerUserByEnable(int enable) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("info_cust").openSession();
		List<CustUser> custUsers = null;
		try {
			CustUserMapper custUserMapper = sqlSession
					.getMapper(CustUserMapper.class);
			CustUserExample example = new CustUserExample();
			example.or().andEnabledEqualTo((byte) enable);
			custUsers = custUserMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return custUsers;
	}
}
