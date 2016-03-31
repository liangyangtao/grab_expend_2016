package com.unbank.classify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unbank.Constants;
import com.unbank.exceptionCaught.ExceptionCaught;
import com.unbank.fetch.Fetcher;

public class LabelRelationshipReader {
	public static Log logger = LogFactory.getLog(LabelRelationshipReader.class);

	public String getTags(String label, String sourcetags, String sourceKeyword) {
		try {
			Set<String> temp = new HashSet<String>();
			Set<String> allTags = new HashSet<String>();
			temp.add(label);
			// 首先把关键词填到Set里 组成 查询条件
			if (sourceKeyword != null) {
				String keywordtemp[] = sourceKeyword.split("/");
				for (String string : keywordtemp) {
					temp.add(string);
				}
			}
			// 把标签也放到 temp 里
			if (sourcetags != null) {
				String tagtemp[] = sourcetags.split("_");
				for (String string : tagtemp) {
					temp.add(string);
				}
				allTags.addAll(Arrays.asList(tagtemp));
			}
			StringBuffer wordsBuffer = new StringBuffer();
			for (String string : temp) {
				wordsBuffer.append(string + "_");
			}
			if (wordsBuffer.length() > 1) {
				String html = null;
				try {
					String url = "http://" + Constants.TUSHUJUIP
							+ "/synchronous/getAllRelatedTags.htm";
					Map<String, String> params = new HashMap<String, String>();
					params.put("words",
							wordsBuffer.substring(0, wordsBuffer.length() - 1));
					html = Fetcher.getInstance().post(url, params, null,
							"utf-8");
				} catch (Exception e) {
					logger.info("读取数据库标签出错", e);
					String errorMessage = "读取数据库标签出错     " + e.getMessage();
					new ExceptionCaught().sendErrorInfo(errorMessage);
				}
				if (html != null) {
					List<String> tagList = null;
					tagList = JSONArray.fromObject(html);
					allTags.addAll(tagList);
				}
			}
			StringBuffer tag = new StringBuffer();
			for (String string : allTags) {
				tag.append(string + " ");
			}
			if (tag.length() > 1) {
				return tag.substring(0, tag.length() - 1);
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.info("读取标签出错", e);
		}
		return "";
	}
}
