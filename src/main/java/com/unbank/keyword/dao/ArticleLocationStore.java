package com.unbank.keyword.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleLocation;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleLocationMapper;

public class ArticleLocationStore {

	public void saveArticleLocation(ArticleLocation articleLocation) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleLocationMapper articleLocationMapper = sqlSession
					.getMapper(ArticleLocationMapper.class);
			articleLocationMapper.insertSelective(articleLocation);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

}
