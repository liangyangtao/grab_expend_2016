package com.unbank.classify.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.classify.entity.ClassSectionEntity;
import com.unbank.mybatis.entity.ClassSection;
import com.unbank.mybatis.entity.ClassSectionExample;
import com.unbank.mybatis.entity.WebSiteInfo;
import com.unbank.mybatis.entity.WebSiteInfoExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ClassSectionMapper;
import com.unbank.mybatis.mapper.WebSiteInfoMapper;

public class ClassSectionReader {
	public static Log logger = LogFactory.getLog(ClassSectionReader.class);

	public List<ClassSectionEntity> readClassSectionEntity() {
		List<ClassSectionEntity> classSectionEntities = new ArrayList<ClassSectionEntity>();
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			List<ClassSection> classSecitons = getClassSections(sqlSession);
			List<WebSiteInfo> websiteinfos = getWebsiteInfos(sqlSession);
			for (ClassSection classSection : classSecitons) {
				String className = classSection.getClassname().trim();
				Set<String> websiteidlist = new HashSet<String>();
				ClassSectionEntity classSectionEntity = new ClassSectionEntity();
				classSectionEntity.setClassname(className);
				classSectionEntity.setForumid(classSection.getForumid());
				classSectionEntity.setId(classSection.getId());
				classSectionEntity.setStatus(classSection.getStatus());
				classSectionEntity.setStrucutureid(classSection
						.getStrucutureid());
				classSectionEntity.setTemplateid(classSection.getTemplateid());
				for (WebSiteInfo webSiteInfo : websiteinfos) {
					className = className.replace("1", "");
					if (webSiteInfo.getSectionName().contains(className)) {
						websiteidlist.add(webSiteInfo.getWebsiteId() + "");
					}
				}
				classSectionEntity.setWebsiteList(websiteidlist);
				classSectionEntities.add(classSectionEntity);
			}

			sqlSession.commit();
		} catch (Exception e) {
			logger.info("读取ClassSection表出错", e);
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return classSectionEntities;
	}

	private List<ClassSection> getClassSections(SqlSession sqlSession) {
		ClassSectionMapper classSectionMapper = sqlSession
				.getMapper(ClassSectionMapper.class);
		ClassSectionExample example = new ClassSectionExample();
		example.or().andStatusEqualTo(1);
		List<ClassSection> classSecitons = classSectionMapper
				.selectByExample(example);
		return classSecitons;
	}

	private List<WebSiteInfo> getWebsiteInfos(SqlSession sqlSession) {
		WebSiteInfoMapper webSiteInfoMapper = sqlSession
				.getMapper(WebSiteInfoMapper.class);
		WebSiteInfoExample example = new WebSiteInfoExample();
		example.or().andIstaskEqualTo(2);
		List<WebSiteInfo> websiteinfos = webSiteInfoMapper
				.selectByExample(example);
		return websiteinfos;
	}
}
