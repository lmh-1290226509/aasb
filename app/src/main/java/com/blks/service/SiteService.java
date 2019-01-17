package com.blks.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.blks.antrscapp.R;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.model.EventOrderModel;
import com.blks.utils.Constants;
import com.blks.utils.LoginUtils;
import com.blks.utils.Util;
import com.ddadai.basehttplibrary.HttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.blks.utils.LoginUtils.isNetwork;

public class SiteService extends Service {

    private MediaPlayer mediaPlayer;

    private TimerTask timerTask;

    private Timer timer;

    private List<EventOrderModel> orderModels;//救援中的订单

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            updateSide();
            return false;
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!(EventBus.getDefault().isRegistered(this))) {
            EventBus.getDefault().register(this);
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
                handler.sendEmptyMessage(2);
            }
        };

        timer.schedule(timerTask, 0, 5000);

        return START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {

        return super.startService(service);
    }

    /**
     * EventBus监听
     *
     * @param model
     */
    @Subscribe
    public void receiveOrderModel(EventOrderModel model) {
        if (model.getAction() == Constants.OrderAction.ADD) {
            addModel(model);
        } else if (model.getAction() == Constants.OrderAction.REMOVE) {
            removeModel(model);
        } else if (model.getAction() == Constants.OrderAction.UPDATE) {
            updateModel(model);
        }
    }

    @Subscribe
    public void getOrderModels(List<EventOrderModel> list) {
        orderModels = list;
    }

    /**
     * app切换前后台的回掉
     * @param ib
     */
    @Subscribe
    public void isBackground(Boolean ib) {
        if (ib) {//后台
            if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                //开线程播放无声音乐
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startPlaySong();
                    }
                }).start();
            }
        } else {
            stopPlaySong();
        }
    }

    private void updateSide() {
        if (!isNetwork || LoginUtils.getLoginModel() == null) {
            return;
        }

        BDLocation location = RoadSideCarApplication.getInstance().getLocationService().bdLocation;

        if (location != null) {
            if (location.getLocType() == 62 || location.getLocType() == 63 || location.getLocType() == 67
                    || location.getLocType() == 162 || location.getLocType() == 167 || location.getLocType() == 505

                    || location.getLatitude() == 4.9E-324
                    || location.getLongitude() == 4.9E-324
                    || String.valueOf(location.getLatitude()).contains("4.9E-324")
                    || String.valueOf(location.getLongitude()).contains("4.9E-324")

//                    || location.getLatitude() == 4.94065645841247E-32
//                    || location.getLongitude() == 4.94065645841247E-32
//                    || String.valueOf(location.getLatitude()).contains("4.94065645841247E-32")
//                    || String.valueOf(location.getLongitude()).contains("4.94065645841247E-32")
                    || TextUtils.equals("cl", location.getNetworkLocationType())  //过滤基站定位结果
                    ) {

                return;
            }

            if (orderModels == null || orderModels.size() == 0) {
                return;
            }


            String time = String.valueOf(new Date().getTime()).substring(0, 10);
            int timeInt = Integer.valueOf(time);

            Iterator<EventOrderModel> iterator = orderModels.iterator();
            while (iterator.hasNext()) {
                final EventOrderModel orderModel = iterator.next();

                HttpUtils.RequestBuilder_ builder = HttpUtils
                        .get(HttpUri.UPDATE_MY_SITE)
                        .dialog(false)
                        .data("lat", location.getLatitude())
                        .data("lng", location.getLongitude())
                        .data("place", location.getAddrStr())
                        .data("usrId", LoginUtils.getLoginModel().USR_ID)
                        .data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
                        .data("usrName", LoginUtils.getLoginModel().USR_NAME)
                        .data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
                        .data("woFrom", "3")
                        .data("gpsTime", Util.getDate_ss())
                        .priority(5);

                String carNo;
                String woNo;
                String option;

                if (LoginUtils.getCarModel() != null) {//车牌号
                    carNo = LoginUtils.getCarModel().ASSETS_NO.trim();
                } else {
                    carNo = LoginUtils.getLoginModel().UDF1.trim();
                }

                builder.data("carNo", carNo);

//                if (orderModel != null && orderModel.getWoNo() != null) {
                woNo = orderModel.getWoNo().trim();
                option = orderModel.getOption();
                builder.data("woNo", orderModel.getWoNo())
                        .data("outNo", orderModel.getOutNo())
                        .data("option", orderModel.getOption());
//                } else {
//                    woNo = "";
//                    option = "R";
//                    builder.data("woNo", "")
//                            .data("outNo", "")
//                            .data("option", "R");
//                }

                builder.mulKey("usrId")
//                        .callBack(new JsonRequestCallBack(this) {
//                            @Override
//                            public void requestSuccess(String url, JSONObject jsonObject) {
////                                if ("F".equals(orderModel.getOption())) {
//////                                    iterator.remove();
////                                } else
////                                if (!"R".equals(orderModel.getOption())) {
//////                                    orderModel.setOption("R");
//////                                }
//                            }
//
//                            @Override
//                            public void requestFail(String url, Response_<JSONObject> response) {
//                                super.requestFail(url, response);
//                            }
//                        })
                        .request();

//                Log.e("TAG", "woNo="+woNo);


                HttpUtils.baidu()
                        .dialog(false)
                        .data("ak", "QH0C4BHWBCmTVgBUyMlBQlDiNvU2CUT2")
                        .data("service_id", "204947")
                        .data("latitude", location.getLatitude())
                        .data("longitude", location.getLongitude())
                        .data("loc_time", timeInt)
                        .data("coord_type_input", "bd09ll")
                        .data("mark", option)
                        .data("entity_name", carNo + woNo)
                        .priority(5)
//                        .callBack(new JsonRequestCallBack(this) {
//                            @Override
//                            public void requestSuccess(String url, JSONObject jsonObject) {
//
//                            }
//                        })
                        .request();

            }

        } else {
            RoadSideCarApplication.getInstance().getLocationService().start();
        }

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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        startService(new Intent(getApplicationContext(), SiteService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 添加
     *
     * @param model
     */
    private void addModel(EventOrderModel model) {
        if (orderModels == null) {
            orderModels = new ArrayList<>();
        }

        boolean exist = false;
        String woNo = model.getWoNo();

        for (EventOrderModel orderModel : orderModels) {
            if (woNo.equals(orderModel.getWoNo())) {
                exist = true;
                break;
            }
        }

        if (!exist) {
            orderModels.add(model);
        }
    }

    /**
     * 移除
     */
    private void removeModel(EventOrderModel model) {
        if (orderModels != null) {
            String woNo = model.getWoNo();

            Iterator<EventOrderModel> iterator = orderModels.iterator();
            while (iterator.hasNext()) {
                EventOrderModel orderModel = iterator.next();
                if (woNo.equals(orderModel.getWoNo())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * 状态更新
     */
    private void updateModel(EventOrderModel model) {
        if (orderModels != null) {
            String woNo = model.getWoNo();
            for (EventOrderModel orderModel : orderModels) {
                if (woNo.equals(orderModel.getWoNo())) {
                    orderModel.setOption(model.getOption());
                    break;
                }
            }
        }
    }

    private void startPlaySong() {
        if(Build.VERSION.SDK_INT  <  23){
            return;
        }
        if (mediaPlayer == null) {

//            if (RomHelper.isEmui() || RomHelper.isOppo()) {
            //华为OPPO
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.no_notice);
//            } else {
//                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.no_kill);
//            }

            mediaPlayer.setLooping(true);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        }

        if (mediaPlayer.isPlaying()) {
            stopPlaySong();
        }
        try {
            mediaPlayer.start();
            Log.i("TAG", "isMusic");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void stopPlaySong() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.prepareAsync();
            Log.i("TAG", "stopMusic");
        }
    }
}
