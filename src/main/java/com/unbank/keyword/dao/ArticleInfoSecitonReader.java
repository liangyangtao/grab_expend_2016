package com.unbank.keyword.dao;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.ArticleInfoExample;
import com.unbank.mybatis.entity.ArticleKeywordNum;
import com.unbank.mybatis.entity.WebSiteInfo;
import com.unbank.mybatis.entity.WebSiteInfoExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleKeywordNumMapper;
import com.unbank.mybatis.mapper.WebSiteInfoMapper;

public class ArticleInfoSecitonReader {

	public String readSectionById(int websiteId) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		String seciton = null;
		try {
			WebSiteInfoMapper webSiteInfoMapper = sqlSession
					.getMapper(WebSiteInfoMapper.class);
			WebSiteInfo webSiteInfo = webSiteInfoMapper
					.selectByPrimaryKey(websiteId);
			seciton = webSiteInfo.getSectionName();
			sqlSession.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return seciton;
	}

}
