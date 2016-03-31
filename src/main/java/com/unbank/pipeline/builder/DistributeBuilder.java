package com.unbank.pipeline.builder;

import java.util.HashMap;
import java.util.Map;

import com.unbank.Constants;
import com.unbank.distribute.sender.ElasticsearchSender;
import com.unbank.distribute.sender.InformationSender;
import com.unbank.distribute.sender.Neo4jSender;
import com.unbank.distribute.sender.RuleMappingSender;
import com.unbank.fetch.Fetcher;
import com.unbank.mybatis.dao.WebsiteInfoReader;
import com.unbank.mybatis.entity.WebSiteInfo;
import com.unbank.pipeline.entity.Information;

public class DistributeBuilder extends AbstractArticleInfoBuilder {

	public static Fetcher fetcher;

	public static Map<Integer, WebSiteInfo> websiteinfos = new HashMap<Integer, WebSiteInfo>();

	@Override
	public void createArticleEntity(Information information) {
		if (information.getWebsite_id() > 10000000) {
			int websiteid = information.getWebsite_id();
			information.setWebsite_id(1711);
			sendInformation(information);
			information.setWebsite_id(websiteid - 10000000);
			logger.info(information.getCrawl_id() + "   被同步到银行高管");
		} else {
			sendInformation(information);
		}

	}

	public void sendInformation(Information information) {

		// 获取文章的网站和板块来源

		if (information.getFile_index() == 1) {
			information.setFile_index((byte) 0);
			// 发送给本地服务器
			senToLocalServer(information);
			//
			WebSiteInfo webSiteInfo = null;
			if (websiteinfos.get(information.getWebsite_id()) == null) {
				webSiteInfo = new WebsiteInfoReader()
						.readerWebSiteInfoByWebsiteID(information
								.getWebsite_id());
				information.setSectionName(webSiteInfo.getSectionName());
				websiteinfos.put(information.getWebsite_id(), webSiteInfo);

			} else {
				webSiteInfo = websiteinfos.get(information.getWebsite_id());
			}

			information.setWeb_url(webSiteInfo.getUrlHome());
			// 发送给ES
			// 发送给图数据库
			senToLabelServer(information);
			// 发送给规则引擎
			senToRuleMapping(information);
			information.setFile_index((byte) 1);
		} else if (information.getFile_index() == 8) {
			// information.setFile_index((byte) 0);
			// new InformationSender().sendInformationByUniq(information);
			// information.setFile_index((byte) 8);
		}
	}

	private void senToRuleMapping(Information information) {
		if (Constants.ISSENDTORULEMAPPING == 1) {
			new RuleMappingSender().sendInfo(information);
		}
	}

	private void senToLabelServer(Information information) {
		try {
			// if (Constants.ISSENDTOES == 1) {
			new ElasticsearchSender().sendInfo(information);
			// }
			if (Constants.ISSENDTUSHUJU == 1) {
				new Neo4jSender().sendInfo(information);
			}
		} catch (Exception e) {
			logger.info("发送到检索平台失败", e);
		}
	}

	private void senToLocalServer(Information information) {
		// if (information.getWebsite_id() > 10000000) {
		// int websiteid = information.getWebsite_id();
		// information.setWebsite_id(1711);
		// new InformationSender().sendInformation(information);
		// information.setWebsite_id(websiteid - 10000000);
		// logger.info(information.getCrawl_id() + "   被同步到银行高管");
		// } else {
		new InformationSender().sendInformation(information);
		// }
	}

}
