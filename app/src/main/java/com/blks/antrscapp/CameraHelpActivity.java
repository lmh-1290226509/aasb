package com.blks.antrscapp;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.blks.adapter.GridPicAdapter;
import com.blks.app.BaseActivity;
import com.blks.app.BtnOnClickListenter;
import com.blks.app.ImageInfo;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.LogoModel;
import com.blks.model.UploadFileModel;
import com.blks.model.WorkOrderModel;
import com.blks.pop.PhotoPopupWindow;
import com.blks.utils.Constants;
import com.blks.utils.LoginUtils;
import com.blks.utils.LogoUtils;
import com.blks.utils.ParseUtil;
import com.blks.utils.SharePreferenceUtil;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.ddadai.basehttplibrary.utils.HttpCode;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.yongchun.library.ImageSelectorUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraHelpActivity extends BaseActivity implements
        BtnOnClickListenter, OnClickListener {

    private GridView gv_pic;
    private GridPicAdapter adapter;// 图片列表适配器
    public  ArrayList<ImageInfo> mImageInfoList;
    private Button btn_commit;
    private TextView tv_close;
    private Context context;
    ImageSelectorUtil imageSelectorUtil;

    private boolean isCancel = false;
    private WorkOrderModel.DataListModel workOrderData;
    private String svcType;
    private Button button1, button2;
    public static String trailerStatus = "拖车待出发";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera_help);
        context = this;
        workOrderData = (WorkOrderModel.DataListModel) getIntent().getSerializableExtra(Constants._MODEL);
        isCancel = getIntent().getBooleanExtra(Constants._KEY_B, false);
        initView();// 初始化数据
        updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTrailerStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
        adapter = null;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageInfo imageInfo = (ImageInfo) msg.obj;
            mImageInfoList.add(imageInfo);
            upload(imageInfo);
            adapter.setList(mImageInfoList);
        }
    };

    private void initView() {

        imageSelectorUtil = new ImageSelectorUtil(new ImageSelectorUtil.ImageSelectorCallBack() {
            @Override
            public void getImages(final List<String> images) {
                if (images != null && images.size() != 0) {

                    RoadSideCarApplication.getInstance().showToast("压缩中...");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ImageInfo imageInfo = new ImageInfo();
                            LogoModel logoModel = new LogoModel();
                            logoModel.address = RoadSideCarApplication.getInstance().getLocationService().bdLocation.getAddrStr();
                            logoModel.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            String s = LogoUtils.addLogo(mThis, images.get(0), logoModel);
                            imageInfo.setLocalImage(s);

                            Message msg = new Message();
                            msg.obj = imageInfo;
                            mHandler.sendMessage(msg);

                        }
                    }).start();

                }
            }
        });
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_close.setOnClickListener(this);
        mImageInfoList = new ArrayList<ImageInfo>();
        gv_pic = (GridView) findViewById(R.id.gv_pic);
        adapter = new GridPicAdapter(this, mImageInfoList, this);
        gv_pic.setAdapter(adapter);
        gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mImageInfoList.size() > 0 && position < mImageInfoList.size()) {
                    dialog(position);
                } else if (mImageInfoList.size() == 8) {
                    ToastUtil.showShort(getApplicationContext(), "最多只能上传8张图片");
                } else {
                    photoImage(position);
                }
            }
        });
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        btn_commit =  findClickView(R.id.btn_commit);
        button2.setOnClickListener(this);


        getPicture();
    }

    private void upload(final ImageInfo model) {
        HttpUtils.img().file(model.getLocalImage(),model.getLocalImage())
                .callBack(new JsonRequestCallBack(mThis) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        mImageInfoList.remove(model);
                        UploadFileModel uploadFileModel=new Gson().fromJson(jsonObject.toString(),UploadFileModel.class);
                        if (TextUtils.isEmpty(uploadFileModel.saveid)) {
                            adapter.setList(mImageInfoList);
                            ToastUtil.showShort(context, "saveid为空");
                        } else {
                            model.setNetImageModel(uploadFileModel);
                            mImageInfoList.add(model);
                        }

                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        if (HttpCode.NETWORK_ERROR.equals(response.code)) {
                            ToastUtil.showShort(context, response.msg);
                        } else {
                            ToastUtil.showShort(context, "照片上传失败，请重新选择");
                        }

                        mImageInfoList.remove(model);
//                        mImageInfoList.remove(mImageInfoList.indexOf(model));
                        adapter.setList(mImageInfoList);
                    }

                })
                .request();
    }

    /*
     * Dialog对话框提示用户删除操作 position为删除图片位置
     */
    private void dialog(final int position) {
        AlertDialog.Builder builder = new Builder(CameraHelpActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mImageInfoList.remove(position);
                adapter.setList(mImageInfoList);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 判断哪类事件
     */
    private void updateView() {
        if (workOrderData != null) {

            String goodsName = workOrderData.GOODS_NAME;
            findViewById(R.id.trailerLayout).setVisibility(View.GONE);

            if ("拖车牵引".equals(goodsName) || "拖车救援".equals(goodsName)) {
                svcType = "拖车";
                findViewById(R.id.trailerLayout).setVisibility(View.VISIBLE);
                trailerStatus = "拖车待出发";
            } else if ("人伤救援".equals(goodsName) || "困境救援".equals(goodsName)) {
                svcType = "困境";
            } else if ("取消有空驶".equals(goodsName)) {
                svcType = "取消有空驶";
            } else {
                svcType = "救援";
            }

            if (isCancel) {//接单后取消
                svcType = "取消有空驶";
            }

            if ("拖车出发".equals(workOrderData.RSC_STEP)) {
                trailerStatus = "拖车已出发";
                Intent intent = new Intent(context, TrailerHandleAct.class);
                intent.putExtra(Constants._KEY_B, true);
                intent.putExtra(Constants._MODEL, workOrderData);
                startActivity(intent);
            } else if ("拖车到达".equals(workOrderData.RSC_STEP)) {
                trailerStatus = "拖车已到达";
            }

            updateTrailerStatus();//更新拖车按钮状态
        }

    }

    /**
     * 拖车状态更新
     */
    private void updateTrailerStatus() {
        button1.setText(trailerStatus);

        if ("拖车已到达".equals(trailerStatus)) {
            button2.setEnabled(false);
        } else {
            button2.setEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera_help, menu);
        return true;
    }

    PhotoPopupWindow photoPopupWindow;
    /**
     * 上传图片
     */
    private void photoImage(int position) {
        if (photoPopupWindow == null) {
            photoPopupWindow = new PhotoPopupWindow(this);
            photoPopupWindow.setPhotoListener(new PhotoPopupWindow.PhotoListener() {
                @Override
                public void photo() {
                    imageSelectorUtil.selectSingleWithCamera(mThis, false);
                    photoPopupWindow.dismiss();
                }
            });
        }

        if (photoPopupWindow.isShowing()) {
            return;
        }

        photoPopupWindow.setImageHintByPosition(position);
        photoPopupWindow.showAtLocation(findViewById(R.id.rootView),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0);
    }

    /**
     * 删除图片
     */
    @Override
    public void onClickListener(int position, int status, String id,
                                String storeId) {
        mImageInfoList.remove(position);
        adapter.setList(mImageInfoList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSelectorUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_commit:

                if ("拖车".equals(svcType)) {

                    if ("拖车已到达".equals(trailerStatus)) {
                        if (mImageInfoList.size() < 4) {
                            ToastUtil.showPosition(context, "至少要传4张照片");
                        } else {
                            requestSaveInfo();
                        }
                    } else {
                        ToastUtil.showPosition(context, "请确认拖车是否到达目的地！");
                    }

                } else if ("困境".equals(svcType)) {
                    if (mImageInfoList.size() < 4) {
                        ToastUtil.showPosition(context, "至少要传4张照片");
                    } else {
                        requestSaveInfo();
                    }
                } else if ("取消有空驶".equals(svcType)) {
                    if (mImageInfoList.size() < 2) {
                        ToastUtil.showPosition(context, "至少要传2张照片");
                    } else {
                        requestSaveInfo();
                    }
                } else {
                    if (mImageInfoList.size() < 3) {
                        ToastUtil.showPosition(context, "至少要传3张照片");
                    } else {
                        requestSaveInfo();
                    }
                }

                break;
            case R.id.tv_close:
                savePicture();
//                intent = new Intent(context, HomePagerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                finish();
                break;
            case R.id.button2:
                intent = new Intent(context, TrailerHandleAct.class);

                if ("拖车已出发".equals(trailerStatus)) {
                    intent.putExtra(Constants._KEY_B, true);
                } else {
                    intent.putExtra(Constants._KEY_B, false);
                }
                intent.putExtra(Constants._MODEL, workOrderData);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void requestSaveInfo(){
        JSONArray ja=new JSONArray();
        for (int i = 0; i <mImageInfoList.size() ; i++) {
            JSONObject jsonObject=new JSONObject();
            ImageInfo imageInfo = mImageInfoList.get(i);

            try {
                jsonObject.put("uri",imageInfo.getLocalImage());
                jsonObject.put("upload",true);
                jsonObject.put("saveid",imageInfo.getNetImageModel().saveid);
                jsonObject.put("folderName",imageInfo.getNetImageModel().folderName);
            }catch (Exception e){
                e.printStackTrace();
            }
            ja.put(jsonObject);
        }
//取消后台接口请求
        OkGo.getInstance().cancelTag("background");

        HttpUtils.postImageInfo()
                .data("woNo", workOrderData.WO_NO)
                .data("photos",ja)
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .data("usrOrgId",LoginUtils.getLoginModel().ORG_NO)
                .data("usrName",LoginUtils.getLoginModel().USR_NAME)
                .data("usrRealName",LoginUtils.getLoginModel().USR_REAL_NAME)
                .data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
                .mulKey("woNo")
                .callBack(new JsonRequestCallBack(mThis) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {

                        btn_commit.setEnabled(true);
                        Intent intent = new Intent(CameraHelpActivity.this, ClientSureActivity.class);
                        intent.putExtra(Constants._WO_NO, workOrderData.WO_NO);
                        intent.putExtra(Constants._KEY_STR, mImageInfoList.size()+"");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        btn_commit.setEnabled(true);

                        if (HttpCode.NETWORK_ERROR.equals(response.code)) {
                            ToastUtil.showShort(context,response.msg);
                            return;
                        }
                        ToastUtil.showShort(CameraHelpActivity.this, "照片信息提交失败！");

                    }
                })
                .request();

    }


    /**
     * GET请求限制长度，则临时循环提交
     */

//    List<Integer> checkList = new ArrayList<>();
//
//    private void requestSaveInfo(){
//        checkList.clear();
//        btn_commit.setEnabled(false);
//
//        for (int i = 0; i <mImageInfoList.size() ; i++) {
//            JSONArray ja=new JSONArray();
//            JSONObject jsonObject=new JSONObject();
//            ImageInfo imageInfo = mImageInfoList.get(i);
//
//            if (imageInfo.isImgInfoUpload()) {
//                checkList.add(1);//成功添加一个
//                continue;
//            }
//
//            try {
//                jsonObject.put("uri",imageInfo.getLocalImage());
//                jsonObject.put("upload",true);
//                jsonObject.put("saveid",imageInfo.getNetImageModel().saveid);
//                jsonObject.put("folderName",imageInfo.getNetImageModel().folderName);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            ja.put(jsonObject);
//
//            final int finalI = i;
//            HttpUtils.get(HttpUri.SAVE_RSC_PHOTO_INFO)
//                    .data("woNo", workOrderData.WO_NO)
//                    .data("photos",ja)
//                    .data("hasMore", i < (mImageInfoList.size() - 1))
//                    .data("serialNumber", String.valueOf(i+1))
//                    .data("usrId", LoginUtils.getLoginModel().USR_ID)
//                    .data("usrOrgId",LoginUtils.getLoginModel().ORG_NO)
//                    .data("usrName",LoginUtils.getLoginModel().USR_NAME)
//                    .data("usrRealName",LoginUtils.getLoginModel().USR_REAL_NAME)
//                    .data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
//                    .mulKey("woNo")
//                    .callBack(new JsonRequestCallBack(mThis) {
//                        @Override
//                        public void requestSuccess(String url, JSONObject jsonObject) {
//                            checkList.add(1);//成功添加一个
//
//                            mImageInfoList.get(finalI).setImgInfoUpload(true);
//
//                            btn_commit.setEnabled(true);
//                            if (checkList.size() == mImageInfoList.size()) {
//                                Intent intent = new Intent(CameraHelpActivity.this, ClientSureActivity.class);
//                                intent.putExtra(Constants._WO_NO, workOrderData.WO_NO);
//                                intent.putExtra(Constants._KEY_STR, mImageInfoList.size()+"");
//                                startActivity(intent);
//                                finish();
//                            }
//
//
//                        }
//
//                        @Override
//                        public void requestFail(String url, Response_<JSONObject> response) {
//                            super.requestFail(url, response);
//                            btn_commit.setEnabled(true);
////                            mImageInfoList.get(finalI).setImgInfoUpload(false);
//
//                            if (HttpCode.NETWORK_ERROR.equals(response.code)) {
//                                ToastUtil.showShort(context,response.msg);
//                                return;
//                            }
//
//                            ToastUtil.showShort(CameraHelpActivity.this, "照片信息提交失败！");
//
//                        }
//                    })
//                    .request();
//        }

//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePicture();
    }

    /**
     * 中途关闭保存图片
     */
    private void savePicture() {

        if (mImageInfoList.size() > 0) {
            String imageString =  ParseUtil.List2Json(mImageInfoList).toString();

            if (!TextUtils.isEmpty(workOrderData.WO_NO)) {
                SharePreferenceUtil.put(this, workOrderData.WO_NO,
                        imageString);
            }

        }

    }

    /**
     * 查询本地是否有图片
     */
    private void getPicture() {

        if (TextUtils.isEmpty(workOrderData.WO_NO)) {
            return;
        }

        String imgString = String.valueOf(SharePreferenceUtil.get(this, workOrderData.WO_NO, ""));

        if (!TextUtils.isEmpty(imgString)) {
            mImageInfoList.clear();
            List<ImageInfo> list = ParseUtil.String2List(imgString, ImageInfo.class);
            mImageInfoList.addAll(list);
            adapter.setList(mImageInfoList);

            SharePreferenceUtil.remove(this, workOrderData.WO_NO);
        }

    }

}
