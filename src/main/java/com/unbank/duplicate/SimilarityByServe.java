package com.unbank.duplicate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.unbank.Constants;
import com.unbank.classify.dao.ClassWebsiteReader;
import com.unbank.classify.entity.ClassSectionEntity;
import com.unbank.duplicate.dao.ArticleCrawlIdSimilarIdReader;
import com.unbank.duplicate.dao.ArticleCrawlIdSimilarIdStore;
import com.unbank.duplicate.dao.ArticleCrawlSimilarStore;
import com.unbank.duplicate.entity.SimHashBean;
import com.unbank.exceptionCaught.ExceptionCaught;
import com.unbank.fetch.Fetcher;
import com.unbank.mina.clent.TLSClinet;
import com.unbank.mybatis.entity.ArticleCrawlIDSimilarId;
import com.unbank.mybatis.entity.ArticleCrawlSimilar;
import com.unbank.pipeline.entity.Information;

public class SimilarityByServe {
	public static Logger logger = Logger.getLogger(SimilarityByServe.class);

	public static void panchong(Information information) {
		Integer websiteId = information.getWebsite_id();
		// 是否是白名单中的就不去重了
		if (Constants.WHITELIST.contains(websiteId + "")) {
			information.setFile_index((byte) 0);
			return;
		}
		try {
			String html = isDuplicate(information);
			if (html != null) {
				if (html.equals("0")) {
					// 没有重复
					information.setFile_index((byte) 0);
				} else {
					try {
						JSONObject jsonObject = JSONObject.fromObject(html);
						SimHashBean simHashBean = (SimHashBean) JSONObject
								.toBean(jsonObject, SimHashBean.class);
						information.setSimilarid(simHashBean.getId());
//						saveArticleCrawlSimilar(information, simHashBean);
						saveSimilarityId(simHashBean.getId(),
								information.getCrawl_id());
					} catch (Exception e) {
						logger.info("保存去重信息出错", e);
					} finally {
						information.setFile_index((byte) 8);
					}
				}
			} else {
				information.setFile_index((byte) 8);
			}
		} catch (Exception e) {
			logger.info("获取是否去重信息失败", e);
			information.setFile_index((byte) 8);
		}
	}

	private static String isDuplicate(Information information) {
		String html = null;
		try {
			String url = "http://" + Constants.DUPLICATEIP
					+ "/ubk_duplicate/rest/duplicate/duplicateNews";
			Map<String, String> params = new HashMap<String, String>();
			params.put("title", Jsoup.parse(information.getCrawl_title())
					.text());
			params.put("content", Jsoup.parse(information.getText()).text());
			params.put("id", information.getCrawl_id() + "");
			html = Fetcher.getInstance().post(url, params, null, "utf-8");
		} catch (Exception e) {
			logger.info("读取去重服务出错", e);
			String errorMessage = "读取去重服务出错   " + e.getMessage();
			new ExceptionCaught().sendErrorInfo(errorMessage);
		}
		return html;
	}

	private static void saveArticleCrawlSimilar(Information information,
			SimHashBean sb1) {
		ArticleCrawlSimilar articleCrawlSimilar = new ArticleCrawlSimilar();
		List<ClassSectionEntity> classWebsiteEntities = new ClassWebsiteReader()
				.readClassWebsiteEntity();
		List<ArticleCrawlIDSimilarId> articleCrawlIDSimilarIds = new ArticleCrawlIdSimilarIdReader()
				.readArticleCrawlIdSimilarId(information.getSimilarid());
		StringBuffer classname = new StringBuffer();
		if (classWebsiteEntities != null && classWebsiteEntities.size() > 0) {
			for (ClassSectionEntity classSectionEntity : classWebsiteEntities) {
				if (classSectionEntity.getWebsiteList().contains(
						information.getWebsite_id() + "")) {
					classname.append(classSectionEntity.getClassname() + "_");
				}
			}
		}
		if (classname.length() > 0) {
			articleCrawlSimilar.setClassname(classname.toString().substring(0,
					classname.length() - 1));
		}
		articleCrawlSimilar.setCrawlId(sb1.getId());
		articleCrawlSimilar.setHotnum(articleCrawlIDSimilarIds.size() + 1);
		articleCrawlSimilar.setIstask(0);
		articleCrawlSimilar.setSimilarId(information.getCrawl_id());
		articleCrawlSimilar.setWebisteId(information.getWebsite_id());
		articleCrawlSimilar.setWebName(information.getWeb_name());
		articleCrawlSimilar.setCrawlTime(information.getCrawl_time());
		articleCrawlSimilar.setNewsTime(information.getNews_time());
		new ArticleCrawlSimilarStore()
				.saveArticleCrawlSimilar(articleCrawlSimilar);
	}

	private static void saveSimilarityId(int crawlid, int similarid) {
		try {
			ArticleCrawlIDSimilarId articleCrawlIDSimilarId = new ArticleCrawlIDSimilarId();
			articleCrawlIDSimilarId.setCrawlid(crawlid);
			articleCrawlIDSimilarId.setSimilarid(similarid);
			new ArticleCrawlIdSimilarIdStore()
					.saveArticleCrawlIdSimilarId(articleCrawlIDSimilarId);
		} catch (Exception e) {
			logger.info("保存重复ID失败", e);
		}
	}
}
