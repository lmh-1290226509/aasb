package com.blks.app;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;

import com.blks.antrscapp.R;
import com.blks.antrscapp.TaskDetailActivity;
import com.blks.antrscapp.WorkOrdersIndexActivity;
import com.blks.application.RoadSideCarApplication;
import com.blks.bdloc.NotificationUtils;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.WorkOrderModel;
import com.blks.utils.Constants;
import com.blks.utils.EventListener;
import com.blks.utils.LoginUtils;
import com.blks.utils.PermissionUtils;
import com.blks.utils.RomHelper;
import com.ddadai.basehttplibrary.HttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

public class BaseActivity extends FragmentActivity implements PermissionUtils.PermissionResultListener {

    protected Activity mThis;
    protected PermissionUtils permissionUtils;
    private CustomDialog dialog;
    private MediaPlayer mp;
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    protected PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private WifiManager.WifiLock wifiLock;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mThis=this;
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (RomHelper.isOppo()) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "antrscapp:MyWakelockTag");
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "antrscapp:MyWifiLockTag");
            }
        }
        EventBus.getDefault().register(this);
        if (needPermission()) {
            permissionUtils = new PermissionUtils(this);
        }
        initNotification();
	}

    @Override
    protected void onDestroy() {
        if ( EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (permissionUtils != null) {
            permissionUtils.Destroy();
        }

        stopPlayMedia();

        if (null != wakeLock && wakeLock.isHeld()) {
            wakeLock.release();
        }

        if (null != wifiLock && wifiLock.isHeld()) {
            wifiLock.release();
        }

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionUtils != null) {
            permissionUtils.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initNotification() {
        //设置后台定位
        //android8.0及以上使用NotificationUtils
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new NotificationUtils(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("蚂蚁救援", "正在后台定位");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(this);
            Intent nfIntent = new Intent(this, this.getClass());

            builder.setContentIntent(PendingIntent.
                    getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("蚂蚁救援") // 设置下拉列表里的标题
                    .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间
                    .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                    .setVibrate(new long[]{0})
                    .setSound(null);;

            if (Build.VERSION.SDK_INT <= 15) {
                notification = builder.getNotification();
            } else {
                notification = builder.build(); // 获取构建好的Notification
            }

        }
//        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
    }

    public static String currentName="";

    @Override
    protected void onResume() {
        super.onResume();
        currentName=getClass().getSimpleName();
        if(!isBackground&&orderModel!=null){

            final WorkOrderModel.DataListModel model = orderModel.DataList.get(0);

            if (orderModel.isShowDialog()) { //手动派工
                //更新工单推送与否状态
                setRscWOPushFlag(model.WO_NO);

                if (dialog != null) {
                    dialog.dismiss();
                }

                dialog = new CustomDialog(this, R.style.mystyle, new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.confirm_btn:// 确定
                                stopPlayMedia();
                                if (TextUtils.equals(model.RSC_STEP, "手工派单")) {
                                    gotoTaskDetailActivity(model.WO_NO);
                                } else {
                                    Intent intent = new Intent(mThis, WorkOrdersIndexActivity.class);
                                    intent.putExtra("_WO_NO", model.WO_NO);
                                    mThis.startActivity(intent);
                                }
                                orderModel=null;
                                dialog.dismiss();
                                break;
                            case R.id.cancel_btn:// 取消
                                stopPlayMedia();
                                orderModel=null;
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                dialog.SetCancelText("稍后查看")
                        .SetCanceledOnTouchOutside(false);

                if (TextUtils.equals("手工派单", model.RSC_STEP)) {
                    //用户状态更新
                    modifyUserState(Constants.UserStatus.BUSY);
                    dialog.SetMessage("你有一条"+model.RSC_STEP+"，请及时处理！")
                            .SetOkText("立即查看");
                } else {
                    dialog.SetMessage("你有一条"+model.RSC_STEP+"，请准时处理！")
                            .SetOkText("查看详情");
                }
                dialog.show();

            } else { //自动派工
                //用户状态更新
                modifyUserState(Constants.UserStatus.BUSY);
                gotoTaskDetailActivity(model.WO_NO);
                stopPlayMedia();
                orderModel=null;
            }

        }
    }
//是否后台
    private boolean isBackground=false;

    /**
     * app切换前后台的回掉
     * @param ib
     */
    @Subscribe
    public void isBackground(Boolean ib) {
        isBackground = ib;
        if (currentName.equals(this.getClass().getSimpleName())) {
            //当前在栈顶
            if (ib) {//后台
                RoadSideCarApplication.getInstance().getLocationService().enableLocInForeground(notification);
                if (null != wakeLock && !(wakeLock.isHeld())) {
                    wakeLock.acquire(60 * 60 * 1000);
                }
                if (null != wifiLock && !(wifiLock.isHeld())) {
                    wifiLock.acquire();
                }
            } else {//前台
                RoadSideCarApplication.getInstance().getLocationService().disableLocInForeground();
                if (null != wakeLock && wakeLock.isHeld()) {
                    wakeLock.release();
                }
                if (null != wifiLock && wifiLock.isHeld()) {
                    wifiLock.release();
                }
            }
        }

//        if (!ib) {//进入前台
//            stopPlayMedia();
//        }

    }
    WorkOrderModel orderModel;

    /**
     * 监听工单
     * @param orderModel
     */
    @Subscribe
    public void getWorkOrder(WorkOrderModel orderModel) {

        if (!currentName.equals(this.getClass().getSimpleName())) {
            //当前不在栈顶的不响应
            return;
        }

        starPlayMedia();//播放提示音
        if (!powerManager.isScreenOn() || isBackground) {
            this.orderModel=orderModel;
            //锁屏不弹dialog，不跳页面，进入后台不弹dialog，不跳页面
            return;
        }

        final WorkOrderModel.DataListModel model = orderModel.DataList.get(0);

        if (orderModel.isShowDialog()) { //手动派工
            //更新工单推送与否状态
            setRscWOPushFlag(model.WO_NO);

            if (dialog != null) {
                dialog.dismiss();
            }

            dialog = new CustomDialog(this, R.style.mystyle, new CustomDialog.CustomDialogListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.confirm_btn:// 确定
                            stopPlayMedia();
                            if (TextUtils.equals(model.RSC_STEP, "手工派单")) {
                                gotoTaskDetailActivity(model.WO_NO);
                            } else {
                                Intent intent = new Intent(mThis, WorkOrdersIndexActivity.class);
                                intent.putExtra("_WO_NO", model.WO_NO);
                                mThis.startActivity(intent);
                            }
                            dialog.dismiss();
                            break;
                        case R.id.cancel_btn:// 取消
                            stopPlayMedia();
                            dialog.dismiss();
                            break;
                    }
                }
            });
            dialog.SetCancelText("稍后查看")
            .SetCanceledOnTouchOutside(false);

            if (TextUtils.equals("手工派单", model.RSC_STEP)) {
                //用户状态更新
                modifyUserState(Constants.UserStatus.BUSY);
                dialog.SetMessage("你有一条"+model.RSC_STEP+"，请及时处理！")
                        .SetOkText("立即查看");
            } else {
                dialog.SetMessage("你有一条"+model.RSC_STEP+"，请准时处理！")
                        .SetOkText("查看详情");
            }
            dialog.show();

        } else { //自动派工
            //用户状态更新
            modifyUserState(Constants.UserStatus.BUSY);
            gotoTaskDetailActivity(model.WO_NO);
            stopPlayMedia();
        }

    }

    /**
     * 修改用户状态
     * @param state
     */
    protected void modifyUserState(String state) {
        //本地用户状态更新
        LoginUtils.setUserStatus(state);
        //服务器状态更新
        if (LoginUtils.getLoginModel() != null) {
            HttpUtils.get(HttpUri.UPDATE_CURR_STATUS)
                    .dialog(false)
                    .data("usrId", LoginUtils.getLoginModel().USR_ID)
                    .data("status", state)
                    .callBack(new JsonRequestCallBack(this) {
                        @Override
                        public void requestSuccess(String url, JSONObject jsonObject) {

                        }
                    })
                    .request();
        }
    }

    /**
     * 更新工单推送与否状态
     * @param WO_NO
     */
    protected void setRscWOPushFlag(String WO_NO) {
        HttpUtils.get(HttpUri.SET_RSCWO_PUSH_FLAG)
                .dialog(false)
                .data("param", WO_NO)
                .onlyKey("param")
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {

                    }
                })
                .request();

    }

    /**
     * 跳到任务处理页面
     * @param wo_no
     */
    private void gotoTaskDetailActivity(String wo_no) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(Constants._WO_NO, wo_no);
        startActivity(intent);
    }

    /**
     * 播放音频
     */
    private void starPlayMedia() {

        if (mp == null) {
            mp = MediaPlayer.create(mThis, R.raw.msg);
            mp.setLooping(true);
        }

        if (!mp.isPlaying()) {//播放中则跳过
            mp.start();
        }
    }

    private void stopPlayMedia() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
           mp.reset();
           mp.release();
           mp = null;
        }
    }


    /**
     * 打开activity * @param ActivityClass
     */
    public void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(this, ActivityClass);
        startActivity(intent);
    }

    /**
     * Find出来的View，自带防抖功能
     */
    protected <T extends View> T findClickView(int id) {

        T view = (T) findViewById(id);
        view.setOnClickListener(new EventListener(this));
        return view;
    }

    /**
     * 需要权限的页面重写
     * @return
     */
    protected boolean needPermission () {
        return false;
    }

    @Override
    public void AllowPermission(String[] permission) {

    }

    @Override
    public void ShowPermission(String[] permission) {

    }

    @Override
    public void DeniedPermission(String[] permission) {

    }

    @Override
    public void NeverAskPermission(String[] permission) {

    }
}
