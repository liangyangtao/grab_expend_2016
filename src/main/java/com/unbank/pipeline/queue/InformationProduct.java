package com.unbank.pipeline.queue;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.unbank.Constants;
import com.unbank.mybatis.dao.InformationModifier;
import com.unbank.mybatis.dao.InformationReader;
import com.unbank.pipeline.entity.Information;

public class InformationProduct extends BaseQueue implements Runnable {
	protected LinkedBlockingQueue<Object> informationQueue;

	public InformationProduct(LinkedBlockingQueue<Object> informationQueue) {
		this.informationQueue = informationQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (informationQueue.size() <= 0) {
					fillQueue();
				}
				// 取一次50条， 休眠1分钟
				sleeping(1000 * 60);
			} catch (Exception e) {
				logger.info("", e);
				continue;
			}

		}
	}

	public void fillQueue() {
		List<Information> informations = new InformationReader()
				.readInformationByFileIndexAndTask(Constants.CLENTFILEINDEX,
						Constants.CLENTTASK, Constants.CLENTLIMITNUM);
		for (Information information : informations) {
			try {
				information.setFile_index((byte) 6);
				new InformationModifier()
						.updateInformationFileIndex(information);
			} catch (Exception e) {
				logger.info("更新file_index 出错", e);
				continue;
			} finally {
				information.setFile_index((byte) 7);
			}
		}
		for (Information information : informations) {
			put(informationQueue, information);
		}
		informations.clear();
	}

	public void fillQueueFileIndex6() {
		List<Information> informations = new InformationReader()
				.readInformationByFileIndexAndTask(6, Constants.CLENTTASK,
						10000);
		if (informations.size() == 0) {
			sleeping(1000 * 30);
		}
		for (Information information : informations) {
			try {

			} catch (Exception e) {
				logger.info("更新file_index 出错", e);
				continue;
			} finally {
				information.setFile_index((byte) 7);
			}
		}
		for (Information information : informations) {
			put(informationQueue, information);
		}
		informations.clear();
	}

}
