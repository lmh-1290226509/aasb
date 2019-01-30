package com.blks.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.WorkOrderModel;
import com.blks.utils.Constants;
import com.blks.utils.LoginUtils;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.ddadai.basehttplibrary.utils.ContentType;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.blks.utils.LoginUtils.isNetwork;

public class TaskService extends Service {
    private TimerTask timerTask;
    private Timer timer;
    private int askSkip = 0;
    private int getStatusFlag = 0;

//    private Handler handler=new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            requestTask();
//            return false;
//        }
//    });

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(timer!=null){
            timer.cancel();

        }
        timer=new Timer();
        if(timerTask!=null){
            timerTask.cancel();

        }
        timerTask=new TimerTask() {
            @Override
            public void run() {
//                handler.sendEmptyMessage(3);
                requestTask();
            }
        };

        timer.schedule(timerTask,0,3*1000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        if(timerTask!=null){
            timerTask.cancel();
            timerTask=null;
        }

        startService(new Intent(getApplicationContext(), TaskService.class));

    }

    private void requestTask(){
        if(!isNetwork || LoginUtils.getLoginModel()==null){
            return;
        }

        if (TextUtils.equals(Constants.LoginStatus.ONLINE, LoginUtils.getLoginStatus())
                && ++getStatusFlag >= 2) {
            getStatusFlag = 0;
            //获取用户状态并比对
            getUserCurrStatus();
        }

        if (TextUtils.equals(Constants.LoginStatus.ONLINE, LoginUtils.getLoginStatus())
                && ++askSkip >= 15 ) {//  手动派工   45s一次
            askSkip = 0;

            HttpUtils.get(HttpUri.LISTEN_MUANUA_RSC_WO)
                    .dialog(false)
                    .data("param",LoginUtils.getLoginModel().USR_ID)
                    .onlyKey("param")
                    .priority(5)
                    .tag("background")
                    .callBack(new JsonRequestCallBack(null) {
                        @Override
                        public void requestSuccess(String url, JSONObject jsonObject) {
                            WorkOrderModel orderModel = new Gson().fromJson(jsonObject.toString(), WorkOrderModel.class);
                            orderModel.setShowDialog(true);

                            EventBus.getDefault().post(orderModel);

                        }
                    })
                    .request();
        }

        if (TextUtils.equals(Constants.LoginStatus.ONLINE, LoginUtils.getLoginStatus()) &&
                TextUtils.equals(Constants.UserStatus.READY, LoginUtils.getUserStatus()) ) {//待命中  自动派工

                HttpUtils.get(HttpUri.LISTEN_AUTO_RSC_WO)
                        .dialog(false)
                        .data("param",LoginUtils.getLoginModel().USR_ID)
                        .onlyKey("param")
                        .priority(5)
                        .tag("background")
                        .callBack(new JsonRequestCallBack(null) {
                            @Override
                            public void requestSuccess(String url, JSONObject jsonObject) {
                                WorkOrderModel orderModel = new Gson().fromJson(jsonObject.toString(), WorkOrderModel.class);
                                orderModel.setShowDialog(false);

                                EventBus.getDefault().post(orderModel);//发送消息
                            }
                        })
                        .request();
        }

    }

    /**
     * 获取用户状态
     */
    private void getUserCurrStatus() {
        HttpUtils.get(HttpUri.GET_USER_CURR_STATUS)
                .dialog(false)
                .contentType(ContentType.TEXT)
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .onlyKey("usrId")
                .priority(5)
                .tag("background")
                .callBack(new JsonRequestCallBack(null) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        try {
                            //服务端用户状态
                            String serverStatus = response.data.getString("text");
                            if (!"-1".equals(serverStatus)
                                    && !LoginUtils.getUserStatus().equals(serverStatus)) {
                                //和本地状态比较，不一样就更新
                                modifyUserState(LoginUtils.getUserStatus());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .request();
    }

    /**
     * 修改用户状态
     * @param state
     */
    private void modifyUserState(String state) {
        //服务器状态更新
        HttpUtils.get(HttpUri.UPDATE_CURR_STATUS)
                .dialog(false)
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .data("status", state)
                .priority(5)
                .tag("background")
//                .callBack(new JsonRequestCallBack(this) {
//                    @Override
//                    public void requestSuccess(String url, JSONObject jsonObject) {
//
//                    }
//                })
                .request();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
