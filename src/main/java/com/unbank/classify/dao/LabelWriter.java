package com.unbank.classify.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.unbank.classify.entity.PtfDoc;
import com.unbank.mybatis.entity.SQLAdapter;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.SQLAdapterMapper;
import com.unbank.pipeline.entity.Information;

public class LabelWriter {
	public static Log logger = LogFactory.getLog(LabelWriter.class);

	public void saveLable(PtfDoc ptfDoc, Information information) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			SQLAdapterMapper sqlAdapterMapper = sqlSession
					.getMapper(SQLAdapterMapper.class);
			SQLAdapter sqlAdapter = fillpdfDocSQLAdapter(ptfDoc, information);
			sqlAdapterMapper.insertInformation(sqlAdapter);
			sqlSession.commit();
		} catch (Exception e) {
			logger.info("插入Label表出错", e);
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

	private SQLAdapter fillpdfDocSQLAdapter(PtfDoc ptfDoc,
			Information information) {
		SQLAdapter sqlAdapter = new SQLAdapter();
		String sql = "INSERT INTO  ptf_crawl_label ";
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("crawl_id", information.getCrawl_id());
		maps.put("labelName", ptfDoc.getLabelName());
		maps.put("rank", 1);
		maps.put("label_id", ptfDoc.getClassid());
		sqlAdapter.setSql(sql);
		sqlAdapter.setObj(maps);
		return sqlAdapter;
	}

}
