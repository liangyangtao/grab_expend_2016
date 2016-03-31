package com.unbank.keyword;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;

import com.hankcs.textrank.TextRankKeyword;
import com.unbank.Constants;
import com.unbank.exceptionCaught.ExceptionCaught;
import com.unbank.fetch.Fetcher;
import com.unbank.keyword.dao.ArticleInfoSecitonReader;
import com.unbank.keyword.dao.ArticleKeywordNumStore;
import com.unbank.keyword.dao.ArticleKeywordStore;
import com.unbank.keyword.dao.ArticleLocationStore;
import com.unbank.keyword.dao.ArticleOrganizationStore;
import com.unbank.keyword.dao.ArticlePersonStore;
import com.unbank.keyword.dao.ArticlePlateKeywordStore;
import com.unbank.keyword.dao.ArticleTextRankKeywordStore;
import com.unbank.keyword.dao.PlateLabelReader;
import com.unbank.mybatis.entity.ArticleKeyword;
import com.unbank.mybatis.entity.ArticleKeywordNum;
import com.unbank.mybatis.entity.ArticleLocation;
import com.unbank.mybatis.entity.ArticleOrganization;
import com.unbank.mybatis.entity.ArticlePerson;
import com.unbank.mybatis.entity.ArticlePlateKeyword;
import com.unbank.mybatis.entity.ArticleTextRankKeyword;
import com.unbank.mybatis.entity.Sectionlabel;
import com.unbank.pipeline.entity.Information;

public class InformationKeywordExtractor {
	private static Log logger = LogFactory
			.getLog(InformationKeywordExtractor.class);

	public static void extractProperties(Information information) {
		// 调取远程服务进行关键词提取
		List<String> keywrod = getKeywordByServe(information);
		StringBuffer keywordBuffer = new StringBuffer();
		if (keywrod == null) {
			information.setKeywords("");
		} else {
			for (String string : keywrod) {
				try {
					// ArticleTextRankKeyword articleTextRankKeyword = new
					// ArticleTextRankKeyword();
					// articleTextRankKeyword.setCrawlId(information.getCrawl_id());
					keywordBuffer.append(string + "/");
					// articleTextRankKeyword.setKeyword(string);
					// articleTextRankKeyword.setScore(1.0f);
					// new ArticleTextRankKeywordStore()
					// .saveArticleTextRankKeyword(articleTextRankKeyword);
				} catch (Exception e) {
					logger.info("保存关键词出错", e);
					continue;
				}

			}
			information.setKeywords(keywordBuffer.substring(0,
					keywordBuffer.length() - 1));
			logger.info("ID=" + information.getCrawl_id()
					+ "===========关键词 ========" + information.getKeywords());
		}
	}

	private static List<String> getKeywordByServe(Information information) {
		List<String> temp = null;
		try {
			String title = information.getCrawl_title();
			String content = information.getText();
			String url = "http://" + Constants.KEYWORDIP
					+ "/ubk_duplicate/rest/keyword/getKeyword";
			Map<String, String> params = new HashMap<String, String>();
			params.put("title", Jsoup.parse(title).text());
			params.put("content", Jsoup.parse(content).text());
			String html = Fetcher.getInstance()
					.post(url, params, null, "utf-8");
			JSONArray jsonArray = JSONArray.fromObject(html);
			temp = JSONArray.toList(jsonArray);
		} catch (Exception e) {
			temp = null;
			logger.info("根据服务器提取关键词出错", e);
			String errorMessage = "根据服务器提取关键词出错" + e.getMessage();
			new ExceptionCaught().sendErrorInfo(errorMessage);
		}
		return temp;
	}

