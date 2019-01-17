package com.yongchun.library.utils;

import java.io.File;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageUtil {
	private ImageUtil() {
//		mImageDisplay=new ImageLoaderDisplay();
		mImageDisplay=new GlideDisplay();
	}

	
	private static ImageUtil mInstance;
	
	public static ImageUtil get(){
		if(mInstance==null){
			synchronized (ImageUtil.class) {
				if(mInstance==null){
					mInstance=new ImageUtil();
				}
			}
		}
		return mInstance;
	}
	
	
	private ImageDisplay mImageDisplay;
	
	
	public void display(ImageView iv,String url,DisplayOptions options){
		mImageDisplay.display(iv, url, options);
	}
	
	public static class DisplayOptions{
		
		public static final int MODE_ANIMATE_DEFAULT=-1;
		public static final int MODE_ANIMATE_NOT=0;
		public static final int MODE_ANIMATE_YES=1;
		
		public int errorIcon=-1;
		
		public boolean isScale=false;
		public ScaleType scaleType;
		
		public float thumbnail=-1;
		
		public int animateMode=MODE_ANIMATE_DEFAULT;
		public int animate=-1;
	}
	
	
	
	public interface ImageDisplay{
		void display(ImageView iv,String url,DisplayOptions options);
	}
	
//	public class ImageLoaderDisplay implements ImageDisplay{
//
//		DisplayImageOptions.Builder builder;
//		DisplayImageOptions imageOptions;
//		@Override
//		public void display(ImageView iv, String url, DisplayOptions options) {
//			ImageLoader imageLoader = ImageLoader.getInstance();
//			if(imageOptions==null){
//				builder=new DisplayImageOptions.Builder();
//			}
//			builder=new DisplayImageOptions.Builder();
//			builder.showImageForEmptyUri(options.errorIcon);
//			builder.showImageOnFail(options.errorIcon);
//			builder.cacheInMemory(false);
//			builder.cacheOnDisc(false);
//			builder.bitmapConfig(Bitmap.Config.RGB_565);
//			builder.resetViewBeforeLoading(false);
//			if(imageOptions==null){
//				imageOptions=builder.build();
//			}
//			if(options.isScale){
//				iv.setScaleType(options.scaleType);
//			}
//			imageLoader.displayImage("file://"+url, iv,imageOptions );
//		}
//
//	}
	
	public class GlideDisplay implements ImageDisplay{

		@Override
		public void display(ImageView iv, String url, DisplayOptions options) {
			if(options!=null&&iv!=null&&url!=null){
				DrawableRequestBuilder<File> builder = Glide
						.with(iv.getContext())
						.load(new File(url))
						.placeholder(options.errorIcon)
						.error(options.errorIcon);
				
				if(options.thumbnail!=-1){
					builder.thumbnail(options.thumbnail);
				}
				
				if(options.animateMode==DisplayOptions.MODE_ANIMATE_NOT){
					builder.dontAnimate();
				}else if(options.animateMode==DisplayOptions.MODE_ANIMATE_NOT){
				}
				
				if(options.isScale){
					switch (options.scaleType) {
					case CENTER_CROP:
						builder.centerCrop();
						break;
					default:
						break;
					}
				}
				builder.into(iv);
			}
		}
	}
}
