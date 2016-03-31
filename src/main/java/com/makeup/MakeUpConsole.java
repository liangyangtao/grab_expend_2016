package com.makeup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.makeup.customized.CustomizedRule;
import com.unbank.pipeline.entity.Information;

public class MakeUpConsole {
	private static Log logger = LogFactory.getLog(MakeUpConsole.class);

	static {
		// 启动日志
		try {
			PropertyConfigurator.configure(MakeUpConsole.class.getClassLoader()
					.getResource("").toURI().getPath()
					+ "log4j.properties");
			logger.info("---日志系统启动成功---");
		} catch (Exception e) {
			logger.error("日志系统启动失败:", e);
		}
	}
	
	public static void main(String[] args) {
		
		Information information = new Information();
		information.setWebsite_id(12452);
		information.setLabels("招商银行_周小川_小窗");
		new CustomizedRule().fillCustomizedRule(information);

	}
}
