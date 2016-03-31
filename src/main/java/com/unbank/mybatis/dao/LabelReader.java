package com.unbank.mybatis.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.classify.entity.Label;
import com.unbank.mybatis.entity.SQLAdapter;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.SQLAdapterMapper;

public class LabelReader {
	private static Log logger = LogFactory.getLog(LabelReader.class);

	public List<Label> selectLabel() {

		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		List<Label> labels = null;
		try {
			SQLAdapterMapper sqlAdapterMapper = sqlSession
					.getMapper(SQLAdapterMapper.class);
			SQLAdapter sqlAdapter = filllabelsSQLAdapter();
			labels = sqlAdapterMapper.selectlabels(sqlAdapter);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}

		return labels;
	}

	private SQLAdapter filllabelsSQLAdapter() {
		SQLAdapter sqlAdapter = new SQLAdapter();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select id,classname,state,strategytype from class");
		sqlAdapter.setSql(sqlBuffer.toString());

		return sqlAdapter;
	}
}
