package com.unbank.duplicate.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleCrawlSimilar;
import com.unbank.mybatis.entity.SQLAdapter;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.SQLAdapterMapper;

public class ArticleCrawlSimilarModifier {
	private static Log logger = LogFactory
			.getLog(ArticleCrawlSimilarModifier.class);

	public void updateArticleCrawlSImilarTask(
			ArticleCrawlSimilar articleCrawlSimilar) {
		String sql = "	update ptf_crawl_similar set istask="
				+ articleCrawlSimilar.getIstask() + " where id   ="
				+ articleCrawlSimilar.getId();
		executeSQL(sql);
	}

	private void executeSQL(String sql) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			SQLAdapterMapper sqlAdapterMaper = sqlSession
					.getMapper(SQLAdapterMapper.class);
			SQLAdapter sqlAdapter = new SQLAdapter();
			sqlAdapter.setSql(sql);
			sqlAdapterMaper.executeSQL(sqlAdapter);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("更新task失败", e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}
}
