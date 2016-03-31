package com.unbank.pipeline.queue;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.unbank.distribute.dao.UserErrorPushInfoReader;
import com.unbank.duplicate.dao.ArticleCrawlSimilarModifier;
import com.unbank.duplicate.dao.ArticleCrawlSimilarReader;
import com.unbank.mybatis.entity.ArticleCrawlSimilar;
import com.unbank.mybatis.entity.UserErrorPushInfo;

public class ErrorInformationProduct extends BaseQueue implements Runnable {
	protected LinkedBlockingQueue<Object> informationQueue;

	public ErrorInformationProduct(LinkedBlockingQueue<Object> informationQueue) {
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
		List<UserErrorPushInfo> userErrorPushInfos = new UserErrorPushInfoReader()
				.readUserErrorPushInfo();
		for (UserErrorPushInfo userErrorPushInfo : userErrorPushInfos) {
			put(informationQueue, userErrorPushInfo);
		}

	}

}
