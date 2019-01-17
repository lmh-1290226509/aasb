package com.blks.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 工具类
 * 
 * @author luxf
 * 
 */
public class MD5ChangeUtile {
	/**
	 * md5 16位 加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String Md5_16(String plainText) {
		StringBuffer buf = new StringBuffer("");
		int i;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			System.out.println("result: " + buf.toString());// 32位的加密

			System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buf.toString().substring(8, 24);
	}

	/**
	 * md5 32位 加密
	 * 
	 * @param plainText
	 */
	public static String Md5_32(String plainText) {
		StringBuffer buf = new StringBuffer("");
		int i;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			System.out.println("result: " + buf.toString());// 32位的加密

			System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
}
