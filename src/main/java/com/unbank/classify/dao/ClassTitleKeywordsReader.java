package com.unbank.classify.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.classify.entity.ClassSectionEntity;
import com.unbank.mybatis.entity.ClassKeyword;
import com.unbank.mybatis.entity.ClassKeywordExample;
import com.unbank.mybatis.entity.ClassSection;
import com.unbank.mybatis.entity.ClassSectionExample;
import com.unbank.mybatis.entity.ClassTitleKeyword;
import com.unbank.mybatis.entity.ClassTitleKeywordExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ClassKeywordMapper;
import com.unbank.mybatis.mapper.ClassSectionMapper;
import com.unbank.mybatis.mapper.ClassTitleKeywordMapper;

public class ClassTitleKeywordsReader {

	public static Log logger = LogFactory.getLog(ClassSectionReader.class);

	public List<ClassSectionEntity> readClassTitleKeywordsEntity() {
		List<ClassSectionEntity> classSectionEntities = new ArrayList<ClassSectionEntity>();
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			List<ClassSection> classSecitons = getClassSections(sqlSession);
			List<ClassTitleKeyword> classWebsiteids = getClassKeyword(sqlSession);
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
				for (ClassTitleKeyword classWebsiteid : classWebsiteids) {
					if (classWebsiteid.getClassid() == classSection.getId()) {
						websiteidlist.add(classWebsiteid.getKeyword());
					}
				}
				classSectionEntity.setWebsiteList(websiteidlist);
				classSectionEntities.add(classSectionEntity);
			}

			sqlSession.commit();
		} catch (Exception e) {
			logger.info("读取ClassKeyword表出错", e);
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return classSectionEntities;

	}

	private List<ClassTitleKeyword> getClassKeyword(SqlSession sqlSession) {
		ClassTitleKeywordMapper classKeywordMapper = sqlSession
				.getMapper(ClassTitleKeywordMapper.class);
		ClassTitleKeywordExample example = new ClassTitleKeywordExample();
		List<ClassTitleKeyword> classkeywordEntities = classKeywordMapper
				.selectByExample(example);
		return classkeywordEntities;
	}

	private List<ClassSection> getClassSections(SqlSession sqlSession) {
		ClassSectionMapper classSectionMapper = sqlSession
				.getMapper(ClassSectionMapper.class);
		ClassSectionExample example = new ClassSectionExample();
		example.or().andStatusEqualTo(4);
		List<ClassSection> classSecitons = classSectionMapper
				.selectByExample(example);
		return classSecitons;
	}
}
