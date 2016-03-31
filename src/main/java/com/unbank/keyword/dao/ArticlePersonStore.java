package com.unbank.keyword.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticlePerson;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticlePersonMapper;

public class ArticlePersonStore {

	public void saveArticlePerson(ArticlePerson articlePerson) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticlePersonMapper articlePersonMapper = sqlSession
					.getMapper(ArticlePersonMapper.class);
			articlePersonMapper.insertSelective(articlePerson);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

}
