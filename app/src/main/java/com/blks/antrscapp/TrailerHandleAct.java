package com.blks.antrscapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.blks.app.BaseActivity;
import com.blks.app.CustomDialog;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.EventOrderModel_2;
import com.blks.model.WorkOrderModel;
import com.blks.pop.RemindPopWindow;
import com.blks.utils.Constants;
import com.blks.utils.EventListener;
import com.blks.utils.LoginUtils;
import com.blks.utils.NavigationUtils;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.ddadai.basehttplibrary.utils.HttpCode;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 拖车
 */
public class TrailerHandleAct extends BaseActivity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    MyLocationConfiguration myLocationConfiguration;
    private LatLng startPoint, endPoint;//终点位置
    private BDLocation bdLocation;

    private CustomDialog dialog;
    private RemindPopWindow remindWindow;
    private Button btn_arrive, btn_start, btn_navigation, btn_colse;

    private WorkOrderModel.DataListModel workOrderData;
    private TextView start_tv, end_tv;
    private boolean trailerStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_trailer_handle);
        trailerStatus = getIntent().getBooleanExtra(Constants._KEY_B, false);
        initView();
        initLocation();
    }

    private void initView() {
        workOrderData = (WorkOrderModel.DataListModel) getIntent().getSerializableExtra(Constants._MODEL);
        if (TextUtils.isEmpty(workOrderData.TO_ADDR)) {
            getRscWOMstrByWoNo();
        }

        start_tv = findViewById(R.id.start_tv);
        end_tv = findViewById(R.id.end_tv);
        btn_arrive = findClickView(R.id.btn_arrive);
        btn_start = findClickView(R.id.btn_start);
        btn_navigation = findViewById(R.id.btn_navigation);
        btn_colse = findViewById(R.id.btn_colse);

        btn_navigation.setOnClickListener(this);
        btn_colse.setOnClickListener(this);
        findViewById(R.id.tv_trailer_close).setOnClickListener(this);

        if (trailerStatus) {
            btn_start.setVisibility(View.GONE);
            btn_arrive.setVisibility(View.VISIBLE);
            btn_navigation.setVisibility(View.VISIBLE);

        }

        if (workOrderData != null) {
            start_tv.setText(workOrderData.FROM_ADDR);
            end_tv.setText(workOrderData.TO_ADDR);
        }
    }

    private void initLocation() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bdMapView);
        //缩放按钮
        mMapView.showZoomControls(true);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启交通图
        mBaiduMap.setTrafficEnabled(true);

        bdLocation = RoadSideCarApplication.getInstance().getLocationService().bdLocation;

        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        //构建Marker图标
        BitmapDescriptor starBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.site1);
        BitmapDescriptor endBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.site2);
        myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                true, null);

        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
        // 当不需要定位图层时关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);


        if (bdLocation != null) {
            List<OverlayOptions> options = new ArrayList<>();
            //开始Maker坐标点
            startPoint = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            //结束mark点
            endPoint = new LatLng(Double.parseDouble(workOrderData.TO_ADDR_LAT),
                    Double.parseDouble(workOrderData.TO_ADDR_LNG));
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option1 = new MarkerOptions()
                    .position(startPoint)
                    .icon(starBitmap);
            OverlayOptions option2 = new MarkerOptions()
                    .position(endPoint)
                    .icon(endBitmap);

            options.add(option1);
            options.add(option2);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlays(options);

            start_tv.setText(bdLocation.getAddrStr());
            end_tv.setText(workOrderData.TO_ADDR);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_trailer_close:
                finish();
                break;
            case R.id.btn_arrive://到达
                reminder();
                break;
            case R.id.btn_start://出发
                btn_start.setEnabled(false);
                updateRscWorkerOption("拖车出发");
                break;
            case R.id.btn_navigation:
                if (dialog == null) {
                    dialog = new CustomDialog(this, R.style.mystyle,
                            new CustomDialog.CustomDialogListener() {

                                @Override
                                public void onClick(View v) {
                                    switch (v.getId()) {
                                        case R.id.confirm_btn:// 确定
                                            NavigationUtils.startBaiduMapNavi(TrailerHandleAct.this,
                                                    endPoint, bdLocation.getAddrStr(),
                                                    endPoint, workOrderData.TO_ADDR);
                                            dialog.dismiss();
                                            break;
                                        case R.id.cancel_btn:// 取消
                                            dialog.dismiss();
                                            break;

                                        default:
                                            break;
                                    }
                                }
                            });
                }
                dialog.show();
                break;
            case R.id.btn_colse:
                finish();
                break;

        }
    }


    private void reminder() {
        if (remindWindow == null) {
            remindWindow = new RemindPopWindow(this, cacheOnClick);
        }

        if (remindWindow.isShowing()) {
            return;
        }

        remindWindow.showAtLocation(findViewById(R.id.trailer_layout),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener cacheOnClick = new EventListener( new View.OnClickListener() {

        public void onClick(View v) {
            remindWindow.dismiss();
            switch (v.getId()) {
                case R.id.tv_remind_continue:// 确定
                    btn_arrive.setEnabled(false);
                    updateRscWorkerOption("拖车到达");
                    remindWindow.dismiss();
                    break;
                case R.id.tv_remind_cancel:// 取消
                    remindWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    });

    /**
     * 更新救援工的状态
     * @param option
     */
    private void updateRscWorkerOption(final String option) {

        HttpUtils.get(HttpUri.UPDATE_RSC_WORKER_OPTION)
                .dialog(true)
                .data("woNo", workOrderData.WO_NO)
                .data("option", option)
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
                .data("usrName", LoginUtils.getLoginModel().USR_NAME)
                .data("usrRealName", LoginUtils.getLoginModel().USR_REAL_NAME)
                .data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
                .data("carNo", LoginUtils.getCarModel().ASSETS_NO)
                .mulKey("woNo")
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {

                        if (option.equals("拖车出发")) {
                            btn_start.setVisibility(View.GONE);
                            btn_arrive.setVisibility(View.VISIBLE);
                            btn_navigation.setVisibility(View.VISIBLE);

                            CameraHelpActivity.trailerStatus = "拖车已出发";

                            //出发发送工单信息到位置更新服务
                            EventOrderModel_2 orderModel = new EventOrderModel_2();
                            orderModel.setWoNo(workOrderData.WO_NO);
                            orderModel.setOutNo(workOrderData.OUT_SOURCE_NO);
                            orderModel.setOption("TS");

                            EventBus.getDefault().post(orderModel);

//                            updateMySite("TS");

                            RoadSideCarApplication.getInstance().showToast("拖车已出发，请前往目的地！");
                        } else {//到达

                            btn_start.setVisibility(View.GONE);
                            btn_arrive.setVisibility(View.GONE);
                            btn_navigation.setVisibility(View.GONE);
                            btn_colse.setVisibility(View.VISIBLE);

                            CameraHelpActivity.trailerStatus = "拖车已到达";

                            //发送工单信息到位置更新服务
                            EventOrderModel_2 orderModel = new EventOrderModel_2();
                            orderModel.setWoNo(workOrderData.WO_NO);
                            orderModel.setOutNo(workOrderData.OUT_SOURCE_NO);
                            orderModel.setOption("TE");

                            EventBus.getDefault().post(orderModel);

//                            updateMySite("TE");
                        }

                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        btn_start.setEnabled(true);
                        btn_arrive.setEnabled(true);
                        if (HttpCode.NETWORK_ERROR.equals(response.code)) {
                            ToastUtil.showShort(mThis,response.msg);
                            return;
                        }

                        boolean show = true;

                        RemindPopWindow errorPop = new RemindPopWindow(mThis, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(TrailerHandleAct.this, HomePagerActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                        errorPop.setCacheGone();
                        errorPop.setTitlePop("工单状态");

                        if ("-100".equals(response.msg)) {
                            errorPop.setMessagePop("该救援单已经完成");
                        } else if ("-101".equals(response.msg)) {
                            errorPop.setMessagePop("该救援单已经取消");
                        } else if ("-102".equals(response.msg)) {
                            errorPop.setMessagePop("该救援单已经退回");
                        } else if ("-110".equals(response.msg)) {
                            errorPop.setMessagePop("该救援单状态已经发生改变，请刷新工单列表");
                        } else {
                            show = false;
                        }

                        if (show) {
                            errorPop.showAtLocation(findViewById(R.id.trailer_layout),
                                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                        }

                    }
                })
                .request();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.clear();
        mBaiduMap = null;
        mMapView.onDestroy();
        mMapView = null;

    }

    /**
     * 根据工单号获取工单信息
     */
    private void getRscWOMstrByWoNo() {
        HttpUtils.get(HttpUri.GET_RSC_WOMSTR_BY_WONO)
                .dialog(true)
                .data("woNo", workOrderData.WO_NO)
                .onlyKey("woNo")
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        WorkOrderModel workOrderModel = new Gson().fromJson(jsonObject.toString(), WorkOrderModel.class);

                        if (workOrderModel.DataList != null && workOrderModel.DataList.size() > 0) {
                            workOrderData = workOrderModel.DataList.get(0);
                            end_tv.setText(workOrderData.TO_ADDR);
                        }

                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        if (HttpCode.NETWORK_ERROR.equals(response.code)) {
                            ToastUtil.showShort(mThis,response.msg);
                        }
                    }
                })
                .request();
    }

//    /**
//     * 更新订单位置状态
//     * @param option
//     */
//    private void updateMySite(String option) {
//        BDLocation location = RoadSideCarApplication.getInstance().getLocationService().bdLocation;
//        if (LoginUtils.getLoginModel() == null
//                || location == null
//                || workOrderModel == null
//                || workOrderModel.DataList == null) {
//            return;
//        }
//
//        HttpUtils.RequestBuilder_ builder = HttpUtils
//                .get(HttpUri.UPDATE_MY_SITE)
//                .dialog(false)
//                .data("lat", location.getLatitude())
//                .data("lng", location.getLongitude())
//                .data("place", location.getAddrStr())
//                .data("usrId", LoginUtils.getLoginModel().USR_ID)
//                .data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
//                .data("usrName", LoginUtils.getLoginModel().USR_NAME)
//                .data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
//                .data("woFrom", "3");
//
//        String carNo;
//
//        if (LoginUtils.getCarModel() != null) {//车牌号
//            carNo = LoginUtils.getCarModel().ASSETS_NO;
//        } else {
//            carNo = LoginUtils.getLoginModel().UDF1;
//        }
//        builder.data("carNo", carNo)
//                .data("woNo", workOrderModel.DataList.get(0).WO_NO)
//                .data("outNo", workOrderModel.DataList.get(0).OUT_SOURCE_NO)
//                .data("option", option)
//                .mulKey("usrId")
//                .callBack(new JsonRequestCallBack(this) {
//                    @Override
//                    public void requestSuccess(String url, JSONObject jsonObject) {
//                    }
//
//                    @Override
//                    public void requestFail(String url, Response_<JSONObject> response) {
//                        super.requestFail(url, response);
//                    }
//                })
//                .request();
//
//        String time = String.valueOf(new Date().getTime()).substring(0, 10);
//
//        HttpUtils.baidu()
//                .data("ak", "QH0C4BHWBCmTVgBUyMlBQlDiNvU2CUT2")
//                .data("service_id", "204947")
//                .data("latitude", location.getLatitude())
//                .data("longitude", location.getLongitude())
//                .data("loc_time", Integer.valueOf(time))
//                .data("coord_type_input", "bd09ll")
//                .data("mark", option)
//                .data("entity_name", carNo + workOrderModel.DataList.get(0).WO_NO)
//                .callBack(new JsonRequestCallBack(this) {
//                    @Override
//                    public void requestSuccess(String url, JSONObject jsonObject) {
//                    }
//
//                })
//                .request();
//    }
}
