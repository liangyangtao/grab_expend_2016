package com.unbank.keyword.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleOrganization;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleOrganizationMapper;

public class ArticleOrganizationStore {

	public void saveArticleOrganizationStore(
			ArticleOrganization articleOrganization) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleOrganizationMapper articleOrganizationMapper = sqlSession
					.getMapper(ArticleOrganizationMapper.class);
			articleOrganizationMapper.insertSelective(articleOrganization);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

}
