package com.blks.antrscapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blks.app.BaseActivity;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.LoginModel;
import com.blks.pop.LogoutPopupWindow;
import com.blks.utils.Constants;
import com.blks.utils.LoginUtils;
import com.blks.utils.SharePreferenceUtil;
import com.blks.utils.ToastUtil;
import com.blks.customer.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.yongchun.library.ImageSelectorUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;

/**
 * @author shaoshuai 我的资料
 */
public class PersonActivity extends BaseActivity implements OnClickListener {

    private Context context;
    private ImageView person_back;
    private CircleImageView img_ss_head;
    private Button btn_loginOut;
    private LogoutPopupWindow menuWindow;// 自定义的弹出框类
    private TextView userNameTv;
    private TextView superNameTv;
    private TextView nameTv;
    private TextView statusTv;
    private TextView superName1Tv;
    private TextView phoneTv;
    private TextView createTimeTv;
    private ImageSelectorUtil imageSelectorUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_person);
        context = this;
        initView();

    }

    private void initView() {
        person_back = (ImageView) findViewById(R.id.person_back);
        person_back.setOnClickListener(this);
        img_ss_head = (CircleImageView) findViewById(R.id.img_ss_head);
        img_ss_head.setOnClickListener(this);
        btn_loginOut = (Button) findViewById(R.id.btn_loginOut);
        btn_loginOut.setOnClickListener(this);


        userNameTv = (TextView) findViewById(R.id.userNameTv);
        superNameTv = (TextView) findViewById(R.id.superNameTv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        statusTv = (TextView) findViewById(R.id.statusTv);
        superName1Tv = (TextView) findViewById(R.id.superName1Tv);
        phoneTv = (TextView) findViewById(R.id.phoneTv);
        createTimeTv = (TextView) findViewById(R.id.createTimeTv);


        LoginModel.DataListModel loginModel = LoginUtils.getLoginModel();
        if (loginModel != null) {
            userNameTv.setText(loginModel.USR_NAME);
            superNameTv.setText(loginModel.SUP_NAME);
            superName1Tv.setText(loginModel.SUP_NAME);
            nameTv.setText(loginModel.EMP_NAME);
            phoneTv.setText(loginModel.EMP_PHONE);
            createTimeTv.setText(loginModel.CREATE_DATE);
        }

        imageSelectorUtil = new ImageSelectorUtil(new ImageSelectorUtil.ImageSelectorCallBack() {
            @Override
            public void getImages(List<String> images) {
                if (images != null && images.size() != 0) {
                    String dir= Environment.getExternalStorageDirectory().getAbsolutePath()+"/resapp/img";
                    File dirFile=new File(dir);
                    if(!dirFile.exists()){
                        dirFile.mkdirs();
                    }

                    File oldFile = new File(images.get(0));
                    File compressFile = null;
                    try {
                        compressFile = new Compressor(mThis)
                                .setDestinationDirectoryPath(dir)
                                .compressToFile(oldFile, System.currentTimeMillis()+".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (compressFile != null) {
                        oldFile.delete();
                        uploadHeadImg(compressFile.getAbsolutePath());
                    } else {
                        uploadHeadImg(images.get(0));
                    }

                }
            }
        });

        String urlImg = RoadSideCarApplication.getInstance().HeadUrl();
        if (urlImg != null) {

            Glide.with(this)
                    .load(urlImg)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            img_ss_head.setBitmap(resource);
                        }
                    });
        }

    }

    /**
     * 上传头像
     * @param path
     */
    private void uploadHeadImg(String path) {
        if (LoginUtils.getLoginModel() != null) {
            HttpUtils.headImg().file(path, path)
                    .callBack(new JsonRequestCallBack(mThis) {
                        @Override
                        public void requestSuccess(String url, JSONObject jsonObject) {
                            String saveName = jsonObject.optString("savename");

                            updateHeadImgInfo(saveName);

                        }

                        @Override
                        public void requestFail(String url, Response_<JSONObject> response) {
                            super.requestFail(url, response);
                        }
                    })
                    .request();

        }
    }

    /**
     * 头像信息更新
     * @param saveName
     */
    private void updateHeadImgInfo(final String saveName) {
        if (LoginUtils.getLoginModel() != null) {
            HttpUtils.get(HttpUri.UPDATE_RSC_WORKER_PIC_URL)
                    .data("usrId", LoginUtils.getLoginModel().USR_ID)
                    .data("url", saveName)
                    .mulKey("usrId")
                    .callBack(new JsonRequestCallBack(mThis) {
                        @Override
                        public void requestSuccess(String url, JSONObject jsonObject) {
                            LoginUtils.getLoginModel().USR_AVATAR_PATH = saveName;
                            updateHead();
                        }

                        @Override
                        public void requestFail(String url, Response_<JSONObject> response) {
                            super.requestFail(url, response);
                            ToastUtil.showLong(mThis, response.msg);
                        }
                    })
                    .request();
        }
    }

    private void updateHead() {
        Glide.with(this)
                .load(RoadSideCarApplication.getInstance().HeadUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        img_ss_head.setBitmap(resource);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userStatus = LoginUtils.getUserStatus();
        if (userStatus != null) {
            //0-离线/1-在线/2-待命/3-离开/4-繁忙
            if ("0".equals(userStatus)) {
                statusTv.setText("离线");
            } else if ("1".equals(userStatus)) {
                statusTv.setText("在线");
            } else if ("2".equals(userStatus)) {
                statusTv.setText("待命");
            } else if ("3".equals(userStatus)) {
                statusTv.setText("离开");
            } else if ("4".equals(userStatus)) {
                statusTv.setText("繁忙");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSelectorUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.person, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_back:
                finish();
                break;
            case R.id.btn_loginOut:// 注销
                menuWindow = new LogoutPopupWindow(context, itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(
                        ((Activity) context).findViewById(R.id.person),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.img_ss_head:
                imageSelectorUtil.selectSingleWithCamera(mThis, true);
                break;
            default:
                break;
        }
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_pick_photo:
                    SharePreferenceUtil.clear(context);

                    LoginUtils.setLoginModel(null);//清空登录信息
                    LoginUtils.setLoginStatus(Constants.LoginStatus.OFFLINE);//登录状态
                    modifyUserState(Constants.UserStatus.OFFLINE);//用户状态
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
