package com.unbank.distribute.sender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unbank.classify.dao.ClassWebsiteReader;
import com.unbank.classify.entity.ClassSectionEntity;
import com.unbank.classify.entity.PtfDoc;
import com.unbank.distribute.dao.ClassNoNeedKeywordReader;
import com.unbank.distribute.entity.NoNeedKeywordEntity;
import com.unbank.distribute.entity.Platform;
import com.unbank.distribute.fill.PlatformFiller;
import com.unbank.distribute.filter.DistributeFilter;
import com.unbank.duplicate.dao.ArticleCrawlIdSimilarIdReader;
import com.unbank.mybatis.dao.InformationReader;
import com.unbank.mybatis.dao.InformationWriter;
import com.unbank.mybatis.dao.intell.PdfDocWriter;
import com.unbank.mybatis.entity.ArticleCrawlIDSimilarId;
import com.unbank.pipeline.entity.Information;

public class InformationSender {
	private static Log logger = LogFactory.getLog(InformationSender.class);

	public void sendInformation(Information information) {

		Map<String, Object> informationMap = fillInformation(information);
		List<Platform> platforms = new PlatformFiller().parsePlatformData();
		for (Platform platform : platforms) {
			try {
				insertInformation(informationMap, platform, information);
			} catch (Exception e) {
				logger.info("", e);
				logger.info(information.getCrawl_id() + "   在    "
						+ platform.getPlatformId() + "    平台同步失败");
				continue;
			}
		}
		platforms.clear();
		informationMap.clear();

	}

	private void insertInformation(Map<String, Object> informationMap,
			Platform platform, Information information) {

		DistributeFilter distributeFilter = new DistributeFilter();
		boolean ispass = distributeFilter.checkInformation(platform,
				information);
		if (!ispass) {
			return;
		}
		// 在此判断环境，发送图片
		String environment = platform.getPlatformId();
		if (environment.equals("ubkview")) {
			sendImage(informationMap, platform);
		}
		// 在此判断环境，发送图片结束
		Iterator<String> iterator = platform.getFields().keySet().iterator();
		Map<String, Map<String, Object>> tables = new HashMap<String, Map<String, Object>>();
		boolean isNotIntell = true;
		while (iterator.hasNext()) {
			String tableName = iterator.next();
			if (tableName.equals("intell_pdf_doc")) {
				isNotIntell = false;
				if (information.getObject() != null) {
					List<PtfDoc> ptfdocs = (List<PtfDoc>) information
							.getObject();
					if (ptfdocs.size() > 1) {
						Map<String, PtfDoc> docmap = new HashMap<String, PtfDoc>();

						for (PtfDoc ptfDoc : ptfdocs) {
							String templateid = ptfDoc.getTemplateId() + "";
							docmap.put(templateid, ptfDoc);
						}
						ptfdocs.clear();
						ptfdocs = new ArrayList<PtfDoc>(docmap.values());

						senToPdfDoc(information, environment, ptfdocs);

					} else {
						senToPdfDoc(information, environment, ptfdocs);
					}

				}
				continue;
			}
			StringBuffer tableSql = new StringBuffer();
			Map<String, Object> objects = new HashMap<String, Object>();
			tableSql.append("insert into " + tableName);
			Map<String, String> fields = platform.getFields().get(tableName);
			Iterator<String> tableiteIterator = fields.keySet().iterator();
			while (tableiteIterator.hasNext()) {
				String fieldName = tableiteIterator.next();
				String fieldValue = fields.get(fieldName);
				if (fieldValue.equals("crawl_id")) {
					environment = environment + "@@@" + fieldName;
					continue;
				}
				objects.put(fieldName, informationMap.get(fieldValue));
			}
			tables.put(tableSql.toString(), objects);
		}
		if (isNotIntell) {
			new InformationWriter().insertInformation(tables, environment);
		}

		tables.clear();
	}

	private void senToPdfDoc(Information information, String environment,
			List<PtfDoc> ptfdocs) {

		List<NoNeedKeywordEntity> noNeedKeywordEntities = new ClassNoNeedKeywordReader()
				.readerNoNeedKeywordEntity();
		for (PtfDoc ptfDoc : ptfdocs) {
			// 添加关键词过滤功能
			int id = ptfDoc.getClassid();
			boolean isexit = false;
			for (NoNeedKeywordEntity noNeedKeywordEntity : noNeedKeywordEntities) {
				// 如果id
				if (noNeedKeywordEntity.getClassid() == id) {
					isexit = true;
					List<String> temp = Arrays.asList(noNeedKeywordEntity
							.getKeywordList().split(","));
					for (String string : temp) {
						if (information.getCrawl_title()
								.contains(string.trim())) {
							new PdfDocWriter().savePdfDoc(ptfDoc, information,
									environment);
							break;

						}
					}

				}
			}
			// 如果不是过滤的就发送
			if (!isexit) {
				new PdfDocWriter().savePdfDoc(ptfDoc, information, environment);
			}

		}
	}

