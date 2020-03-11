package com.be.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileUtil {

	/**
	 * 判断是否存在，不存在则创建目录
	 * 
	 * @param path
	 * @return
	 */
	public static void isexitsPath(String path) {
		File file = new File(path);
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
	}

	// 获取文件夹下数量
	public static int getFileCount(String strFile) {
		isexitsPath(strFile);
		File file = new File(strFile);
		File[] files = file.listFiles();
		return files.length;
	}

	// 判断文件是否存在
	public static boolean isExistsFile(String strFile) {
		try {
			File f = new File(strFile);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// 读取文件内容
	public static String readToString(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		fi.close();
		return buffer;
	}

	/**
	 * 获得类的基路径，打成jar包也可以正确获得路径
	 * 
	 * @return
	 */
	public static String getBasePath() {
		/*
		 * /D:/zhao/Documents/NetBeansProjects/docCompare/build/classes/
		 * /D:/zhao/Documents/NetBeansProjects/docCompare/dist/bundles/
		 * docCompare/app/docCompare.jar
		 */
		String filePath = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();

		if (filePath.endsWith(".jar")) {
			filePath = filePath.substring(0, filePath.lastIndexOf("/"));
			try {
				filePath = URLDecoder.decode(filePath, "UTF-8"); // 解决路径中有空格%20的问题
			} catch (UnsupportedEncodingException ex) {

			}

		}
		File file = new File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}

	//文字转Unicode码
	public static String gbEncoding(final String gbString) { // gbString = "测试"
		char[] utfBytes = gbString.toCharArray(); // utfBytes = [测, 试]
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]); // 转换为16进制整型字符串
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		System.out.println("unicodeBytes is: " + unicodeBytes);
		return unicodeBytes;
	}
}
