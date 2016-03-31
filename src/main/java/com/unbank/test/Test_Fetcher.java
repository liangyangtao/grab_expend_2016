package com.unbank.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class Test_Fetcher {

	private final static int PAGESIZE = 20;

	private static String host = "http://10.0.2.25:8080/recommend/getDocByLabelFilter.htm";

	@Test
	public void test_index2() throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(
				"http://10.0.0.16:8080/SearchPlatform/rest/index/oper");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("crawl_id", "289989759"));
		nvps.add(new BasicNameValuePair("title", "光大银行直销银行将于今年6月上线"));
		nvps.add(new BasicNameValuePair("url",
				"http://zx.cebnet.com.cn/20150318/101149404.html"));
		nvps.add(new BasicNameValuePair(
				"content",
				"<div> <div> <p>据理财周报记者了解，目前光大银行直销银行的筹备已经进入最后阶段。光大银行方面称，将于今年6月正式推出直销银行服务。</p> <p>此前，已有北京银行、民生银行、兴业银行、恒丰银行、华夏银行、平安银行等近20家银行的直销银行纷纷上线。</p> <p>近期，工行的“工银融e行”也已悄然上线。民生银行直销银行刚刚过了周岁生日，其直销银行的资产规模目前已经接近250亿元，客户数量超过了160万。</p> <p>据了解，即将上线的光大银行直销银行将突破传统银行“高大全”的形象，定位“小轻新”，客群定位上有别于以往的“覆盖千家万户”，而是专注服务“忙、潮、精”的年轻人群，产品设计上也不再要求全面，而是主打快速理财、缴费、便民精品等，力求简单、实惠。</p> <p>“光大银行素以‘理财银行’在业内外扬名，为配合直销银行上线，光大银行特地根据目标客群的需求，推出了财富保、定存宝、积存金、实物金和e理财等优质理财产品，并上线“光大云缴费”平台。”光大银行电子银行部总经理杨兵兵表示。</p> <p>据理财周报记者了解，光大银行的直销银行隶属于电子银行部，目前尚未单独设立部门。</p> <p>责任编辑：Rachel</p> </div></div>"));
		nvps.add(new BasicNameValuePair("newsDate", new Date().getTime() + ""));
		nvps.add(new BasicNameValuePair("crawlDate", new Date().getTime() + ""));
		nvps.add(new BasicNameValuePair("webName", "中国电子银行网"));
		nvps.add(new BasicNameValuePair("webSectionName", "业内资讯"));
		nvps.add(new BasicNameValuePair("website_id", "5890"));
		nvps.add(new BasicNameValuePair("p_url", "http://zx.cebnet.com.cn/"));
		nvps.add(new BasicNameValuePair("tagName", "银行"));
		nvps.add(new BasicNameValuePair("keyWords", "银行，直销"));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = httpclient.execute(httpost);
		// System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();

		String s = EntityUtils.toString(entity);
		System.out.println(s);
	}

/*	@Test
	public void test_index3() throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(
				"http://10.0.2.20/SearchPlatform/rest/index/opt");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("crawl_id", "1155527"));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
		String s = EntityUtils.toString(entity);
		System.out.println(s);
	}*/

}
