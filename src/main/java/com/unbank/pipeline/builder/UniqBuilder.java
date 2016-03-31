package com.unbank.pipeline.builder;

import com.unbank.Constants;
import com.unbank.duplicate.qiangkong.DocBuffer;
import com.unbank.duplicate.qiangkong.DuplicateService;
import com.unbank.pipeline.entity.Information;

public class UniqBuilder extends AbstractArticleInfoBuilder {

	@Override
	public void createArticleEntity(Information information) {
		if (information.getFile_index() == 7) {
			// Similarity.panchong(information);

			// SimilarityBySimHash.panchong(information);

			// SimilarityByServe.panchong(information);
			Integer websiteId = information.getWebsite_id();
			// 是否是白名单中的就不去重了
			if (Constants.WHITELIST.contains(websiteId + "")) {
				information.setFile_index((byte) 0);
				return;
			}
			int did = information.getCrawl_id();
			String title = information.getCrawl_title();
			String html = information.getText();
			try {
				DuplicateService duplicate = new DuplicateService();
				DocBuffer buffer = duplicate
						.docDuplicate(did + "", title, html);
				if (buffer != null && buffer.getFlag() == 3) {
					information.setFile_index((byte) 0);
				} else {
					information.setFile_index((byte) 8);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("比较重复失败", e);
				information.setFile_index((byte) 8);
			}

		}

	}

}
