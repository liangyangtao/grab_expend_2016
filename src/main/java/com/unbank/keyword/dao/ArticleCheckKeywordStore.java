package com.unbank.keyword.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleCheckKeyword;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleCheckKeywordMapper;

public class ArticleCheckKeywordStore {

	public void saveArticleCheckKeyword(ArticleCheckKeyword articleCheckKeyword) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleCheckKeywordMapper articleCheckKeywordMapper = sqlSession
					.getMapper(ArticleCheckKeywordMapper.class);
			articleCheckKeywordMapper.insertSelective(articleCheckKeyword);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

}
