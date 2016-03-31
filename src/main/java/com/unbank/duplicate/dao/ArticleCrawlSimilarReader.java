package com.unbank.duplicate.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleCrawlSimilar;
import com.unbank.mybatis.entity.ArticleCrawlSimilarExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleCrawlSimilarMapper;

public class ArticleCrawlSimilarReader {
	public List<ArticleCrawlSimilar> readArticleCrawlSimilar(Integer istask) {
		List<ArticleCrawlSimilar> articleCrawlSimilars = null;
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleCrawlSimilarMapper articleCrawlSimilarMapper = sqlSession
					.getMapper(ArticleCrawlSimilarMapper.class);
			ArticleCrawlSimilarExample example = new ArticleCrawlSimilarExample();
			example.or().andIstaskEqualTo(istask);
			example.setOrderByClause(" id desc limit 1000");
			articleCrawlSimilars = articleCrawlSimilarMapper
					.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return articleCrawlSimilars;
	}
}