	private void sendImage(Map<String, Object> informationMap, Platform platform) {
		String text = (String) informationMap.get("text");
		Document document = Jsoup.parse(text);
		Elements imageElements = document.select("img");
		for (Element element : imageElements) {
			try {
				String imageUrl = element.attr("src");
				String newUrl = new ImageSender().senderImage(imageUrl,
						platform);
				if (newUrl != null) {
					element.attr("src", newUrl);
				} else {
					element.remove();
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		String nowtext = document.body().toString();
		nowtext = nowtext.replaceAll(">\\s{0,10}", ">");
		nowtext = nowtext.replaceAll(">\\s{0,10}(&nbsp; ){0,}", ">");
		nowtext = nowtext.replaceAll(">\\s{0,10}(&nbsp;){0,}", ">");
		nowtext = nowtext.replaceAll(">\\s{0,10} {0,}", ">");
		nowtext = nowtext.replaceAll(">\\s{0,10}  {0,}", ">");
		nowtext = nowtext.replaceAll(">\\s{0,10}", ">");
		nowtext = nowtext.replaceAll("\\s{0,10}<", "<");
		nowtext = nowtext.replace("<body>", "");
		nowtext = nowtext.replace("</body>", "");
		informationMap.put("text", nowtext);

	}

	private Map<String, Object> fillInformation(Information information) {
		Map<String, Object> informationMap = new HashMap<String, Object>();
		informationMap.put("crawl_brief", information.getCrawl_brief());
		informationMap.put("crawl_id", information.getCrawl_id());
		informationMap.put("crawl_title", information.getCrawl_title());
		informationMap.put("crawl_views", information.getCrawl_views());
		informationMap.put("crawl_time", information.getCrawl_time());
		informationMap.put("file_index", information.getFile_index());
		informationMap.put("news_time", information.getNews_time());
		informationMap.put("task", information.getTask());
		informationMap.put("text", information.getText());
		informationMap.put("url", information.getUrl());
		informationMap.put("web_name", information.getWeb_name());
		informationMap.put("website_id", information.getWebsite_id());
		informationMap.put("className", information.getObject());
		return informationMap;
	}

	public void sendInformationByUniq(Information information) {
		List<ClassSectionEntity> classWebsiteEntities = new ClassWebsiteReader()
				.readClassWebsiteEntity();
		List<Integer> myClassids = new ArrayList<Integer>();
		List<Integer> classids = new ArrayList<Integer>();
		List<Integer> crawlids = new ArrayList<Integer>();
		List<ArticleCrawlIDSimilarId> articleCrawlIDSimilarIds = new ArticleCrawlIdSimilarIdReader()
				.readArticleCrawlIdSimilarId(information.getSimilarid());
		crawlids.add(information.getWebsite_id());
		// 如果不是制定的网址，就没有分类
		// 20896188
		// 20910713
		// 20914149

		for (ArticleCrawlIDSimilarId articleCrawlIDSimilarId : articleCrawlIDSimilarIds) {
			Integer websiteid = new InformationReader()
					.readInformationByCrawlid(
							articleCrawlIDSimilarId.getSimilarid())
					.getWebsite_id();
			crawlids.add(websiteid);
		}
		if (classWebsiteEntities != null && classWebsiteEntities.size() > 0) {
			for (ClassSectionEntity classSectionEntity : classWebsiteEntities) {
				for (Integer crawlid : crawlids) {
					if (classSectionEntity.getWebsiteList().contains(
							crawlid + "")) {
						if (!classids.contains(classSectionEntity.getId())) {
							classids.add(classSectionEntity.getId());
						}
					}
				}
				if (classSectionEntity.getWebsiteList().contains(
						information.getWebsite_id() + "")) {
					// 如果重复的并且在我们客户的名单里
					if (!myClassids.contains(classSectionEntity.getId())) {
						myClassids.add(classSectionEntity.getId());
					}

				}
			}
		}
		//
		// 21 he 23
		List<PtfDoc> ptfdocs = new ArrayList<PtfDoc>();
		// 获取到已有的 客户id列表 ，这篇新闻的新闻列表
		for (Integer classid : myClassids) {
			if (classids.contains(classid)) {

			} else {
				logger.info("发现重复的制定网址" + information.getCrawl_id()
						+ "        " + information.getWebsite_id());
				for (ClassSectionEntity classSectionEntity : classWebsiteEntities) {
					if (classSectionEntity.getId() == classid) {
						logger.info(information.getCrawl_id() + " 分到了   "
								+ classSectionEntity.getClassname());
						PtfDoc ptfDoc = new PtfDoc();
						ptfDoc.setClassid(classSectionEntity.getId());
						ptfDoc.setLabelName(classSectionEntity.getClassname());
						ptfDoc.setStrucutureId(classSectionEntity
								.getStrucutureid());
						ptfDoc.setTemplateId(classSectionEntity.getTemplateid());
						ptfDoc.setForumId(classSectionEntity.getForumid());
						ptfdocs.add(ptfDoc);
					}
				}
			}

		}
		if (ptfdocs.size() <= 0) {
			return;
		}
		Information information2 = new InformationReader()
				.readInformationByCrawlid(information.getSimilarid());
		information2.setObject(ptfdocs);
		List<Platform> platforms = new PlatformFiller().parsePlatformData();
		for (Platform platform : platforms) {
			String environment = platform.getPlatformId();
			if (environment.contains("intell")) {
				Iterator<String> iterator = platform.getFields().keySet()
						.iterator();
				while (iterator.hasNext()) {
					String tableName = iterator.next();
					if (tableName.equals("intell_pdf_doc")) {
						if (information2.getObject() != null) {
							if (ptfdocs.size() > 1) {
								Map<String, PtfDoc> docmap = new HashMap<String, PtfDoc>();
								for (PtfDoc ptfDoc : ptfdocs) {
									String templateid = ptfDoc.getTemplateId()
											+ "";
									docmap.put(templateid, ptfDoc);
								}
								ptfdocs.clear();
								ptfdocs = new ArrayList<PtfDoc>(docmap.values());
								for (PtfDoc ptfDoc : ptfdocs) {
									new PdfDocWriter().savePdfDoc(ptfDoc,
											information2, environment);
								}
							} else {
								for (PtfDoc ptfDoc : ptfdocs) {
									new PdfDocWriter().savePdfDoc(ptfDoc,
											information2, environment);
								}
							}

						}
						break;
					}
				}
				break;
			}
		}
	}
}
