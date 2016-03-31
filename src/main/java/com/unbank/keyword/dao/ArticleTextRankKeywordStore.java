package com.unbank.keyword.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleTextRankKeyword;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleTextRankKeywordMapper;

public class ArticleTextRankKeywordStore {
	public void saveArticleTextRankKeyword(ArticleTextRankKeyword articleTextRankKeyword) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleTextRankKeywordMapper articleTextRankKeywordMapper = sqlSession
					.getMapper(ArticleTextRankKeywordMapper.class);
			articleTextRankKeywordMapper.insertSelective(articleTextRankKeyword);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}
}
