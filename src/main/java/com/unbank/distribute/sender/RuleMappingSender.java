package com.unbank.distribute.sender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unbank.Constants;
import com.unbank.fetch.Fetcher;
import com.unbank.pipeline.entity.Information;

public class RuleMappingSender extends BaseInfoSender {
	public static Log logger = LogFactory.getLog(RuleMappingSender.class);

	public boolean sendInfo(Information information) {
		boolean istrue = true;
		if (Constants.RULEMAPPINGIP.isEmpty()) {
			return istrue;
		}
		List<String> urls = Arrays.asList(Constants.RULEMAPPINGIP.split(","));
		for (String string : urls) {
			String url = "http://" + string
					+ "/ubk_ruleMapping/rest/ruleMapping/receiveInfo";
			Map<String, String> params = new HashMap<String, String>();
			try {
				Map<String, Object> sendMap = new HashMap<String, Object>();
				sendMap.put("crawl_id", information.getCrawl_id());
				sendMap.put("website_id", information.getWebsite_id());
				sendMap.put("crawl_title", information.getCrawl_title());
				sendMap.put("crawl_brief", information.getCrawl_brief());
				sendMap.put("crawl_views", information.getCrawl_views());
				sendMap.put("web_name", information.getWeb_name());
				sendMap.put("url", information.getUrl());
				sendMap.put("file_index", information.getFile_index());
				sendMap.put("news_time", information.getNews_time().getTime());
				sendMap.put("crawl_time", information.getCrawl_time().getTime());
				sendMap.put("task", information.getTask());
				sendMap.put("text", information.getText());
				JSONObject json = JSONObject.fromObject(sendMap);
				params.put("infor", json.toString());
				String html = Fetcher.getInstance().post(url, params, null,
						"utf-8");
				if (html.equals("success")) {
					logger.info("发送给规则映射引擎成功        "
							+ information.getCrawl_id());
				} else {
					istrue = false;
					logger.info("发送给规则映射引擎出错" + information.getCrawl_id());
				}
			} catch (Exception e) {
				istrue = false;
				logger.info("发送给规则映射引擎失败" + information.getCrawl_id(), e);
			}
		}
		return istrue;
	}

}
