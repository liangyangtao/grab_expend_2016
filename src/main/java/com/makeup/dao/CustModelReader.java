package com.makeup.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.CustModule;
import com.unbank.mybatis.entity.CustModuleExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.CustModuleMapper;

public class CustModelReader {

	public List<CustModule> readerCustModelByUserid(long userid) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("info_cust").openSession();
		List<CustModule> custUsers = null;
		try {
			CustModuleMapper custModuleMapper = sqlSession
					.getMapper(CustModuleMapper.class);
			CustModuleExample example = new CustModuleExample();
			example.or().andUseridEqualTo(Integer.parseInt(userid + ""))
					.andParentidEqualTo(0);
			custUsers = custModuleMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return custUsers;
	}

	public List<CustModule> readerCustModelByPid(int pid) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("info_cust").openSession();
		List<CustModule> custUsers = null;
		try {
			CustModuleMapper custModuleMapper = sqlSession
					.getMapper(CustModuleMapper.class);
			CustModuleExample example = new CustModuleExample();
			example.or().andParentidEqualTo(pid);
			custUsers = custModuleMapper.selectByExample(example);
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
