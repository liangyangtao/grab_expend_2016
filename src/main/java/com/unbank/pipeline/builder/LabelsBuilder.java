package com.unbank.pipeline.builder;

import com.makeup.labels.LabelsReader;
import com.unbank.pipeline.entity.Information;

public class LabelsBuilder extends AbstractArticleInfoBuilder {

	@Override
	public void createArticleEntity(Information information) {
		new LabelsReader().readLabels(information);
	}

}
