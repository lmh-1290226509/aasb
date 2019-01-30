package com.blks.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blks.https.HttpUri;
import com.blks.receiver.NetworkChangeReceiver;
import com.blks.utils.LoginUtils;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.utils.ContentType;

import java.util.Timer;
import java.util.TimerTask;

import static com.blks.utils.LoginUtils.canRequest;
import static com.blks.utils.LoginUtils.isNetwork;

public class HeartService extends Service {

    private NetworkChangeReceiver networkChangeReceiver;
    private IntentFilter intentFilter;

    private TimerTask timerTask;

    private Timer timer;

//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            requestHeart();
//            return false;
//        }
//    });

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intentFilter == null) {
            intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        }
        if (networkChangeReceiver == null) {
            networkChangeReceiver = new NetworkChangeReceiver();
            registerReceiver(networkChangeReceiver, intentFilter);
        }


        if (timer != null) {
            timer.cancel();

        }
        timer = new Timer();
        if (timerTask != null) {
            timerTask.cancel();

        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
//                handler.sendEmptyMessage(1);
                requestHeart();
            }
        };

        timer.schedule(timerTask, 0, 3000);

        return START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {

        return super.startService(service);
    }


    private void requestHeart() {
        if (!isNetwork || LoginUtils.getLoginModel() == null || !canRequest) {
            return;
        }
        HttpUtils.get(HttpUri.HEART_BEAT)
                .dialog(false)
                .contentType(ContentType.TEXT)
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .onlyKey("usrId")
                .priority(5)
                .tag("background")
//                .callBack(new JsonRequestCallBack(this) {
//                    @Override
//                    public void requestSuccess(String url, JSONObject jsonObject) {
//
//                    }
//
//                    @Override
//                    public void requestFail(String url, Response_<JSONObject> response) {
////                        super.requestFail(url, response);
//                    }
//                })
                .request();
    }

    @Override
    public boolean stopService(Intent name) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        unregisterReceiver(networkChangeReceiver);

        startService(new Intent(getApplicationContext(), HeartService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
