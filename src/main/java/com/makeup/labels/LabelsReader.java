package com.makeup.labels;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.unbank.Constants;
import com.unbank.fetch.Fetcher;
import com.unbank.fetch.HttpClientBuilder;
import com.unbank.pipeline.entity.Information;

public class LabelsReader {
	public static Fetcher fetcher;

	public void readLabels(Information information) {
		String keywords = information.getKeywords();
		String tagNames = information.getClassname();
		String keywordTemp[] = keywords.split("/");
		StringBuffer keywordSb = new StringBuffer();
		for (String string : keywordTemp) {
			keywordSb.append(string + "_");
		}
		String url = "http://" + Constants.SEACHERIP
				+ "/SearchPlatform/rest/tagRelation/getTagsByKeyWords";
		Map<String, String> params = new HashMap<String, String>();
		params.put(
				"keywords",
				keywords == null ? "" : keywordSb.substring(0,
						keywordSb.length() - 1));
		params.put("tagNames", tagNames == null ? "" : tagNames);
		String html = fetcher.post(url, params, null, "utf-8");
		if (html.contains("参数错误")) {
			information.setLabels("");
		} else {
			JSONArray jsonArray = JSONArray.fromObject(html);
			StringBuffer sb = new StringBuffer();
			for (Object object : jsonArray) {
				sb.append(object + "_");
			}
			if (sb.length() > 1) {
				information.setLabels(sb.substring(0, sb.length() - 1));
			} else {
				information.setLabels("");
			}
		}
		System.out.println(information.getLabels());
	}

	public static void main(String[] args) {
		Constants.init();
		Information information = new Information();
		information.setClassname("");
		information.setKeywords("");
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		BasicCookieStore cookieStore = new BasicCookieStore();
		HttpClientBuilder httpClientBuilder = new HttpClientBuilder(false,
				poolingHttpClientConnectionManager, cookieStore);
		CloseableHttpClient httpClient = httpClientBuilder.getHttpClient();
		fetcher = new Fetcher(cookieStore, httpClient);
		new LabelsReader().readLabels(information);

	}
}
