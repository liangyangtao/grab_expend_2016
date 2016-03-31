package com.unbank.duplicate.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleCrawlSimilar;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleCrawlSimilarMapper;

public class ArticleCrawlSimilarStore {
	public void saveArticleCrawlSimilar(ArticleCrawlSimilar articleCrawlSimilar) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleCrawlSimilarMapper articleCrawlSimilarMapper = sqlSession
					.getMapper(ArticleCrawlSimilarMapper.class);
			articleCrawlSimilarMapper.insertSelective(articleCrawlSimilar);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}
}
