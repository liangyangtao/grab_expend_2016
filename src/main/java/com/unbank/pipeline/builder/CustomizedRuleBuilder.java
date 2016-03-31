package com.unbank.pipeline.builder;

import com.makeup.customized.CustomizedRule;
import com.unbank.pipeline.entity.Information;

public class CustomizedRuleBuilder extends AbstractArticleInfoBuilder {

	@Override
	public void createArticleEntity(Information information) {
		new CustomizedRule().fillCustomizedRule(information);
	}

}
