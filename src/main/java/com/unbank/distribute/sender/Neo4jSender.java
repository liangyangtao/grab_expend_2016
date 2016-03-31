package com.unbank.distribute.sender;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unbank.Constants;
import com.unbank.GrabExpandConsole;
import com.unbank.exceptionCaught.ExceptionCaught;
import com.unbank.fetch.Fetcher;
import com.unbank.pipeline.entity.Information;

public class Neo4jSender extends BaseInfoSender {
	public static Log logger = LogFactory.getLog(Neo4jSender.class);

	public boolean sendInfo(Information information) {
		boolean istrue = true;
		Map<String, String> params = fillParams(information);
		String url = "http://" + Constants.TUSHUJUIP
				+ "/synchronousToCollectSystem/synchronousInformation1.htm";
		if (Constants.ISSENDTUSHUJU == 1) {
			try {
				String html = Fetcher.getInstance().post(url, params, null,
						"utf-8");
				if (html.equals("{\"success\":1}")) {
					logger.info("发给图数据库成功             "
							+ information.getCrawl_id());
					istrue = true;
				} else {
					logger.info("发给图数据库失败             "
							+ information.getCrawl_id());
					istrue = false;
				}
			} catch (Exception e) {
				istrue = false;
				logger.info("发给图数据库失败         " + information.getCrawl_id(), e);
				String errorMessage = "发给图数据库失败         "
						+ information.getCrawl_id() + "  " + e.getMessage();
				new ExceptionCaught().reportExceptByUrlAndParams(params, url,
						errorMessage);
			}
		}

		return istrue;
	}

	private Map<String, String> fillParams(Information information) {
		Integer doc_id = information.getCrawl_id();
		String keyword = information.getKeywords();
		Long time = information.getCrawl_time().getTime();
		Map<String, String> params = new HashMap<String, String>();
		params.put("docId", doc_id + "");
		params.put("publishId", doc_id + "");
		params.put("webSiteId", information.getWebsite_id() + "");
		params.put("webSiteName", information.getWeb_name());
		params.put("staus", "0");
		params.put("capturedTime", time + "");
		params.put("publishTime", information.getNews_time().getTime() + "");
		String className = information.getClassname();
		StringBuffer sb = new StringBuffer();
		if (className != null) {
			String temp[] = className.split("_");
			for (String string : temp) {
				sb.append(string + ",");
			}
		}
		if (sb.length() > 1) {
			params.put("labels", sb.substring(0, sb.length() - 1));
		} else {
			params.put("labels", "");
		}
		params.put("keyWords", keyword);
		return params;
	}

}
