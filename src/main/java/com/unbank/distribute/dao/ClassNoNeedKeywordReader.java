package com.unbank.distribute.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.distribute.entity.NoNeedKeywordEntity;
import com.unbank.mybatis.entity.SQLAdapter;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.SQLAdapterMapper;

public class ClassNoNeedKeywordReader {
	public static Log logger = LogFactory
			.getLog(ClassNoNeedKeywordReader.class);

	public List<NoNeedKeywordEntity> readerNoNeedKeywordEntity() {

		List<NoNeedKeywordEntity> noNeedKeywordEntities = new ArrayList<NoNeedKeywordEntity>();

		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			SQLAdapterMapper sqlAdapterMapper = sqlSession
					.getMapper(SQLAdapterMapper.class);
			SQLAdapter sqlAdapter = fillNoNeedKeywordSQLAdapter();
			noNeedKeywordEntities = sqlAdapterMapper
					.selectNoNeedKeywordEntity(sqlAdapter);
			sqlSession.commit();
		} catch (Exception e) {
			logger.info("读取过滤词出错", e);
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return noNeedKeywordEntities;

	}

	private SQLAdapter fillNoNeedKeywordSQLAdapter() {
		SQLAdapter sqlAdapter = new SQLAdapter();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer
				.append("SELECT classid ,GROUP_CONCAT(keyword) as keywordList from class_no_need_keyword group by classid ");
		sqlAdapter.setSql(sqlBuffer.toString());
		return sqlAdapter;
	}
}
