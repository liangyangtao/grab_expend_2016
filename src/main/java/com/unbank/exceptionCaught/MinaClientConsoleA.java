package com.unbank.exceptionCaught;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.unbank.mina.clent.TLSClinet;

public class MinaClientConsoleA {

	public static Log logger = LogFactory.getLog(MinaClientConsoleA.class);
	static {
		// 启动日志
		try {
			System.out.println(MinaClientConsoleA.class.getClassLoader()
					.getResource("").toURI().getPath()
					+ "log4j.properties");

			PropertyConfigurator.configure(MinaClientConsoleA.class
					.getClassLoader().getResource("").toURI().getPath()
					+ "log4j.properties");

			logger.info("---日志系统启动成功---");
		} catch (Exception e) {
			logger.error("日志系统启动失败:", e);
		}
	}

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		TLSClinet tlsClinet = new TLSClinet();
		// 异常例子

		try {
			int a = 1 / 0;
		} catch (Exception e) {
			tlsClinet.getFuture().getSession().write(e);
		}

		tlsClinet.getFuture().getSession().getCloseFuture()
				.awaitUninterruptibly();
	}
}
