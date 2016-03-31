package com.unbank.exceptionCaught;

import org.springframework.stereotype.Component;

@Component
public class Constants {
	public Integer SERVERPORT;
	public String SERVERIP;
	public boolean USE_SSL;

	public int HEARTBEATRATE;
	public String HEARTBEATREQUEST = "HEARTBEATREQUEST";
	public String HEARTBEATRESPONSE = "HEARTBEATRESPONSE";
	public int HEART_TIMEOUT;
	public int CLENT_TIMEOUT;
	public static Constants constants;
	public Boolean ISTANCHUANG;

	public void init() {
		constants = this;
		constants.SERVERPORT = this.SERVERPORT;
		constants.SERVERIP = this.SERVERIP;
		constants.USE_SSL = this.USE_SSL;
		constants.HEARTBEATRATE = this.HEARTBEATRATE;
		constants.HEARTBEATREQUEST = this.HEARTBEATREQUEST;
		constants.HEARTBEATRESPONSE = this.HEARTBEATRESPONSE;
		constants.HEART_TIMEOUT = this.HEART_TIMEOUT;
		constants.CLENT_TIMEOUT = this.CLENT_TIMEOUT;
		constants.ISTANCHUANG = this.ISTANCHUANG;

	}

	public Integer getSERVERPORT() {
		return SERVERPORT;
	}

	public void setSERVERPORT(Integer sERVERPORT) {
		SERVERPORT = sERVERPORT;
	}

	public String getSERVERIP() {
		return SERVERIP;
	}

	public void setSERVERIP(String sERVERIP) {
		SERVERIP = sERVERIP;
	}

	public boolean isUSE_SSL() {
		return USE_SSL;
	}

	public void setUSE_SSL(boolean uSE_SSL) {
		USE_SSL = uSE_SSL;
	}

	public int getHEARTBEATRATE() {
		return HEARTBEATRATE;
	}

	public void setHEARTBEATRATE(int hEARTBEATRATE) {
		HEARTBEATRATE = hEARTBEATRATE;
	}

	public String getHEARTBEATREQUEST() {
		return HEARTBEATREQUEST;
	}

	public void setHEARTBEATREQUEST(String hEARTBEATREQUEST) {
		HEARTBEATREQUEST = hEARTBEATREQUEST;
	}

	public String getHEARTBEATRESPONSE() {
		return HEARTBEATRESPONSE;
	}

	public void setHEARTBEATRESPONSE(String hEARTBEATRESPONSE) {
		HEARTBEATRESPONSE = hEARTBEATRESPONSE;
	}

	public int getHEART_TIMEOUT() {
		return HEART_TIMEOUT;
	}

	public void setHEART_TIMEOUT(int hEART_TIMEOUT) {
		HEART_TIMEOUT = hEART_TIMEOUT;
	}

	public int getCLENT_TIMEOUT() {
		return CLENT_TIMEOUT;
	}

	public void setCLENT_TIMEOUT(int cLENT_TIMEOUT) {
		CLENT_TIMEOUT = cLENT_TIMEOUT;
	}

	public static Constants getConstants() {
		return constants;
	}

	public static void setConstants(Constants constants) {
		Constants.constants = constants;
	}

	public Boolean getISTANCHUANG() {
		return ISTANCHUANG;
	}

	public void setISTANCHUANG(Boolean iSTANCHUANG) {
		ISTANCHUANG = iSTANCHUANG;
	}

}
