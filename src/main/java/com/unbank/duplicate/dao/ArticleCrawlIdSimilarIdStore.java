package com.unbank.duplicate.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleCrawlIDSimilarId;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleCrawlIDSimilarIdMapper;

public class ArticleCrawlIdSimilarIdStore {

	public void saveArticleCrawlIdSimilarId(
			ArticleCrawlIDSimilarId articleCrawlIDSimilarId) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleCrawlIDSimilarIdMapper articleCrawlIDSimilarIdMapper = sqlSession
					.getMapper(ArticleCrawlIDSimilarIdMapper.class);
			articleCrawlIDSimilarIdMapper
					.insertSelective(articleCrawlIDSimilarId);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

}
