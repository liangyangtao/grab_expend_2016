package com.unbank.keyword.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.unbank.mybatis.entity.Sectionlabel;
import com.unbank.mybatis.entity.SectionlabelExample;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.SectionlabelMapper;

public class PlateLabelReader {

	public List<Sectionlabel> readSectionLabels() {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		List<Sectionlabel> sectionLabels = null;
		try {
			SectionlabelMapper sectionlabelMapper = sqlSession
					.getMapper(SectionlabelMapper.class);
			SectionlabelExample sectionlabelExample = new SectionlabelExample();
			sectionLabels = sectionlabelMapper
					.selectByExample(sectionlabelExample);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return sectionLabels;
	}

}
