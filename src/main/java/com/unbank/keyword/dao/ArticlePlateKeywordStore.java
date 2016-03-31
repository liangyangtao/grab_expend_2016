package com.unbank.keyword.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticlePlateKeyword;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticlePlateKeywordMapper;

public class ArticlePlateKeywordStore {

	public void saveArticlePlateKeyword(ArticlePlateKeyword articlePlateKeyword) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticlePlateKeywordMapper articlePlateKeywordMapper = sqlSession
					.getMapper(ArticlePlateKeywordMapper.class);
			articlePlateKeywordMapper.insertSelective(articlePlateKeyword);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

}
