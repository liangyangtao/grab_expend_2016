package com.unbank.exceptionCaught;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.unbank.distribute.dao.UserErrorPushInfoStore;
import com.unbank.mina.clent.TLSClinet;
import com.unbank.mybatis.entity.UserErrorPushInfo;

public class ExceptionCaught {

	public void reportExceptByUrlAndParams(Map<String, String> params,
			String url, String errorMessage) {
		Map<String, Object> errorInfo = new HashMap<String, Object>();
		errorInfo.put("type", 0);
		errorInfo.put("url", url);
		errorInfo.put("params", params);
		sendErrorInfo(errorMessage);
//		saveUserErrorPushInfo(errorInfo);
	}

	public void saveUserErrorPushInfo(Object errorInfo) {
		JSONObject jsonObject = JSONObject.fromObject(errorInfo);
		UserErrorPushInfo userErrorPushInfo = new UserErrorPushInfo();
		userErrorPushInfo.setErrorPushInfo(jsonObject.toString());
		userErrorPushInfo.setIstask(0);
//		new UserErrorPushInfoStore().saveUserErrorPushInfo(userErrorPushInfo);
	}

	public void sendErrorInfo(Object errorMessage) {
		TLSClinet tlsClinet = TLSClinet.getInstance();
		tlsClinet.getFuture().getSession().write(errorMessage);
		tlsClinet.getFuture().awaitUninterruptibly();
	}

}
