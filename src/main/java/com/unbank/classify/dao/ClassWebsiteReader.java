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
import com.unbank.mybatis.entity.ClassWebsiteid;
import com.unbank.mybatis.entity.ClassWebsiteidExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ClassSectionMapper;
import com.unbank.mybatis.mapper.ClassWebsiteidMapper;

public class ClassWebsiteReader {
	public static Log logger = LogFactory.getLog(ClassWebsiteReader.class);

	public List<ClassSectionEntity> readClassWebsiteEntity() {
		List<ClassSectionEntity> classSectionEntities = new ArrayList<ClassSectionEntity>();
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			List<ClassSection> classSecitons = getClassSections(sqlSession);
			List<ClassWebsiteid> classWebsiteids = getClassWebsiteid(sqlSession);
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
				for (ClassWebsiteid classWebsiteid : classWebsiteids) {
					if (classWebsiteid.getClassid() == classSection.getId()) {
						websiteidlist.add(classWebsiteid.getWebsiteid() + "");
					}
				}
				classSectionEntity.setWebsiteList(websiteidlist);
				classSectionEntities.add(classSectionEntity);
			}
			sqlSession.commit();
		} catch (Exception e) {
			logger.info("读取ClassSection表出错", e);
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return classSectionEntities;
	}

	private List<ClassWebsiteid> getClassWebsiteid(SqlSession sqlSession) {
		ClassWebsiteidMapper classWebsiteidMapper = sqlSession
				.getMapper(ClassWebsiteidMapper.class);
		ClassWebsiteidExample example = new ClassWebsiteidExample();
		List<ClassWebsiteid> classWebsiteids = classWebsiteidMapper
				.selectByExample(example);
		return classWebsiteids;
	}

	private List<ClassSection> getClassSections(SqlSession sqlSession) {
		ClassSectionMapper classSectionMapper = sqlSession
				.getMapper(ClassSectionMapper.class);
		ClassSectionExample example = new ClassSectionExample();
		example.or().andStatusEqualTo(2);
		List<ClassSection> classSecitons = classSectionMapper
				.selectByExample(example);
		return classSecitons;
	}

}