	private static void savePlateKeyword(Information information) {
		int websiteId = information.getWebsite_id();
		String sectionName = new ArticleInfoSecitonReader()
				.readSectionById(websiteId);
		List<Sectionlabel> sectionlabels = new PlateLabelReader()
				.readSectionLabels();
		for (Sectionlabel sectionlabel : sectionlabels) {
			try {
				if (sectionName.contains(sectionlabel.getLabelname().trim())) {
					ArticlePlateKeyword articlePlateKeyword = new ArticlePlateKeyword();
					articlePlateKeyword.setCrawlId(information.getCrawl_id());
					articlePlateKeyword.setKeyword(sectionlabel.getLabelname()
							.trim());
					articlePlateKeyword.setPlateId(sectionlabel.getId());
					new ArticlePlateKeywordStore()
							.saveArticlePlateKeyword(articlePlateKeyword);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

	}

	private static void savePageRankKeyword(Information information) {
		String title = information.getCrawl_title();
		String content = information.getText();
		String keywordJson = TextRankKeyword.getInstance().getKyewordByFd(
				Jsoup.parse(title).text(), Jsoup.parse(content).text());
		JSONArray hankcsKeywordJsonArray = new JSONArray()
				.fromObject(keywordJson);
		StringBuffer keywordBuffer = new StringBuffer();
		for (Object object : hankcsKeywordJsonArray) {
			try {
				JSONObject JSONObject = (JSONObject) object;
				ArticleTextRankKeyword articleTextRankKeyword = new ArticleTextRankKeyword();
				articleTextRankKeyword.setCrawlId(information.getCrawl_id());
				String keyword = (String) JSONObject.get("keyword");
				keywordBuffer.append(keyword + "/");
				// new UnbankKeywordChecker().checkKeyword(keyword.trim());
				articleTextRankKeyword.setKeyword(keyword);
				articleTextRankKeyword.setScore(Float.parseFloat(JSONObject
						.get("score") + "f"));
				new ArticleTextRankKeywordStore()
						.saveArticleTextRankKeyword(articleTextRankKeyword);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		information.setKeywords(keywordBuffer.substring(0,
				keywordBuffer.length() - 1));
		logger.info("            " + information.getCrawl_id()
				+ "===========关键词 ========" + information.getKeywords());
	}

	private static JSONObject getJsonobject(Information information) {
		String title = information.getCrawl_title();
		String content = information.getText();
		String url = "http://" + com.unbank.Constants.KEYWORDIP
				+ "/keywords/extract-properties.htm";
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", title);
		params.put("content", content);
		String html = Fetcher.getInstance().post(url, params, null, "utf-8");
		JSONObject json = JSONObject.fromObject(html);
		return json;
	}

	private static void saveOrganizations(Information information,
			JSONObject json) {
		JSONArray organizationsJsonArray = json.getJSONArray("organizations");
		for (Object object : organizationsJsonArray) {
			try {
				String organization = (String) object;
				if (organization.trim().isEmpty()) {
					continue;
				}
				ArticleOrganization articleOrganization = new ArticleOrganization();
				articleOrganization.setCrawlId(information.getCrawl_id());
				articleOrganization.setOrganizationName(organization.trim());
				ArticleOrganizationStore articleOrganizationStore = new ArticleOrganizationStore();
				articleOrganizationStore
						.saveArticleOrganizationStore(articleOrganization);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	private static void saveLocations(Information information, JSONObject json) {
		JSONArray locationsJsonArray = json.getJSONArray("locations");
		for (Object object : locationsJsonArray) {
			try {
				String location = (String) object;
				if (location.trim().isEmpty()) {
					continue;
				}
				ArticleLocation articleLocation = new ArticleLocation();
				articleLocation.setCrawlId(information.getCrawl_id());
				articleLocation.setLocation(location.trim());
				ArticleLocationStore articleLocationStore = new ArticleLocationStore();
				articleLocationStore.saveArticleLocation(articleLocation);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
	}

	private static void savePersons(Information information, JSONObject json) {
		JSONArray personsJsonArray = json.getJSONArray("persons");
		for (Object object : personsJsonArray) {
			try {
				String person = (String) object;
				if (person.trim().isEmpty()) {
					continue;
				}
				ArticlePerson articlePerson = new ArticlePerson();
				articlePerson.setCrawlId(information.getCrawl_id());
				articlePerson.setPersonname(person.trim());
				ArticlePersonStore articlePersonStore = new ArticlePersonStore();
				articlePersonStore.saveArticlePerson(articlePerson);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
	}

	private static void saveKeywords(Information information, JSONObject json) {
		JSONArray keywordJsonArray = json.getJSONArray("keywords");
		for (Object object : keywordJsonArray) {
			try {
				String keyword = (String) object;
				if (keyword.trim().isEmpty()) {
					continue;
				}
				ArticleKeyword articleKeyword = new ArticleKeyword();
				articleKeyword.setCrawlId(information.getCrawl_id());
				articleKeyword.setCrawlTime(information.getCrawl_time());
				articleKeyword.setKeyword(keyword.trim());
				ArticleKeywordStore articleKeywordStore = new ArticleKeywordStore();
				articleKeywordStore.saveArticleKeyword(articleKeyword);
				ArticleKeywordNum articleKeywordNum = new ArticleKeywordNum();
				articleKeywordNum.setKeyword(keyword.trim());
				ArticleKeywordNumStore articleKeywordNumStore = new ArticleKeywordNumStore();
				articleKeywordNumStore.saveArticleKeywordNum(articleKeywordNum);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
	}

	public static void extractKeyword(Information information) {
		String html = getHtml(information);
		saveKeywods(information, html);

	}

	private static String getHtml(Information information) {
		String title = information.getCrawl_title();
		String content = information.getText();
		String url = "http://" + com.unbank.Constants.KEYWORDIP
				+ "/keywords/extract.htm";
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", title);
		params.put("content", content);
		String html = Fetcher.getInstance().post(url, params, null, "utf-8");
		return html;
	}

	private static void saveKeywods(Information information, String html) {
		JSONArray jsonArray = JSONArray.fromObject(html);

		for (Object object : jsonArray) {
			try {
				String keyword = (String) object;
				if (keyword.trim().isEmpty()) {
					continue;
				}
				ArticleKeyword articleKeyword = new ArticleKeyword();
				articleKeyword.setCrawlId(information.getCrawl_id());
				articleKeyword.setCrawlTime(information.getCrawl_time());
				articleKeyword.setKeyword(keyword.trim());
				ArticleKeywordStore articleKeywordStore = new ArticleKeywordStore();
				articleKeywordStore.saveArticleKeyword(articleKeyword);
				ArticleKeywordNum articleKeywordNum = new ArticleKeywordNum();
				articleKeywordNum.setKeyword(keyword.trim());
				ArticleKeywordNumStore articleKeywordNumStore = new ArticleKeywordNumStore();
				articleKeywordNumStore.saveArticleKeywordNum(articleKeywordNum);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
	}

	// public void sendInformationId(Information information) {
	// try {
	// String url = "http://" + com.unbank.Constants.TUSHUJUIP
	// + "/keywords-1.0/article-properties.htm?docid="
	// + information.getCrawl_id();
	// String html = fetcher.get(url);
	// System.out.println(information.getCrawl_id() + "==send====" + html);
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.info("发送给邹经理Id异常" + e);
	// }
	// }

}
