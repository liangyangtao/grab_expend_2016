package com.unbank.distribute.sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.unbank.mina.clent.TLSClinet;

public class ImageSenderByftp {
	private static Log logger = LogFactory.getLog(ImageSenderByftp.class);

	public static boolean uploadFile(String url, int port, String username,
			String password, String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			ftp.login(username, password);// 登录
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			ftp.setControlKeepAliveTimeout(30000);
			ftp.setControlKeepAliveReplyTimeout(30000);
			ftp.addProtocolCommandListener(new PrintCommandListener(
					new PrintWriter(System.out), true));
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.makeDirectory(path);
			boolean isChangeWorkingDirectory = ftp.changeWorkingDirectory(path);
			if (isChangeWorkingDirectory) {
				ftp.storeFile(filename, input);
				input.close();
				ftp.logout();
				success = true;
			}
		} catch (Exception e) {
			success = false;
			logger.info("发给信贷审查服务器失败", e);
			TLSClinet tlsClinet = TLSClinet.getInstance();
			tlsClinet.getFuture().getSession()
					.write("发给信贷审查图片服务器失败     " + e.getMessage());
			tlsClinet.getFuture().awaitUninterruptibly();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	public static void main(String[] args) throws Exception {
		File file = new File("ffeede08b784c7eaf02d59f75e598366.png");
		FileInputStream input = new FileInputStream(file);
		System.out.println(uploadFile("www.xindaixx.com", 21, "xindaixx",
				"Windows2008", "/images/", file.getName(), input));
		// System.out.println(uploadFile("10.0.2.26", 21, "ftpUser", "ftpuser",
		// "/ftpup/", file.getName(), input));

	}
}
