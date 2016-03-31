package com.unbank.pipeline.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.unbank.Constants;
import com.unbank.classify.LabelRelationshipReader;
import com.unbank.classify.dao.ClassWebsiteReader;
import com.unbank.classify.entity.ClassSectionEntity;
import com.unbank.duplicate.dao.ArticleCrawlSimilarModifier;
import com.unbank.mina.clent.TLSClinet;
import com.unbank.mybatis.entity.ArticleCrawlSimilar;
import com.unbank.pipeline.builder.DistributeBuilder;

public class UniquInformationConsume extends BaseQueue implements Runnable {

	protected LinkedBlockingQueue<Object> informationQueue;

	public UniquInformationConsume(LinkedBlockingQueue<Object> informationQueue) {
		this.informationQueue = informationQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (informationQueue.size() <= 10000
						&& informationQueue.size() > 0) {
					ArticleCrawlSimilar articleCrawlSimilar = null;
					articleCrawlSimilar = (ArticleCrawlSimilar) take(informationQueue);
					if (articleCrawlSimilar != null) {
						consumeInformation(articleCrawlSimilar);
					}
				}
				sleeping(500);
			} catch (Exception e) {
				logger.info("", e);
				continue;
			}

		}
	}

	private void consumeInformation(ArticleCrawlSimilar articleCrawlSimilar) {
		if (Constants.ISSENDDUPLICATETOES == 1) {
			sentToEs(articleCrawlSimilar);
		}
		if (Constants.ISSENDDUPLICATETOTUSHUJU == 1) {
			sentToNe4j(articleCrawlSimilar);
		}

	}

	private void sentToEs(ArticleCrawlSimilar articleCrawlSimilar) {
		List<ClassSectionEntity> classWebsiteEntities = new ClassWebsiteReader()
				.readClassWebsiteEntity();
		StringBuffer classname = new StringBuffer();
		if (classWebsiteEntities != null && classWebsiteEntities.size() > 0) {
			for (ClassSectionEntity classSectionEntity : classWebsiteEntities) {
				if (classSectionEntity.getWebsiteList().contains(
						articleCrawlSimilar.getWebisteId() + "")) {
					if (Constants.CLASSNAMES.contains(classSectionEntity
							.getId() + "")) {
						classname.append(classSectionEntity.getClassname()
								+ "_");
					}
				}
			}
		}
		String tagName = null;
		if (classname.length() > 0) {
			tagName = classname.toString().substring(0, classname.length() - 1);
		}
		if (tagName == null || tagName.trim().isEmpty()) {
			return;
		}
//		tagName = fillTagNames(tagName, "");
		try {
			Map<String, String> params = new HashMap<String, String>();
			// 不重复的id
			params.put("crawl_id", articleCrawlSimilar.getCrawlId() + "");
			params.put("website_id", articleCrawlSimilar.getWebisteId() + "");
			// 重复的id
			params.put("tag", tagName);
			senToES(params);
			senToRemoteES(params);
		} catch (Exception e) {
			logger.info("发送重复的内容给检索系统失败", e);
		}

	}

	private void senToES(Map<String, String> params) {
		try {
			String url = "http://" + Constants.SEACHERIP
					+ "/SearchPlatform/rest/updateNews/appendWebsiteIDAndTag";
			String html = DistributeBuilder.fetcher.post(url, params, null,
					"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("发送重复本地ES的传递失败", e);
			TLSClinet tlsClinet = TLSClinet.getInstance();
			tlsClinet.getFuture().getSession()
					.write("发送重复本地ES的传递失败" + e.getMessage());
			tlsClinet.getFuture().awaitUninterruptibly();
		}
	}

	private void senToRemoteES(Map<String, String> params) {
		try {
			String url = "http://" + Constants.SEACHERIP_REMOTE
					+ "/SearchPlatform/rest/updateNews/appendWebsiteIDAndTag";
			String html = DistributeBuilder.fetcher.post(url, params, null,
					"utf-8");
			System.out.println(html + "++++重复外网ES的传递");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("发送重复外网ES的传递失败" + e);
			TLSClinet tlsClinet = TLSClinet.getInstance();
			tlsClinet.getFuture().getSession()
					.write("发送重复外网ES的传递失败" + e.getMessage());
			tlsClinet.getFuture().awaitUninterruptibly();
		}
	}

//	private static String fillTagNames(String classname, String keyword) {
//		return new LabelRelationshipReader().getTags(classname, keyword);
//	}

	private void sentToNe4j(ArticleCrawlSimilar articleCrawlSimilar) {
		try {
			// http://10.0.2.147:8080/synchronousToCollectSystem/synchronousInformation2.htm
			String url = "http://" + Constants.TUSHUJUIP
					+ "/synchronousToCollectSystem/synchronousInformation2.htm";
			Map<String, String> params = new HashMap<String, String>();
			// 不重复的id
			params.put("docId", articleCrawlSimilar.getCrawlId() + "");
			// 重复的id
			params.put("publishId", articleCrawlSimilar.getSimilarId() + "");
			params.put("webSiteId", articleCrawlSimilar.getWebisteId() + "");
			params.put("webSiteName", articleCrawlSimilar.getWebName());
			params.put("staus", "8");
			// capturedTime=123&publishTime=123
			// 抓取时间
			if (articleCrawlSimilar.getCrawlTime() != null) {
				params.put("capturedTime", articleCrawlSimilar.getCrawlTime()
						.getTime() + "");
			} else {
				params.put("capturedTime", "0");
			}
			// 新闻时间
			if (articleCrawlSimilar.getNewsTime() != null) {
				params.put("publishTime", articleCrawlSimilar.getNewsTime()
						.getTime() + "");
			} else {
				params.put("publishTime", "0");
			}
			// 热度没有
			// params.put("hotValue", articleCrawlSimilar.getHotnum() + "");
			params.put("labels", articleCrawlSimilar.getClassname());
			String html = DistributeBuilder.fetcher.post(url, params, null,
					"utf-8");
			System.out.println(html + "+++++NEO4j重复的传递");
			articleCrawlSimilar.setIstask(1);
			new ArticleCrawlSimilarModifier()
					.updateArticleCrawlSImilarTask(articleCrawlSimilar);
		} catch (Exception e) {
			logger.info("发送去重信息到图数据库失败", e);
			TLSClinet tlsClinet = TLSClinet.getInstance();
			tlsClinet.getFuture().getSession()
					.write("发送去重信息到图数据库失败" + e.getMessage());
			tlsClinet.getFuture().awaitUninterruptibly();
		}
	}

}
