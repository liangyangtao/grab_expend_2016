package com.unbank.pipeline.queue;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSONObject;

import com.unbank.distribute.dao.UserErrorInfoUpdater;
import com.unbank.fetch.Fetcher;
import com.unbank.mybatis.entity.UserErrorPushInfo;

public class ErrorInformationConsume extends BaseQueue implements Runnable {

	protected LinkedBlockingQueue<Object> errorInformationQueue;

	public ErrorInformationConsume(LinkedBlockingQueue<Object> informationQueue) {
		this.errorInformationQueue = informationQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (errorInformationQueue.size() <= 10000
						&& errorInformationQueue.size() > 0) {
					UserErrorPushInfo userErrorPushInfo = (UserErrorPushInfo) take(errorInformationQueue);
					if (userErrorPushInfo != null) {
						consumeInformation(userErrorPushInfo);
					}
				}
				sleeping(500);
			} catch (Exception e) {
				logger.info("", e);
				continue;
			}

		}
	}

	private void consumeInformation(UserErrorPushInfo userErrorPushInfo) {

		// 重新发送
		try {
			String errorPushinfo = userErrorPushInfo.getErrorPushInfo();
			JSONObject jsonObject = JSONObject.fromObject(errorPushinfo);
			Integer type = jsonObject.getInt("type");
			boolean isTrue = false;

			if (type == 0) {
				// url
				// 参数
				// http post submit
				isTrue = sendByHttp(jsonObject);
			} else if (type == 1) {
				//

			} else {

			}
			if (isTrue) {
				// 如果成功修改状态
				userErrorPushInfo.setIstask(1);
				new UserErrorInfoUpdater()
						.updateUserErrorPushInfo(userErrorPushInfo);
			}
		} catch (Exception e) {
			// 失败则什么也不做
			logger.info("重新发送失败的信息出错", e);
		} finally {

		}
	}

	public boolean sendByHttp(JSONObject jsonObject) {
		try {
			String url = jsonObject.getString("url");
			Map<String, String> params = (Map<String, String>) jsonObject
					.get("params");
			String html = Fetcher.getInstance()
					.post(url, params, null, "utf-8");
			if (html == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			logger.info("重新传输失败：" + jsonObject, e);
			return false;
		}
	}
}
