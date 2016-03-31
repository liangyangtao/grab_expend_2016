package com.unbank.duplicate.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleCrawlIDSimilarId;
import com.unbank.mybatis.entity.ArticleCrawlIDSimilarIdExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleCrawlIDSimilarIdMapper;

public class ArticleCrawlIdSimilarIdReader {

	public List<ArticleCrawlIDSimilarId> readArticleCrawlIdSimilarId(
			Integer crawlid) {
		List<ArticleCrawlIDSimilarId> articleCrawlIDSimilarIds = new ArrayList<ArticleCrawlIDSimilarId>();
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleCrawlIDSimilarIdMapper articleCrawlIDSimilarIdMapper = sqlSession
					.getMapper(ArticleCrawlIDSimilarIdMapper.class);
			ArticleCrawlIDSimilarIdExample example = new ArticleCrawlIDSimilarIdExample();
			example.or().andCrawlidEqualTo(crawlid);
			articleCrawlIDSimilarIds = articleCrawlIDSimilarIdMapper
					.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return articleCrawlIDSimilarIds;
	}
}
