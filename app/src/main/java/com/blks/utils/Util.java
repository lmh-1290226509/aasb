package com.blks.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	/**
	 * 密码
	 * 
	 * @param pwd
	 * @return
	 */
	public static boolean isPwd(String pwd) {
		int length = pwd.length();
		if (length >= 6 && length < 16) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 手机验证
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0,0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	/**
	 * 验证邮政编码
	 * 
	 * @param post
	 * @return
	 */
	public static boolean checkPost(String post) {
		if (post.matches("[1-9]\\d{5}(?!\\d)")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 邮箱验证
	 * 
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String email) {
		Pattern pattern = Pattern
				.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
		Matcher mc = pattern.matcher(email);
		return mc.matches();
	}

	/**
	 * 是否为视频频
	 * 
	 * @param suffix
	 * @return
	 */
	public static boolean fileIsVideo(String suffix) {
		String[] fileIsAudios = new String[] { ".3gp", ".avi", ".mp4", ".mpeg",
				".mpe", ".mpg4", ".rmvb", ".3gp" };
		for (String s : fileIsAudios) {
			if (suffix.toLowerCase().endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否为音频
	 * 
	 * @param suffix
	 * @return
	 */
	public static boolean fileIsAudio(String suffix) {
		String[] fileIsAudios = new String[] { ".amr", ".mp3", ".ogg", ".mp2",
				".m3u", ".m4a", ".m4b", ".m4p", ".wav", ".wma", ".wmv" };
		for (String s : fileIsAudios) {
			if (suffix.toLowerCase().endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否为图片
	 */
	public static boolean fileIsImage(String suffix) {
		String[] fileIsAudios = new String[] { ".jpg", ".png", ".jpeg" };
		for (String s : fileIsAudios) {
			if (suffix.toLowerCase().endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 保留index数量的小数
	 * 
	 * @param number
	 * @param index
	 * @return
	 */
	public static double roundNum(double number, int index) {
		double result = 0;
		double temp = Math.pow(10, index);
		result = Math.round(number * temp) / temp;
		return result;
	}

	/**
	 * 是否为null
	 * 
	 * @param str
	 * @return boolean
	 * @author luxf 2015-7-15 下午6:53:50
	 */
	public static boolean isNull(String str) {
		if ("".equals(str) || str == null) {
			return true;
		} else {
			return false;
		}

	}

	public static void moveTo(Context context, Class<? extends Activity> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
	}

	public static void moveTo(Context context, Class<? extends Activity> cls,
			String key, String value) {
		Intent intent = new Intent();
		intent.putExtra(key, value);
		intent.setClass(context, cls);
		context.startActivity(intent);
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

	public static String getDate_ss() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

}
