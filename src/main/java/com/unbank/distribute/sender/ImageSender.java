package com.unbank.distribute.sender;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unbank.distribute.entity.Platform;

public class ImageSender {
	private static Log logger = LogFactory.getLog(ImageSender.class);

	// public String saveImage(String imageUrl, String imageHost) {
	// String nowImageName = null;
	// try {
	// String temp[] = imageUrl.split("/");
	// BufferedImage imge = saveImage(imageUrl);
	// String todayStr = temp[5];
	// String imagePath = "//" + imageHost.split(":")[0] + "/images/"
	// + todayStr + "/";
	// String imageFileName = temp[temp.length - 1];
	// File imageDir = new File(imagePath);
	// if (!imageDir.exists()) {
	// imageDir.mkdirs();
	// }
	// String format = null;
	// try {
	// format = imageFileName.split(".")[1];
	// } catch (Exception e) {
	// format = "jpeg";
	// }
	// try {
	// ImageIO.write(imge, format, new File(imagePath + imageFileName));
	// } catch (IOException e) {
	// e.printStackTrace();
	// logger.info("保存图片失败", e);
	// }
	// nowImageName = imageUrl.replace(temp[2], imageHost);
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.info("保存图片失败", e);
	// }
	// return nowImageName;
	// }

	// public BufferedImage saveImage(String imgUrl) {
	// BufferedImage img = null;
	// try {
	// if (imgUrl.startsWith("http:")) {
	// img = ImageIO.read(new URL(imgUrl));
	// } else {
	// img = ImageIO.read(new File(imgUrl));
	// }
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return img;
	// }

	// public static byte[] imageToByte(BufferedImage bi, String format)
	// throws IOException {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// ImageIO.write(bi, format, baos);
	// return baos.toByteArray();
	// }

	public String senderImage(String imageUrl, Platform platform) {
		String imageHost = platform.getFilters().get("imageAddress");

		// webrun/m,KLop)_ 123.56.196.62 21 端口
		try {
			saveImageByFtp(imageUrl, imageHost, "webrun", "m,KLop)_",
					"123.56.196.62", "/opt/webrun/tomcat/webapps/unbankImage");
			logger.info("发送给 123.56.196.62 图片成功           " + imageUrl);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("发送给 123.56.196.62 图片失败          " + imageUrl);
		}
		// 发送到xidaixx 122.112.1.72
		String url = imageHost.split(":")[0];
		return saveImageByFtp(imageUrl, imageHost, "xindaixx", "Windows2008",
				url, "");
		// return saveImage(imageUrl, imageHost);
	}

//	public static void main(String[] args) {
//		 new ImageSender()
//		 .saveImageByFtp(
//		 "http://10.0.2.35:8080/unbankImage/images/20150326/00c2eae6472afc899d48e2e2135318cc.png",
//		 "www.xindaixx.com:8080","webrun","m,KLop)_","123.56.196.62","/opt/webrun/tomcat/webapps/unbankImage");
//		 new ImageSender()
//		 .saveImageByFtp(
//		 "http://10.0.2.35:8080/unbankImage/images/20150326/00c2eae6472afc899d48e2e2135318cc.png",
//		 "www.xindaixx.com:8080","xindaixx","Windows2008","www.xindaixx.com","");
//	}

	private String saveImageByFtp(String imageUrl, String imageHost,
			String username, String password, String url, String basePath) {

		String nowImageName = null;
		try {
			int port = 21;
			String temp[] = imageUrl.split("/");
			String todayStr = temp[5];
			String path = "/images/" + todayStr + "/";
			String imageFileName = temp[temp.length - 1];
			String imagePath = "//10.0.2.35/" + path + imageFileName;
			File file = new File(imagePath);
			FileInputStream input = null;
			try {
				input = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			path = basePath + path;
			boolean istrue = new ImageSenderByftp().uploadFile(url, port,
					username, password, path, imageFileName, input);
			if (istrue) {
				nowImageName = imageUrl.replace(temp[2], imageHost);
			} else {
				nowImageName = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("保存图片失败            "+ url +"        " + imageUrl, e);
		}
		return nowImageName;

	}
}
