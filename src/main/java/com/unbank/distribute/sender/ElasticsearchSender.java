package com.unbank.distribute.sender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unbank.Constants;
import com.unbank.classify.place.InformationPlaceReader;
import com.unbank.classify.qiankong.ClassifierService;
import com.unbank.classify.qiankong.LabelAndKeywords;
import com.unbank.fetch.Fetcher;
import com.unbank.pipeline.entity.Information;

public class ElasticsearchSender extends BaseInfoSender {
	public static Log logger = LogFactory.getLog(ElasticsearchSender.class);

	public boolean sendInfo(Information information) {
		boolean istrue = true;
		try {

			// 根据新的算法得到的标签2015年12月
			LabelAndKeywords labelAndKeywords = new ClassifierService()
					.getBankTagNewssOpen(information.getCrawl_title(),
							information.getText());
			// 关键词开始
			String keyword[] = labelAndKeywords.getKeywords();
			StringBuffer keywordBuffer = new StringBuffer();
			for (String string : keyword) {
				try {
					keywordBuffer.append(string.trim() + "/");
				} catch (Exception e) {
					logger.info("保存关键词出错", e);
					continue;
				}

			}

			information.setKeywords(keywordBuffer.substring(0,
					keywordBuffer.length() - 1));
			// 关键词结束
			// 标签开始
//			String label = labelAndKeywords.getLabel();
			// 根据图数据库获取的标签关系作为真正的标签
			// 去掉这一步
			// 不要原来赵健 的分类的标签了
//			String tagName = fillTagNames(label, information.getClassname());
			String tagName =labelAndKeywords.getLabel();
			// 去掉这一步

			Map<String, String> params = fillParams(information, tagName);
			if (Constants.ISSENDTOES == 1) {

				istrue = SendToNeiEs(information, params);
			}
			if (Constants.ISSENDTOREMOTEES == 1) {

				istrue = SendToRemoteEs(information, params);
			}
		} catch (Exception e) {
			logger.info("发给检索服务器失败   " + information.getCrawl_id(), e);
			istrue = false;
		}
		return istrue;

	}

	private Map<String, String> fillParams(Information information,
			String tagName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("crawl_id", information.getCrawl_id() + "");
		params.put("title", information.getCrawl_title());
		params.put("url", information.getUrl());
		params.put("content", information.getText());
		params.put("newsDate", information.getNews_time().getTime() + "");
		params.put("crawlDate", information.getCrawl_time().getTime() + "");
		params.put("webName", information.getWeb_name());
		params.put("webSectionName", information.getSectionName());
		params.put("website_id", information.getWebsite_id() + "");
		params.put("p_url", information.getWeb_url());
		params.put("tagName", tagName);
		params.put("keyWords", information.getKeywords());
		// 增加内容图片
		Document document = Jsoup.parse(information.getText());
		String imgs = getTextImage(document);
		params.put("picUrl", imgs);
		// 用 逗号 隔开
		// 增加内容图片jieshu
		// 增加place
		String place = getInfoPlace(information.getCrawl_title(),
				document.text());
		params.put("region", place);
		// 用 空格隔开
		return params;
	}

	private String getInfoPlace(String crawl_title, String text) {

		return new InformationPlaceReader().getPlace(crawl_title + text);
	}

	private String getTextImage(Document document) {
		Elements imgElements = document.select("img");
		StringBuffer stringBuffer = new StringBuffer();
		for (Element element : imgElements) {
			String imgurl = element.attr("src");
			if (imgurl.isEmpty()) {
				continue;
			}
			stringBuffer.append(imgurl + ",");
		}
		String imgUrls = null;
		if (stringBuffer.length() > 1) {
			imgUrls = stringBuffer.substring(0, stringBuffer.length() - 1);
		} else {
			imgUrls = "";
		}
		return imgUrls;
	}

	private static boolean SendToNeiEs(Information information,
			Map<String, String> params) {
		boolean istrue = false;
		if (Constants.SEACHERIP.isEmpty()) {
			return istrue;
		}
		List<String> urls = Arrays.asList(Constants.SEACHERIP.split(","));
		for (String ip : urls) {
			String url = "http://" + ip + "/SearchPlatform/rest/index/oper";
			try {
				String html = Fetcher.getInstance().post(url, params, null,
						"utf-8");
				if (html.equals(information.getCrawl_id() + "")) {
					logger.info("发给内网检索服务器成功     " + ip + "  "
							+ information.getCrawl_id());
					istrue = true;
				} else {
					logger.info("发给内网检索服务器失败     " + ip + "   "
							+ information.getCrawl_id());
					istrue = false;
				}
			} catch (Exception e) {
				istrue = false;
				logger.info("发给内网检索服务器失败     " + information.getCrawl_id(), e);
				continue;
			}
		}

		return istrue;
	}

	private boolean SendToRemoteEs(Information information,
			Map<String, String> params) {
		boolean istrue = false;
		if (Constants.SEACHERIP_REMOTE.isEmpty()) {
			return istrue;
		}
		List<String> urls = Arrays
				.asList(Constants.SEACHERIP_REMOTE.split(","));
		for (String ip : urls) {
			String url = "http://" + ip + "/SearchPlatform/rest/index/oper";
			try {
				String html = Fetcher.getInstance().post(url, params, null,
						"utf-8");
				if (html != null && html.equals(information.getCrawl_id() + "")) {
					logger.info("发给外网检索服务器成功     " + ip + "     "
							+ information.getCrawl_id());
					istrue = true;
				} else {
					logger.info("发给外网检索服务器失败     " + ip + "    "
							+ information.getCrawl_id());
					istrue = false;
				}
			} catch (Exception e) {
				istrue = false;
				logger.info("发给外网检索服务器失败     " + information.getCrawl_id(), e);
				// String errorMessage = "发给外网检索服务器失败     "
				// + information.getCrawl_id() + "  " + e.getMessage();
				// new ExceptionCaught().reportExceptByUrlAndParams(params, url,
				// errorMessage);
				continue;
			}
		}
		return istrue;
	}

	private String fillTagNames(String label, String classname) {

		Set<String> temp = new HashSet<String>();
		temp.add(label);
		// 把标签也放到 temp 里
		if (classname != null) {
			String tagtemp[] = classname.split("_");
			for (String string : tagtemp) {
				if (string.isEmpty()) {
					continue;
				} else {
					if (string.contains("1")) {
						string = string.replace("1", "");
					}
				}
				temp.add(string);
			}
		}
		StringBuffer tag = new StringBuffer();
		for (String string : temp) {
			tag.append(string + " ");
		}
		if (tag.length() > 1) {
			return tag.toString().trim();
		} else {
			return "";
		}
	}

//	public static void main(String[] args) {
//		// LabelAndKeywords{label='世界经济', keywords=[经济, 政府, 投资, 世界, 人民, 浮动, 成本,
//		// 困境, 贷款, 货币, 支付, 消费, 公务员, 研究, 盈余, 财政, 援助, 道路]}
//		Constants.init();
//		String label = "世界经济";
//		String keyword = "经济/政府/投资/世界/人民/浮动/成本/困境/贷款/货币/支付/消费/公务员/研究/盈余/财政/援助/道路";
//		System.out.println(new LabelRelationshipReader().getTags(label, "",
//				keyword));
//	}

}
