package com.unbank;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.unbank.classify.InformationClassify;
import com.unbank.classify.place.WordLorder;
import com.unbank.classify.qiankong.ClassifierService;
import com.unbank.duplicate.qiangkong.DuplicateService;
import com.unbank.fetch.Fetcher;
import com.unbank.pipeline.builder.ArticleBuilderFactory;
import com.unbank.pipeline.builder.ChainedArticleBuilder;
import com.unbank.pipeline.builder.ClassifyBuilder;
import com.unbank.pipeline.builder.DistributeBuilder;
import com.unbank.pipeline.builder.UniqBuilder;
import com.unbank.pipeline.queue.ErrorInformationConsume;
import com.unbank.pipeline.queue.ErrorInformationProduct;
import com.unbank.pipeline.queue.InformationConsume;
import com.unbank.pipeline.queue.InformationProduct;
import com.unbank.pipeline.queue.UniquInformationConsume;
import com.unbank.pipeline.queue.UniquInformationProduct;

public class GrabExpandConsole {

	private static Log logger = LogFactory.getLog(GrabExpandConsole.class);

	static {
		// 启动日志
		try {
			PropertyConfigurator.configure(GrabExpandConsole.class
					.getClassLoader().getResource("").toURI().getPath()
					+ "log4j.properties");
			logger.info("---日志系统启动成功---");
		} catch (Exception e) {
			logger.error("日志系统启动失败:", e);
		}
	}

	public static ChainedArticleBuilder chainedArticleBuilder;

	public static ChainedArticleBuilder buildArticleChainedTask() {
		ChainedArticleBuilder chainedArticleBuilder = new ChainedArticleBuilder();
		ArticleBuilderFactory articleBuilderFactory = ArticleBuilderFactory
				.getInstance();
		// 去重
		UniqBuilder uniqBuilder = new UniqBuilder();
		// 提取关键词
		/***
		 * 调用2015年12日新的获取关键词程序，我的废弃掉
		 */
		// KeywordBuilder keywordBuilder = new KeywordBuilder();
		/**
		 * 废弃结束
		 */

		// 分类
		ClassifyBuilder classifyBuilder = new ClassifyBuilder();
		// 同步
		DistributeBuilder distributeBuilder = new DistributeBuilder();
		// 注意顺序，别还没去重就同步了
		articleBuilderFactory.register(uniqBuilder);
		// articleBuilderFactory.register(keywordBuilder);
		articleBuilderFactory.register(classifyBuilder);
		articleBuilderFactory.register(distributeBuilder);
		chainedArticleBuilder.addBuilders(articleBuilderFactory.getBuilders());
		return chainedArticleBuilder;
	}

	public static void init() {
		Constants.init();
		Fetcher.getInstance();
		InformationClassify.init();
		ClassifierService.init();
		WordLorder wordLorder = new WordLorder();
		wordLorder.init();
		// Integer fileindex, Integer task, Integer num
		DuplicateService.init(Constants.DUPLICATEFILEINDEX,
				Constants.DUPLICATETASK, Constants.DUPLICATELIMITNUM);
		chainedArticleBuilder = buildArticleChainedTask();
	}

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		init();
		LinkedBlockingQueue<Object> informationQueue = new LinkedBlockingQueue<Object>();
		ExecutorService executor = Executors.newFixedThreadPool(96);
		for (int i = 0; i < 12; i++) {
			executor.execute(new InformationConsume(informationQueue,
					chainedArticleBuilder));
		}
		executor.execute(new InformationProduct(informationQueue));
		if (Constants.ISENDDUPLICATE == 1) {
			LinkedBlockingQueue<Object> uniqInformationQueue = new LinkedBlockingQueue<Object>();
			for (int i = 0; i < 5; i++) {
				executor.execute(new UniquInformationConsume(
						uniqInformationQueue));
			}
			executor.execute(new UniquInformationProduct(uniqInformationQueue));
		}

		if (Constants.ISSENDFAILURE == 1) {
			LinkedBlockingQueue<Object> errorInformationQueue = new LinkedBlockingQueue<Object>();
			for (int i = 0; i < 5; i++) {
				executor.execute(new ErrorInformationConsume(
						errorInformationQueue));
			}
			executor.execute(new ErrorInformationProduct(errorInformationQueue));
		}

		executor.shutdown();
	}

}
