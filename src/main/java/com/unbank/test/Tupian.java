package com.unbank.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.unbank.distribute.sender.ImageSenderByftp;

public class Tupian {
 
	public static void main(String[] args) {
		   String imageUrl ="http://10.0.2.35:8080/unbankImage/images/20160312/000bdd0c9d83400bdef470fa2d599a6b.jpeg";
		String url = "10.0.3.100";
		int port = 22;
		String username = "hzuser";
		String password = "yinlianxin";
		String temp[] = imageUrl .split("/");
		String todayStr = temp[5];
		String path = "/images/" + todayStr + "/";
		String imageFileName = temp[temp.length - 1];
		
		String imagePath = "//10.0.2.35/"+path+ imageFileName;
		File file = new File(imagePath);
		FileInputStream input =null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean istrue = new ImageSenderByftp().uploadFile(url, port,
				username, password, "/home/hzuser/file/yt", imageFileName, input );
	}
	
//	public static void main(String[] args) {
//        String imageHost ="10.1.0.155";
//        String imageUrl ="http://10.0.2.35:8080/unbankImage/images/20160312/000bdd0c9d83400bdef470fa2d599a6b.jpeg";
//		//		String imageHost ="http://10.0.2.35:8080/unbankImage/images/20160303/74735bad4a4437c147f0e0ad22f25ca0.png";
//		String url = imageHost  .split(":")[0];
//		int port = 21;
//		String username = "ftpuser";
//		String password = "Ftp2016";
//		
//		String temp[] = imageUrl .split("/");
//		String todayStr = temp[5];
//		String path = "/images/" + todayStr + "/";
//		String imageFileName = temp[temp.length - 1];
//		
//		String imagePath = "//10.0.2.35/"+path+ imageFileName;
//		File file = new File(imagePath);
//		FileInputStream input =null;
//		try {
//			input = new FileInputStream(file);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		boolean istrue = new ImageSenderByftp().uploadFile(url, port,
//				username, password, "", imageFileName, input );
//	}
}
