package com.unbank.mybatis.dao.intell;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.unbank.Constants;
import com.unbank.classify.entity.PtfDoc;
import com.unbank.mybatis.entity.ArticleCrawlIdToDocId;
import com.unbank.mybatis.entity.SQLAdapter;
import com.unbank.mybatis.factory.DynamicConnectionFactory;
import com.unbank.mybatis.mapper.ArticleCrawlIdToDocIdMapper;
import com.unbank.mybatis.mapper.SQLAdapterMapper;
import com.unbank.mybatis.maxid.MaxIdFinder;
import com.unbank.pipeline.entity.Information;

public class PdfDocWriter extends MaxIdFinder {

	public void savePdfDoc(PtfDoc ptfDoc, Information information,
			String environment) {
		String temp[] = environment.split("@@@");
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory(temp[0]).openSession();

		/***
		 * 如果时间是一点半到2点不用同步
		 * 
		 */
		Date date = new Date();
		int hours = date.getHours();
		int minutes = date.getMinutes();
		if (hours == 13 && minutes >= 15 && minutes <= 30) {
			return;
		}
		/***
		 * 同步的两种方法
		 */

		if (Constants.ISBYZIZENG == 1) {
			/***
			 * 方法二
			 * 
			 * 自增获取ptf_doc 的id
			 */
			savePtfDocByZizeng(ptfDoc, information, sqlSession);
			/**
			 * 方法二结束
			 */
		} else {
			/**
			 * 方法一：
			 * 
			 * 通过刘经理的返回id保存
			 */
			savePtfdocByRetuenId(ptfDoc, information, temp, sqlSession);

			/**
			 * 方法一结束
			 */
		}

	}

	private void savePtfdocByRetuenId(PtfDoc ptfDoc, Information information,
			String[] temp, SqlSession sqlSession) {
		int docid = (int) findMaxId("doc_id", "ptf_doc", temp[0]) + 1;
		ptfDoc.setDocId(docid);
		try {

			SQLAdapterMapper sqlAdapterMapper = sqlSession
					.getMapper(SQLAdapterMapper.class);
			SQLAdapter sqlAdapter = fillpdfDocSQLAdapter(ptfDoc, information);
			sqlAdapterMapper.insertPdfDoc(sqlAdapter);
			if (ptfDoc.getDocId() > 0) {
				sqlAdapter = fillpdfDocTextSQLAdapter(ptfDoc.getDocId(),
						information);
				sqlAdapterMapper.insertPdfDocText(sqlAdapter);
			}
			sqlSession.commit();
		} catch (Exception e) {
			logger.info("插入智能编辑平台doc表出错", e);
			e.printStackTrace();
			sqlSession.rollback();
		} finally {

			sqlSession.close();
		}
	}

	private void savePtfDocByZizeng(PtfDoc ptfDoc, Information information,
			SqlSession sqlSession) {
		try {

			SQLAdapterMapper sqlAdapterMapper = sqlSession
					.getMapper(SQLAdapterMapper.class);
			SQLAdapter sqlAdapter = fillpdfDocSQLAdapter(ptfDoc, information);
			sqlAdapterMapper.insertPdfDoc(sqlAdapter);
			int docid = sqlAdapter.getPrikey();
			// System.out.println("智能编辑平台返回Id 是" + docid);
			ptfDoc.setDocId(docid);
			if (ptfDoc.getDocId() > 0) {
				sqlAdapter = fillpdfDocTextSQLAdapter(ptfDoc.getDocId(),
						information);
				sqlAdapterMapper.insertPdfDocText(sqlAdapter);
			}
			sqlSession.commit();
			// 保存docid 和crawlid

		} catch (Exception e) {
			logger.info("插入智能编辑平台doc表出错", e);
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {

			sqlSession.close();
		}
	}

	private void saveCrawlIdAndDocid(int docid, int crawlid) {
		SqlSession sqlSession = DynamicConnectionFactory
				.getInstanceSessionFactory("development").openSession();
		try {
			ArticleCrawlIdToDocId articleTextRankKeyword = new ArticleCrawlIdToDocId();
			articleTextRankKeyword.setCrawlId(crawlid);
			articleTextRankKeyword.setDocId(docid);
			ArticleCrawlIdToDocIdMapper articleCrawlIdToDocIdMapper = sqlSession
					.getMapper(ArticleCrawlIdToDocIdMapper.class);
			articleCrawlIdToDocIdMapper.insertSelective(articleTextRankKeyword);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

	/***
	 * 保存到ptf_docText 表的
	 * 
	 * @param ptfDoc
	 * @param information
	 * @return
	 */
	private SQLAdapter fillpdfDocTextSQLAdapter(int docid,
			Information information) {
		SQLAdapter sqlAdapter = new SQLAdapter();
		String sql = " INSERT INTO  ptf_doc_text";
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("doc_id", docid);
		maps.put("text", information.getText());
		sqlAdapter.setSql(sql);
		sqlAdapter.setObj(maps);
		return sqlAdapter;
	}

	/***
	 * 保存到ptf_doc表的
	 * 
	 * @param ptfDoc
	 * @param information
	 * @return
	 */
	private SQLAdapter fillpdfDocSQLAdapter(PtfDoc ptfDoc,
			Information information) {
		SQLAdapter sqlAdapter = new SQLAdapter();
		String sql = "INSERT INTO  ptf_doc";
		Map<String, Object> maps = new HashMap<String, Object>();

		// 此处进行判断 如果是刘经理返回id的话
		if (ptfDoc.getDocId() > 0) {
			maps.put("doc_id", ptfDoc.getDocId());
		} else {
			// 如果是自增的话，doc_id 就有自增的到
		}

		maps.put("forum_id", ptfDoc.getForumId());
		maps.put("template_id", ptfDoc.getTemplateId());
		maps.put("structure_id", ptfDoc.getStrucutureId());
		maps.put("doc_type", "PASSED");
		maps.put("doc_title", information.getCrawl_title());
		maps.put("doc_brief", information.getCrawl_brief());
		maps.put("doc_views", 0);
		maps.put("doc_edit_time", new Date());
		maps.put("display_order", 0);
		maps.put("deny", 0);
		maps.put("file_index", 0);
		maps.put("c_id", 1);
		maps.put("c_name", "系统");
		maps.put("c_date", new Date());
		maps.put("c_ip", "12711");
		maps.put("flag", 0);
		maps.put("flag_ubk", 0);
		maps.put("flag_banker", 2);
		maps.put("web_name", information.getWeb_name());
		maps.put("url", information.getUrl());
		sqlAdapter.setSql(sql);
		sqlAdapter.setObj(maps);
		return sqlAdapter;
	}

}
