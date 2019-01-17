package com.yongchun.library;

import android.app.Activity;
import android.content.Intent;

import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageSelectorUtil {

	public static final int MODE_MULTIPLE=ImageSelectorActivity.MODE_MULTIPLE;
	public static final int MODE_SINGLE=ImageSelectorActivity.MODE_SINGLE;
	public static final int MODE_ONLY_CAMERA=ImageSelectorActivity.MODE_ONLY_CAMERA;

	public ImageSelectorUtil(ImageSelectorCallBack cb) {
		mCallBack=cb;
	}
	
	public ImageSelectorCallBack mCallBack;
	
	
	public void selectImage(Activity act,int mode,int maxSelectNum,boolean isCamera,boolean isPreview,boolean isCrop){
		ImageSelectorActivity.start(act, maxSelectNum,mode, isCamera,isPreview,isCrop);
	}


	public void selectOnlyCamera(Activity act,boolean isCrop){
		selectImage(act, MODE_ONLY_CAMERA, 1, true, false, isCrop);
	}
	
	public void selectSingleNoCamera(Activity act,boolean isCrop){
		selectImage(act, MODE_SINGLE, 1, false, false, isCrop);
	}
	
	public void selectMulNoCamera(Activity act,int maxSelectNum){
		selectImage(act, MODE_MULTIPLE, maxSelectNum, false, true, false);
	}
	public void selectSingleWithCamera(Activity act,boolean isCrop){
		selectImage(act, MODE_SINGLE, 1, true, false, isCrop);
	}
	
	public void selectMulWithCamera(Activity act,int maxSelectNum){
		selectImage(act, MODE_MULTIPLE, maxSelectNum, true, true, false);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == Activity.RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE&& mCallBack!=null){
            @SuppressWarnings("unchecked")
			ArrayList<String> images =  (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            mCallBack.getImages(images);
        }
	}
	
	public  interface ImageSelectorCallBack{
		void getImages(List<String> images);
	}
}
