package com.blks.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.EventOrderModel_2;
import com.blks.utils.LoginUtils;
import com.blks.utils.Util;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.Date;

import static com.blks.utils.LoginUtils.isNetwork;

public class UpdateService extends Service {

    private EventOrderModel_2 orderModel;

    private boolean mySide, baidu;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (orderModel == null) {
                return false;
            }
            if (!isNetwork) {
                if (msg.what == 5 && !mySide) {
                    handler.sendEmptyMessageDelayed(5, 3000);
                } else  if (msg.what == 6 && !baidu) {
                    handler.sendEmptyMessageDelayed(6, 3000);
                }

            } else {
                switch (msg.what) {
                    case 5:
                        if (!mySide) {
                            updateMySite();
                        }
                        break;

                    case 6:
                        if (!baidu) {
                            updateBaidu();
                        }
                        break;
                }
            }

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
        return START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    @Subscribe
    public void updateEvent(EventOrderModel_2 model2) {
        orderModel = model2;
        orderModel.setEventTime(Util.getDate_ss());
//        orderModel.setBdLocation(RoadSideCarApplication.getInstance().getLocationService().bdLocation);
        mySide = false;
        baidu = false;

        handler.sendEmptyMessage(5);
        handler.sendEmptyMessage(6);
    }

    /**
     * 更新订单位置状态
     */
    private void updateMySite() {

        BDLocation location = RoadSideCarApplication.getInstance().getLocationService().bdLocation;
        if (LoginUtils.getLoginModel() == null) {
            return;
        }

        if (location == null || location.getLocType() == 62 || location.getLocType() == 63 || location.getLocType() == 67
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
            handler.sendEmptyMessageDelayed(5, 3000);
            return;
        }

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
                .data("gpsTime", orderModel.getEventTime())
                .priority(5);

        String carNo;

        if (LoginUtils.getCarModel() != null) {//车牌号
            carNo = LoginUtils.getCarModel().ASSETS_NO.trim();
        } else {
            carNo = LoginUtils.getLoginModel().UDF1.trim();
        }

        builder.data("carNo", carNo)
                .data("woNo", orderModel.getWoNo().trim())
                .data("outNo", orderModel.getOutNo())
                .data("option", orderModel.getOption())
                .mulKey("usrId")
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        mySide = true;
                        if (baidu) {
                            orderModel = null;
                        }
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        mySide = true;
                        if (baidu) {
                            orderModel = null;
                        }
                    }
                })
                .request();


    }

    private void updateBaidu() {

        BDLocation location = RoadSideCarApplication.getInstance().getLocationService().bdLocation;
        if (LoginUtils.getLoginModel() == null) {
            return;
        }

        if (location == null || location.getLocType() == 62 || location.getLocType() == 63 || location.getLocType() == 67
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
            handler.sendEmptyMessageDelayed(6, 3000);
            return;
        }

        String carNo;

        if (LoginUtils.getCarModel() != null) {//车牌号
            carNo = LoginUtils.getCarModel().ASSETS_NO.trim();
        } else {
            carNo = LoginUtils.getLoginModel().UDF1.trim();
        }

        String time = String.valueOf(new Date().getTime()).substring(0, 10);

        HttpUtils.baidu()
                .dialog(false)
                .data("ak", "QH0C4BHWBCmTVgBUyMlBQlDiNvU2CUT2")
                .data("service_id", "204947")
                .data("latitude", location.getLatitude())
                .data("longitude", location.getLongitude())
                .data("loc_time", Integer.valueOf(time))
                .data("coord_type_input", "bd09ll")
                .data("mark", orderModel.getOption())
                .data("entity_name", carNo + orderModel.getWoNo().trim())
                .priority(5)
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);

//                        if (HttpCode.NETWORK_ERROR.equals(response.code)) {
//                            handler.sendEmptyMessageDelayed(6, 3000);
//                            return;
//                        }

                        baidu = true;
                        if (mySide) {
                            orderModel = null;
                        }

                    }
                })
                .request();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        startService(new Intent(getApplicationContext(), UpdateService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
