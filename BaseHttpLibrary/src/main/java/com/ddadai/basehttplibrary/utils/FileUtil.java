package com.ddadai.basehttplibrary.utils;

import android.os.Environment;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.text.DecimalFormat;
//
//import android.graphics.Bitmap;
//import android.os.Environment;
//import android.os.StatFs;
//
///*
// * Author: Lucifer
// *
// * Created Date:2015-4-7
// * Copyright @ 2015 BU
// * Description: 文件工具类
// *
// * History:
// */
public class FileUtil {

    /**
     * 可用内存大小
     * @param fileSize
     * @return
     */
    public static boolean MemoryAvailable(long fileSize) {

        long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();

//		Log.d("TAG", "usableSpace= "+usableSpace +"\n fileSize= "+fileSize);

        if (usableSpace < fileSize) {
//			CustomApplication.getInstance().showToast("存储空间不足!");
            return false;
        }else
            return true;
    }

    // 读/写检查
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

//
//	public static String appFile = App.AppFilePath;// 默认app文件目录
//	public static final String baseFile = Environment
//			.getExternalStorageDirectory() + File.separator;
//	public static final String appBaseFile = baseFile + appFile
//			+ File.separator;
//	boolean sdCardExist = Environment.getExternalStorageState().equals(
//			android.os.Environment.MEDIA_MOUNTED);// 是否有存储设备
//
//	public FileUtil() throws SecurityException {
//		if (!sdCardExist)
//			throw new SecurityException("请插入外部SD存储卡");
//		File fileBase = new File(appBaseFile);
//		if (!fileBase.exists())
//			fileBase.mkdir();
//	}
//
//	/**
//	 * 创建一个文件目录
//	 *
//	 * @author Lucifer 2015-4-8 下午8:02:31
//	 * @return void
//	 */
//	public File creatFileDirectory(String path) {
//		File file = new File(appBaseFile + File.separator + path);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		return file;
//	}
//
//	/**
//	 * 创建一个文件
//	 *
//	 * @author Lucifer 2015-4-8 下午8:28:54
//	 * @param path
//	 * @param fileName
//	 * @return
//	 * @return File
//	 */
//	public File creatNewFile(String path, String fileName) {
//		File file = null;
//		creatFileDirectory(path);
//		file = new File(appBaseFile + File.separator + path + File.separator
//				+ fileName);
//		return file;
//	}
//
//	/**
//	 * 将图片写入当前文件中
//	 *
//	 * @author Lucifer 2015-4-8 下午8:38:17
//	 * @param photo
//	 * @param path
//	 * @param fileName
//	 * @return
//	 * @return File
//	 */
//	public File saveBitmapToFile(Bitmap photo, String path, String fileName) {
//		File file = null;
//		file = creatNewFile(path, fileName);
//		FileOutputStream fOut = null;
//		try {
//			fOut = new FileOutputStream(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		photo.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//		try {
//			fOut.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			fOut.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return file;
//	}
//
//	/**
//	 *
//	 * @author luxf 2015-5-26 下午2:37:43
//	 * @param path
//	 * @param fileName
//	 * @param inputStream
//	 * @return
//	 * @return File
//	 */
//	public File write2SDFormInput(String path, String fileName,
//			InputStream inputStream) {
//		// 创建文件
//		File file = creatNewFile(path, fileName);
//		OutputStream outputStream = null;
//		try {
//			// 创建输出流
//			outputStream = new FileOutputStream(file);
//			// 创建缓冲区
//			byte buffer[] = new byte[4 * 1024];
//			// 写入数据
//			while ((inputStream.read(buffer)) != -1) {
//				outputStream.write(buffer);
//			}
//			// 清空缓存
//			outputStream.flush();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				outputStream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return file;
//	}
//
//	/**
//	 * 获取文件大小
//	 *
//	 * @author Administrator 2015-6-14 下午8:22:02
//	 * @param file
//	 * @return
//	 * @throws Exception
//	 * @return long
//	 */
//	public long getFileSize(File file) throws Exception {
//		long size = 0;
//		File flist[] = file.listFiles();
//		for (int i = 0; i < flist.length; i++) {
//			if (flist[i].isDirectory()) {
//				size = size + getFileSize(flist[i]);
//			} else {
//				size = size + flist[i].length();
//			}
//		}
//		return size;
//	}
//
//	/**
//	 * 转换文件 大小
//	 *
//	 * @author Administrator 2015-6-14 下午8:22:14
//	 * @param fileS
//	 * @return
//	 * @return String
//	 */
//	public String FormetFileSize(long fileS) {// 转换文件大小
//		DecimalFormat df = new DecimalFormat("#.00");
//		String fileSizeString = "";
//		if (fileS == 0) {
//			fileSizeString = "0.0B";
//		} else if (fileS < 1024) {
//			fileSizeString = df.format((double) fileS) + "B";
//		} else if (fileS < 1048576) {
//			fileSizeString = df.format((double) fileS / 1024) + "K";
//		} else if (fileS < 1073741824) {
//			fileSizeString = df.format((double) fileS / 1048576) + "M";
//		} else {
//			fileSizeString = df.format((double) fileS / 1073741824) + "G";
//		}
//		return fileSizeString;
//	}
//
//	/**
//	 * 递归删除文件和文件夹
//	 *
//	 * @param file
//	 *            要删除的根目录
//	 */
//	public void RecursionDeleteFile(File file) {
//		if (file.isFile()) {
//			file.delete();
//			return;
//		}
//		if (file.isDirectory()) {
//			File[] childFile = file.listFiles();
//			if (childFile == null || childFile.length == 0) {
//				file.delete();
//				return;
//			}
//			for (File f : childFile) {
//				RecursionDeleteFile(f);
//			}
//			file.delete();
//		}
//	}
//
//	/**
//	 * 判断文件是否存在
//	 *
//	 * @author Administrator 2015-6-16 下午10:32:40
//	 * @param path
//	 * @param fileName
//	 * @return
//	 * @return boolean
//	 */
//	public static boolean isFileExistes(String path, String fileName) {
//		try {
//			File f = new File(path + fileName);
//			if (!f.exists()) {
//				return false;
//			}
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 判断SDCard是否可用
//	 *
//	 * @return
//	 */
//	public static boolean isSDCardEnable() {
//		return Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED);
//
//	}
//
//	/**
//	 * 获取SD卡路径
//	 *
//	 * @return
//	 */
//	public static String getSDCardPath() {
//		return Environment.getExternalStorageDirectory().getAbsolutePath()
//				+ File.separator;
//	}
//
//	/**
//	 * 获取SD卡的剩余容量 单位byte
//	 *
//	 * @return
//	 */
//	public static long getSDCardAllSize() {
//		if (isSDCardEnable()) {
//			StatFs stat = new StatFs(getSDCardPath());
//			// 获取空闲的数据块的数量
//			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
//			// 获取单个数据块的大小（byte）
//			long freeBlocks = stat.getAvailableBlocks();
//			return freeBlocks * availableBlocks;
//		}
//		return 0;
//	}
//
//	/**
//	 * 获取指定路径所在空间的剩余可用容量字节数，单位byte
//	 *
//	 * @param filePath
//	 * @return 容量字节 SDCard可用空间，内部存储可用空间
//	 */
//	public static long getFreeBytes(String filePath) {
//		// 如果是sd卡的下的路径，则获取sd卡可用容量
//		if (filePath.startsWith(getSDCardPath())) {
//			filePath = getSDCardPath();
//		} else {// 如果是内部存储的路径，则获取内存存储的可用容量
//			filePath = Environment.getDataDirectory().getAbsolutePath();
//		}
//		StatFs stat = new StatFs(filePath);
//		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
//		return stat.getBlockSize() * availableBlocks;
//	}
}
