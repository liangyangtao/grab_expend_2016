package com.unbank.keyword.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleUnbankKeyword;
import com.unbank.mybatis.entity.ArticleUnbankKeywordExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleUnbankKeywordMapper;

public class ArticleUnbankKeywordReader {
	public List<ArticleUnbankKeyword> readUnbankKeyword() {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		List<ArticleUnbankKeyword> articleUnbankKeywords = new ArrayList<ArticleUnbankKeyword>();
		try {
			ArticleUnbankKeywordMapper articleUnbankKeywordMapper = sqlSession
					.getMapper(ArticleUnbankKeywordMapper.class);
			ArticleUnbankKeywordExample example = new ArticleUnbankKeywordExample();
			articleUnbankKeywords = articleUnbankKeywordMapper
					.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return articleUnbankKeywords;
	}
}
