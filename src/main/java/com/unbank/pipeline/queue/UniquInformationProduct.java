package com.unbank.pipeline.queue;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.unbank.duplicate.dao.ArticleCrawlSimilarModifier;
import com.unbank.duplicate.dao.ArticleCrawlSimilarReader;
import com.unbank.mybatis.entity.ArticleCrawlSimilar;

public class UniquInformationProduct extends BaseQueue implements Runnable {
	protected LinkedBlockingQueue<Object> informationQueue;

	public UniquInformationProduct(LinkedBlockingQueue<Object> informationQueue) {
		this.informationQueue = informationQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (informationQueue.size() <= 0) {
					fillQueue();
				}
				sleeping(1000);
			} catch (Exception e) {
				logger.info("", e);
				continue;
			}

		}
	}

	private void fillQueue() {

		List<ArticleCrawlSimilar> articleCrawlSimilars = new ArticleCrawlSimilarReader()
				.readArticleCrawlSimilar(0);
		if (articleCrawlSimilars.size() == 0) {
			sleeping(1000 * 30);
		}
		for (ArticleCrawlSimilar articleCrawlSimilar : articleCrawlSimilars) {
			try {
				articleCrawlSimilar.setIstask(6);
				new ArticleCrawlSimilarModifier()
						.updateArticleCrawlSImilarTask(articleCrawlSimilar);
			} catch (Exception e) {
				logger.info("更新file_index 出错", e);
				continue;
			} finally {
				articleCrawlSimilar.setIstask(0);
			}
		}
		for (ArticleCrawlSimilar articleCrawlSimilar : articleCrawlSimilars) {
			put(informationQueue, articleCrawlSimilar);
		}
		articleCrawlSimilars.clear();
	}

	public void fillQueueFileIndex6() {
		List<ArticleCrawlSimilar> articleCrawlSimilars = new ArticleCrawlSimilarReader()
				.readArticleCrawlSimilar(6);
		if (articleCrawlSimilars.size() == 0) {
			sleeping(1000 * 30);
		}
		for (ArticleCrawlSimilar articleCrawlSimilar : articleCrawlSimilars) {
			try {

			} catch (Exception e) {
				logger.info("更新file_index 出错", e);
				continue;
			} finally {
				articleCrawlSimilar.setIstask(0);
			}
		}
		for (ArticleCrawlSimilar articleCrawlSimilar : articleCrawlSimilars) {
			put(informationQueue, articleCrawlSimilar);
		}
		articleCrawlSimilars.clear();
	}

}
