package com.unbank.exceptionCaught;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.unbank.mina.clent.TLSClinet;
import com.unbank.mina.server.TLSServer;

public class MinaServerConsole {

	public static Log logger = LogFactory.getLog(MinaServerConsole.class);
	static {
		// 启动日志
		try {
			System.out.println(MinaServerConsole.class.getClassLoader()
					.getResource("").toURI().getPath()
					+ "log4j.properties");

			PropertyConfigurator.configure(MinaServerConsole.class.getClassLoader()
					.getResource("").toURI().getPath()
					+ "log4j.properties");

			logger.info("---日志系统启动成功---");
		} catch (Exception e) {
			logger.error("日志系统启动失败:", e);
		}
	}

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		TLSServer tlsServer = new TLSServer();

	}
}
