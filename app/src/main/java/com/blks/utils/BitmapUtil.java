package com.blks.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

/*
 * Author: luxf
 * Created Date:2015-4-1
 * Copyright @ 2015 BU
 * Description: 图片工具转换
 *
 * History:
 */
public class BitmapUtil {

	/**
	 * byte[] → Bitmap
	 * 
	 * @author luxf 2015-4-1 下午5:47:49
	 * @param b
	 * @return
	 * @return Bitmap
	 */
	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * Bitmap缩放
	 * 
	 * @author luxf 2015-4-1 下午5:48:10
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 * @return Bitmap
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, double width, double height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * 将Drawable转化为Bitmap
	 * 
	 * @author luxf 2015-4-1 下午5:48:34
	 * @param drawable
	 * @return
	 * @return Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 获得圆角图片
	 * 
	 * @author luxf 2015-4-1 下午5:48:54
	 * @param bitmap
	 * @param roundPx
	 * @return
	 * @return Bitmap
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获得带倒影的图片
	 * 
	 * @author luxf 2015-4-1 下午5:49:52
	 * @param bitmap
	 * @return
	 * @return Bitmap
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * Drawable缩放
	 * 
	 * @author luxf 2015-4-1 下午5:50:21
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 * @return Drawable
	 */
	@SuppressWarnings("deprecation")
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		// drawable转换成bitmap
		Bitmap oldbmp = drawableToBitmap(drawable);
		// 创建操作图片用的Matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		// 设置缩放比例
		matrix.postScale(sx, sy);
		// 建立新的bitmap，其内容是对原bitmap的缩放后的图
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * 将byte[]转换成InputStream
	 * 
	 * @author luxf 2015-4-1 下午5:52:41
	 * @param b
	 * @return
	 * @return InputStream
	 */
	public InputStream Byte2InputStream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}

	/**
	 * 将InputStream转换成byte[]
	 * 
	 * @author luxf 2015-4-1 下午5:53:15
	 * @param inStream
	 * @return
	 * @return byte[]
	 */
	public static final byte[] InputStream2Bytes1(InputStream inStream) {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		try {
			while ((rc = inStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	/**
	 * 将InputStream转换成byte[]
	 * 
	 * @author luxf 2015-4-1 下午5:53:40
	 * @param is
	 * @return
	 * @return byte[]
	 */
	public byte[] InputStream2Bytes2(InputStream is) {
		String str = "";
		byte[] readByte = new byte[1024];
		@SuppressWarnings("unused")
		int readCount = -1;
		try {
			while ((readCount = is.read(readByte, 0, 1024)) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将Bitmap转换成InputStream
	 * 
	 * @author luxf 2015-4-1 下午5:54:12
	 * @param bm
	 * @param quality
	 * @return
	 * @return InputStream
	 */
	public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 将InputStream转换成Bitmap
	 * 
	 * @author luxf 2015-4-1 下午5:54:27
	 * @param is
	 * @return
	 * @return Bitmap
	 */
	public Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * Drawable转换成InputStream
	 * 
	 * @author luxf 2015-4-1 下午5:54:40
	 * @param d
	 * @return
	 * @return InputStream
	 */
	public InputStream Drawable2InputStream(Drawable d) {
		Bitmap bitmap = this.drawable2Bitmap(d);
		return this.Bitmap2InputStream(bitmap, 0);
	}

	/**
	 * InputStream转换成Drawable
	 * 
	 * @author luxf 2015-4-1 下午5:54:59
	 * @param is
	 * @return
	 * @return Drawable
	 */
	public Drawable InputStream2Drawable(InputStream is) {
		Bitmap bitmap = this.InputStream2Bitmap(is);
		return this.bitmap2Drawable(bitmap);
	}

	/**
	 * Drawable转换成byte[]
	 * 
	 * @author luxf 2015-4-1 下午5:55:18
	 * @param d
	 * @return
	 * @return byte[]
	 */
	public byte[] Drawable2Bytes(Drawable d) {
		Bitmap bitmap = this.drawable2Bitmap(d);
		return this.Bitmap2Bytes(bitmap);
	}

	/**
	 * byte[]转换成Drawable
	 * 
	 * @author luxf 2015-4-1 下午5:55:33
	 * @param b
	 * @return
	 * @return Drawable
	 */
	public Drawable Bytes2Drawable(byte[] b) {
		Bitmap bitmap = this.Bytes2Bitmap(b);
		return this.bitmap2Drawable(bitmap);
	}

	/**
	 * Bitmap转换成byte[]
	 * 
	 * @author luxf 2015-4-1 下午5:55:48
	 * @param bm
	 * @return
	 * @return byte[]
	 */
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[]转换成Bitmap
	 * 
	 * @author luxf 2015-4-1 下午5:55:59
	 * @param b
	 * @return
	 * @return Bitmap
	 */
	public Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	/**
	 * Drawable转换成Bitmap
	 * 
	 * @author luxf 2015-4-1 下午5:56:12
	 * @param drawable
	 * @return
	 * @return Bitmap
	 */
	public Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap转换成Drawable
	 * 
	 * @author luxf 2015-4-1 下午5:56:25
	 * @param bitmap
	 * @return
	 * @return Drawable
	 */
	public Drawable bitmap2Drawable(Bitmap bitmap) {
		@SuppressWarnings("deprecation")
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		Drawable d = (Drawable) bd;
		return d;
	}

	/**
	 * 
	 * @param bitmap
	 *            原图
	 * @param edgeLength
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg > edgeLength && heightOrg > edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
					.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}

	/**
	 * 
	 * @param bitmap
	 *            原图
	 * @param width
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int width,
			int height) {
		if (null == bitmap || width <= 0 || height <= 0) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();
		if (widthOrg < width)
			heightOrg = heightOrg * width / widthOrg;
		int scaledHeight = height > heightOrg ? heightOrg : height;
		int topY = 0;
		if (height < heightOrg) {
			topY = (heightOrg - height) / 2;
		}
		try {
			result = Bitmap.createBitmap(bitmap, 0, topY, width, scaledHeight);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 改变尺寸
	 * 
	 * @author Lucifer 2015-5-6 下午10:34:22
	 * @param loadedImage
	 * @param width
	 * @param height
	 * @return
	 * @return Bitmap
	 */
	public static Bitmap getChangeBitMap(Bitmap loadedImage, int width,
			int height) {
		// TODO Auto-generated method stub
		System.out.println("log-" + loadedImage.getWidth() + "he-"
				+ loadedImage.getHeight());
		double bl = loadedImage.getWidth() / loadedImage.getHeight();
		if (loadedImage.getWidth() < width) {
			int cHeight = (int) (width / bl);
			loadedImage = BitmapUtil.zoomBitmap(loadedImage, width, cHeight);
		}
		if (loadedImage.getHeight() < height) {
			int cWidth = (int) (height * bl);
			loadedImage = BitmapUtil.zoomBitmap(loadedImage, cWidth, height);
		}
		System.out.println("log" + loadedImage.getWidth() + "he"
				+ loadedImage.getHeight());
		return loadedImage;
	}

	/**
	 * 1.获取ByteArrayOutputStream对象
	 * 
	 * 2.调用bitmap压缩方法，将压缩的内容存在baos中
	 * 
	 * 3.设置每次压缩的步数
	 * 
	 * 4.进行循环遍历baos的lenth是否大于100kb，如果大于清空baos，并且继续压缩image
	 * 
	 * 5.吧压缩以后的boas存到bytearrayinputstream中
	 * 
	 * 6.最后通过bitmapfactory生成bitmap值返回
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Log.d("TAG", "Srr" + baos.toByteArray().length / 1024);

		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		int quality = 100;
		while (baos.toByteArray().length / 1024 > 100) {
			Log.d("TAG", "S" + baos.toByteArray().length / 1024);
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			quality -= 10;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		Bitmap bitmapImage = BitmapFactory.decodeStream(bais, null, null);

		return bitmapImage;
	}

	/**
	 * 根据图片路径压缩
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = ScreenUtils.getScreenHeight();// 这里设置高度为800f
		float ww = ScreenUtils.getScreenWidth();// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 压缩
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = ScreenUtils.getScreenHeight();// 这里设置高度为800f
		float ww = ScreenUtils.getScreenWidth();// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 通过Base32将Bitmap转换成Base64字符串
	 *
	 * @param bit
	 * @return
	 */
	public static String BitmapBase64(Bitmap bit) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bit.compress(Bitmap.CompressFormat.PNG, 40, bos);// 参数100表示不压缩
		byte[] bytes = bos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

}
