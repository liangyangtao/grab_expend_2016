package com.unbank.pipeline.builder;

import com.unbank.classify.InformationClassify;
import com.unbank.pipeline.entity.Information;

public class ClassifyBuilder extends AbstractArticleInfoBuilder {

	@Override
	public void createArticleEntity(Information information) {
		if (information.getFile_index() == 0) {
			new InformationClassify().transInformation(information);
			// 保存类别到本地数据库中
//			if (information.getObject() != null) {
//				List<PtfDoc> ptfdocs = (List<PtfDoc>) information.getObject();
//				for (PtfDoc ptfDoc : ptfdocs) {
//					new LabelWriter().saveLable(ptfDoc, information);
//				}
//
//			}
			information.setFile_index((byte) 1);
		}

	}

}
