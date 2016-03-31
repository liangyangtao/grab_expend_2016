package com.unbank.mybatis.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.WebSiteInfo;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.WebSiteInfoMapper;

public class WebsiteInfoReader {
	private static Log logger = LogFactory.getLog(WebsiteInfoReader.class);

	public WebSiteInfo readerWebSiteInfoByWebsiteID(int websiteid) {
		WebSiteInfo webSiteInfo = null;
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {

			WebSiteInfoMapper webSiteInfoMapper = sqlSession
					.getMapper(WebSiteInfoMapper.class);
			webSiteInfo = webSiteInfoMapper.selectByPrimaryKey(websiteid);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询websieInfo失败", e);
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return webSiteInfo;
	}
}
