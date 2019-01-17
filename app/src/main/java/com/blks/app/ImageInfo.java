package com.blks.app;

import com.blks.model.UploadFileModel;

/**
 * @author yujiangjiao
 * @date 下午2:46:34
 * @description 公司框架代码的照相机图片实体类
 */
public class ImageInfo {

	private boolean imgInfoUpload;

	// 图片文件
	private String localImage;
	// 上传后返回的图片的网络地址
	private UploadFileModel netImageModel;

	public boolean isImgInfoUpload() {
		return imgInfoUpload;
	}

	public void setImgInfoUpload(boolean imgInfoUpload) {
		this.imgInfoUpload = imgInfoUpload;
	}

	public String getLocalImage() {
		return localImage;
	}

	public void setLocalImage(String localImage) {
		this.localImage = localImage;
	}

	public UploadFileModel getNetImageModel() {
		return netImageModel;
	}

	public void setNetImageModel(UploadFileModel netImageModel) {
		this.netImageModel = netImageModel;
	}
}
