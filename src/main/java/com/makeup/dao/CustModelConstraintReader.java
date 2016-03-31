package com.makeup.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.CustModuleConstraint;
import com.unbank.mybatis.entity.CustModuleConstraintExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.CustModuleConstraintMapper;

public class CustModelConstraintReader {
	public List<CustModuleConstraint> readerCustModelConstraintByMid(int mid) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("info_cust").openSession();
		List<CustModuleConstraint> custModuleConstraints = null;
		try {
			CustModuleConstraintMapper custModuleConstraintMapper = sqlSession
					.getMapper(CustModuleConstraintMapper.class);
			CustModuleConstraintExample example = new CustModuleConstraintExample();
			example.or().andMidEqualTo(mid);
			custModuleConstraints = custModuleConstraintMapper
					.selectByExampleWithBLOBs(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return custModuleConstraints;
	}
}
