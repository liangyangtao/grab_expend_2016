package com.unbank.mina.clent;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.unbank.exceptionCaught.Constants;
import com.unbank.view.RightCornerPopMessage;

public class TLSClentHandler extends IoHandlerAdapter {

	static Log logger = LogFactory.getLog(TLSClentHandler.class);

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("与服务器建立连接");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		InetSocketAddress remoteAddress = (InetSocketAddress) session
				.getRemoteAddress();
		logger.info(remoteAddress + "和服务器断开了链接                  Session 失效");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// super.exceptionCaught(session, cause);
		InetSocketAddress remoteAddress = (InetSocketAddress) session
				.getRemoteAddress();
		logger.info(remoteAddress + "有异常");
		if (Constants.constants.ISTANCHUANG) {
			new RightCornerPopMessage(remoteAddress + "有异常");
		}
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		logger.info("客户端接受的消息:" + message);
		if (Constants.constants.ISTANCHUANG) {
			String temp = (String) message.toString();
			new RightCornerPopMessage(temp);
		}

	}

}