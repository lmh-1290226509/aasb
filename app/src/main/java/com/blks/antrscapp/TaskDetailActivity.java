package com.blks.antrscapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.blks.app.CustomDialog.CustomDialogListener;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.EventOrderModel;
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
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 任务详情
 */
public class TaskDetailActivity extends BaseActivity implements OnClickListener {

	private Context context;

	private TextView address, tv_second, tv_task_close, tv_task_title, goodsNameTv, woFromTv;
	private Button btn_accept, btn_refuse, btn_arrive;
	private EditText destination;
	private LinearLayout ll_accept, ll_phone, ll_depart, ll_tel, ll_start, bottom_layout;
	private Timer timer;
	private TimerTask timerTask;
	private CustomDialog dialog;
	private CustomDialog cancelDialog;

	private int i = 30;
	//倒计时     区分标志
	private final int countdownFlag = 22;
	private String workNo;
//	private String destinations;
	private long starDate;
	private int orderType = 0;
	private Boolean isExit = false;

	private RemindPopWindow remindWindow;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	MyLocationConfiguration myLocationConfiguration;
	private LatLng starPoint, endPoint;//起点位置
	private BDLocation bdLocation;

	private WorkOrderModel.DataListModel workOrderData;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_detail);
		context = this;
		//工单号
		workNo = getIntent().getStringExtra(Constants._WO_NO);
		workOrderData = (WorkOrderModel.DataListModel) getIntent().getSerializableExtra(Constants._MODEL);
		orderType = getIntent().getIntExtra(Constants._KEY_INT, 0);
		initView();
		initMap();

		if (workOrderData == null) {
			getRscWOMstrByWoNo();
		} else {
			updateViewByModel();
		}
	}

	/**
	 * 30S倒计时
	 */
	private void countDown() {
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
				i--;
				Message me = handler.obtainMessage();
				me.arg1 = i;
				me.what = countdownFlag;
				handler.sendMessage(me);

			}
		};
		timer.schedule(timerTask, 1000);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case countdownFlag: //30s倒计时
					String second = tv_second.getText().toString().trim();
					if (second.equals("1")) {
						replyForRscWOMstr("0");
						return;
					}
					tv_second.setText(msg.arg1 + "");
					countDown();
					break;
			}
		};

	};

	private void initView() {

		goodsNameTv = findViewById(R.id.goodsNameTv);
		woFromTv = findViewById(R.id.woFromTv);
		address = (TextView) findViewById(R.id.address);
		tv_task_close = (TextView) findViewById(R.id.tv_task_close);
		tv_task_title = (TextView) findViewById(R.id.tv_task_title);
		tv_task_title.setText("线路规划");
		tv_second = (TextView) findViewById(R.id.tv_second);
		btn_accept = (Button) findViewById(R.id.btn_accept);
		btn_refuse = (Button) findViewById(R.id.btn_refuse);
		btn_arrive = (Button) findClickView(R.id.btn_arrive);
		btn_accept.setOnClickListener(this);
		btn_refuse.setOnClickListener(this);
		tv_task_close.setOnClickListener(this);
		destination = (EditText) findViewById(R.id.destination);
		ll_accept = (LinearLayout) findViewById(R.id.ll_accept);
		ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
		ll_depart = (LinearLayout) findClickView(R.id.ll_depart);
		ll_tel = (LinearLayout) findViewById(R.id.ll_tel);
		ll_start = (LinearLayout) findViewById(R.id.ll_start);
		bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);

		ll_tel.setOnClickListener(this);
		ll_start.setOnClickListener(this);
		findViewById(R.id.btn_navigation).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
	}

	/**
	 * 地图初始化
	 */
	private void initMap() {
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		//缩放按钮
		mMapView.showZoomControls(true);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

	}

	/**
	 * 地图上绘制点，路线规划
	 */
	private void drawMarkPoint() {
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
//		mBaiduMap.setMyLocationEnabled(false);


		if (bdLocation != null) {
			List<OverlayOptions> options = new ArrayList<>();
			//结束mark点
			endPoint = new LatLng(Double.parseDouble(workOrderData.FROM_ADDR_LAT),
					Double.parseDouble(workOrderData.FROM_ADDR_LNG));
			//定义Maker坐标点
			starPoint = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
			//构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option1 = new MarkerOptions()
					.position(starPoint)
					.icon(starBitmap);
			OverlayOptions option2 = new MarkerOptions()
					.position(endPoint)
					.icon(endBitmap);

			options.add(option1);
			options.add(option2);

			//在地图上添加Marker，并显示
			mBaiduMap.addOverlays(options);

			address.setText(bdLocation.getAddrStr());
			destination.setText(workOrderData.FROM_ADDR);
		}
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
		// // 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mBaiduMap.clear();
		mBaiduMap = null;
		mMapView.onDestroy();
		mMapView = null;

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}

		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_accept:// 接受
            timer.cancel();
			replyForRscWOMstr("1");//接单
			break;
		case R.id.tv_task_close:
//			intent = new Intent(this, HomePagerActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
			finish();
			break;
		case R.id.btn_refuse:
			//拒绝
			refuse();
			break;
		case R.id.ll_depart://出发
			updateRscWorkerOption("出发");
			RoadSideCarApplication.getInstance().showToast("为保证订单信息准确，请勿退至后台运行且保持屏幕常亮。谢谢！");
			break;
		case R.id.btn_arrive:
			// 弹出提示框
			reminder();
			break;
		case R.id.ll_tel:
			// String forPhoneNum = inputPhoneNumber.getText().toString();
			intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + workOrderData.CUS_MOBILE));
			startActivity(intent);
			break;
		case R.id.ll_start:

			break;
		case R.id.btn_navigation:
			if (dialog == null) {
				dialog = new CustomDialog(context, R.style.mystyle,
						new CustomDialogListener() {

							@Override
							public void onClick(View v) {
								switch (v.getId()) {
									case R.id.confirm_btn:// 确定
										NavigationUtils.startBaiduMapNavi(TaskDetailActivity.this,
												starPoint, bdLocation.getAddrStr(),
												endPoint, workOrderData.FROM_ADDR);
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
		case R.id.btn_cancel:

			long date = new Date().getTime();
		   if (date - starDate > 900000) {//15分钟
			   cancelOrder();
		   } else{
			   intent = new Intent(context, CancelWorkOrderActivity.class);
			   intent.putExtra(Constants._KEY_B, false);
			   intent.putExtra(Constants._WO_NO, workOrderData.WO_NO);
			   startActivity(intent);
		   }

			break;

		default:
			break;
		}
	}

    /**
     * 根据工单号获取工单信息
     */
	private void getRscWOMstrByWoNo() {
		if (workNo == null) {
			return;
		}
        HttpUtils.get(HttpUri.GET_RSC_WOMSTR_BY_WONO)
                .dialog(true)
                .data("woNo", workNo)
                .onlyKey("woNo")
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
						WorkOrderModel workOrderModel = new Gson().fromJson(jsonObject.toString(), WorkOrderModel.class);

						if (workOrderModel.DataList != null && workOrderModel.DataList.size() > 0) {
							workOrderData = workOrderModel.DataList.get(0);
							updateViewByModel();
						}

                    }

					@Override
					public void requestFail(String url, Response_<JSONObject> response) {
						super.requestFail(url, response);
						findViewById(R.id.order_Info_layout).setVisibility(View.GONE);
						if (HttpCode.NETWORK_ERROR.equals(response.code)) {
							ToastUtil.showShort(context,response.msg);
						}
					}
				})
                .request();
    }

	/**
	 * 出发15分钟后
	 * 取消有空驶
	 */
    private void cancelOrder() {
		if (cancelDialog == null) {
			cancelDialog = new CustomDialog(this, R.style.mystyle, new CustomDialogListener() {
				@Override
				public void onClick(View view) {
					switch (view.getId()) {
						case R.id.confirm_btn:// 确定
							Intent intent = new Intent(TaskDetailActivity.this, CameraHelpActivity.class);
							intent.putExtra(Constants._MODEL, workOrderData);
							intent.putExtra(Constants._KEY_B, true);
							startActivity(intent);
							finish();
							break;
						case R.id.cancel_btn:// 取消
							cancelDialog.dismiss();
							break;
					}
				}
			});
			cancelDialog.SetTitle("取消工单确认")
					.SetMessage("确认取消且有空驶（需拍摄路牌照片及工单照片）？");
		}

		cancelDialog.show();
	}

	/**
	 * 到达提示框
	 */
	private void reminder() {
		if (remindWindow == null) {
			remindWindow = new RemindPopWindow(context, cacheOnClick);
		}

		if (remindWindow.isShowing()) {
			return;
		}

		remindWindow.showAtLocation(
				((Activity) context).findViewById(R.id.task_detail),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	/**
	 * 拒绝按钮操作
	 */
	private void refuse() {
		timer.cancel();
		Intent intent = new Intent(context, CancelWorkOrderActivity.class);
		intent.putExtra(Constants._KEY_B, true);
		intent.putExtra(Constants._WO_NO, workOrderData.WO_NO);
		startActivity(intent);
	}

	/**
	 * 30s无响应
	 * 操作状态1:接单；-1:拒接；0:无响应
	 * 回复工单分配状态
	 */
	private void replyForRscWOMstr(final String reply) {

		HttpUtils.get(HttpUri.REPLY_FOR_RSC_WOMSTR)
				.dialog(true)
				.data("woNo", workOrderData.WO_NO)
				.data("reply", reply)
				.data("usrId", LoginUtils.getLoginModel().USR_ID)
				.data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
				.data("usrName", LoginUtils.getLoginModel().USR_NAME)
				.data("usrMobile", LoginUtils.getLoginModel().USR_MOBILE)
				.data("supNo", LoginUtils.getLoginModel().SUP_NO)
				.data("supName", LoginUtils.getLoginModel().SUP_NAME)
				.data("supMobile", LoginUtils.getLoginModel().SUP_MOBILE)
				.data("supManageUsrId", LoginUtils.getLoginModel().SUP_MANAGE_USRID)
				.data("supManageEmpNo", LoginUtils.getLoginModel().SUP_MANAGE_EMPNO)
				.data("usrRealName", LoginUtils.getLoginModel().USR_REAL_NAME)
				.data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
				.data("carNo", LoginUtils.getCarModel().ASSETS_NO)
				.mulKey("woNo")
				.callBack(new JsonRequestCallBack(this) {
					@Override
					public void requestSuccess(String url, JSONObject jsonObject) {

						if (TextUtils.equals("0", reply)) {
							//30s无响应
							ToastUtil.showPosition(TaskDetailActivity.this,"您没有回应该救援单！");
							modifyUserState(Constants.UserStatus.READY);//改状态

//							Intent intent = new Intent(TaskDetailActivity.this, HomePagerActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//							startActivity(intent);//回首页
							finish();
						} else if (TextUtils.equals("1", reply)) {//1接单
							ToastUtil.showPosition(TaskDetailActivity.this, "你已接受该救援单，请及时前往！");
							modifyUserState(Constants.UserStatus.BUSY);//改状态

							ll_accept.setVisibility(View.GONE);
							ll_phone.setVisibility(View.VISIBLE);
							tv_task_close.setVisibility(View.VISIBLE);
							tv_task_title.setText("任务信息");

							if (workOrderData != null) {
								//添加进位点服务
								EventOrderModel model = new EventOrderModel("R", workOrderData.WO_NO,
										workOrderData.OUT_SOURCE_NO, Constants.OrderAction.ADD);
								EventBus.getDefault().post(model);
							}

						}

					}

					@Override
					public void requestFail(String url, Response_<JSONObject> response) {
						super.requestFail(url, response);

						if (HttpCode.NETWORK_ERROR.equals(response.code)) {
							ToastUtil.showShort(context,response.msg);
							return;
						}

						if (TextUtils.equals("0", reply)) {
							//30s无响应
							ToastUtil.showPosition(TaskDetailActivity.this,"更新状态失败！");
//							Intent intent = new Intent(TaskDetailActivity.this, HomePagerActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//							startActivity(intent);
							finish();
						} else if (TextUtils.equals("1", reply)) {//1接单
							ToastUtil.showPosition(TaskDetailActivity.this,"接单失败！");
//							Intent intent = new Intent(TaskDetailActivity.this, HomePagerActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//							startActivity(intent);
							finish();
						}
					}
				})
				.request();
	}

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

						if (option.equals("出发")) {
//							destinations = destination.getText().toString().trim();
							starStatus();
						} else {//到达
							ToastUtil.showPosition(TaskDetailActivity.this,"您已到达现场，请及时拍照上传哦！");
							//发送工单信息到位置更新服务
							EventOrderModel_2 orderModel = new EventOrderModel_2();
							orderModel.setWoNo(workOrderData.WO_NO);
							orderModel.setOutNo(workOrderData.OUT_SOURCE_NO);
							orderModel.setOption("E");

							EventBus.getDefault().post(orderModel);

//							updateMySite("E");

							Intent intent = new Intent(TaskDetailActivity.this, CameraHelpActivity.class);
							intent.putExtra(Constants._MODEL, workOrderData);
							startActivity(intent);
							finish();
						}

					}

					@Override
					public void requestFail(String url, Response_<JSONObject> response) {
						super.requestFail(url, response);

						if (HttpCode.NETWORK_ERROR.equals(response.code)) {
							ToastUtil.showShort(context,response.msg);
							return;
						}

						boolean show = true;

						RemindPopWindow errorPop = new RemindPopWindow(mThis, new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(TaskDetailActivity.this, HomePagerActivity.class);
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
							ToastUtil.showLong(TaskDetailActivity.this, response.msg);
						}

						if (show) {
							errorPop.showAtLocation(
									((Activity) context).findViewById(R.id.task_detail),
									Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
						}

					}
				})
				.request();
	}

	private void starStatus() {
		ll_depart.setVisibility(View.GONE);
		bottom_layout.setVisibility(View.VISIBLE);
		//出发发送工单信息到位置更新服务
		EventOrderModel_2 orderModel = new EventOrderModel_2();
		orderModel.setWoNo(workOrderData.WO_NO);
		orderModel.setOutNo(workOrderData.OUT_SOURCE_NO);
		orderModel.setOption("S");

		EventBus.getDefault().post(orderModel);

//		updateMySite("S");

		starDate = new Date().getTime();
		ToastUtil.showPosition(TaskDetailActivity.this,"您已出发，请及时赶往救援现场！");

	}

	// 为弹出窗口实现监听类
	private OnClickListener cacheOnClick = new EventListener( new OnClickListener() {

		public void onClick(View v) {
			remindWindow.dismiss();
			switch (v.getId()) {
			case R.id.tv_remind_continue:// 确定
				OkGo.getInstance().cancelTag("background");
				updateRscWorkerOption("到达");
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
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出任务", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			// PreferencesUtil.setPreferences(this, "isFresh", "0");
			finish();
		}
	}

	/**
	 * 更新界面
	 */
	private void updateViewByModel() {
		drawMarkPoint();
		findViewById(R.id.order_Info_layout).setVisibility(View.VISIBLE);

		switch (orderType) {
			case Constants.OrderStatus.TYPE_0:
				goodsNameTv.setText(workOrderData.GOODS_NAME);
				woFromTv.setText(workOrderData.WO_FROM_TEXT);
				tv_second.setText(i+"");
				countDown();
				break;
			case Constants.OrderStatus.TYPE_1:
				ll_accept.setVisibility(View.GONE);
				ll_phone.setVisibility(View.VISIBLE);
				tv_task_close.setVisibility(View.VISIBLE);
				tv_task_title.setText("任务信息");
				break;
			case Constants.OrderStatus.TYPE_2:
				ll_accept.setVisibility(View.GONE);
				ll_depart.setVisibility(View.GONE);
				ll_phone.setVisibility(View.VISIBLE);
				tv_task_close.setVisibility(View.VISIBLE);
				tv_task_title.setText("任务信息");
				bottom_layout.setVisibility(View.VISIBLE);

				starDate = new Date().getTime();
				break;
		}
	}

//	/**
//	 * 更新订单位置状态
//	 * @param option
//	 */
//	private void updateMySite(String option) {
//		BDLocation location = RoadSideCarApplication.getInstance().getLocationService().bdLocation;
//		if (LoginUtils.getLoginModel() == null
//				|| location == null
//				|| workOrderModel == null
//				|| workOrderModel.DataList == null) {
//			return;
//		}
//
//		HttpUtils.RequestBuilder_ builder = HttpUtils
//				.get(HttpUri.UPDATE_MY_SITE)
//				.dialog(false)
//				.data("lat", location.getLatitude())
//				.data("lng", location.getLongitude())
//				.data("place", location.getAddrStr())
//				.data("usrId", LoginUtils.getLoginModel().USR_ID)
//				.data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
//				.data("usrName", LoginUtils.getLoginModel().USR_NAME)
//				.data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
//				.data("woFrom", "3");
//
//		String carNo;
//
//		if (LoginUtils.getCarModel() != null) {//车牌号
//			carNo = LoginUtils.getCarModel().ASSETS_NO;
//		} else {
//			carNo = LoginUtils.getLoginModel().UDF1;
//		}
//		builder.data("carNo", carNo)
//			.data("woNo", workOrderModel.DataList.get(0).WO_NO)
//			.data("outNo", workOrderModel.DataList.get(0).OUT_SOURCE_NO)
//			.data("option", option)
//			.mulKey("usrId")
//				.callBack(new JsonRequestCallBack(this) {
//					@Override
//					public void requestSuccess(String url, JSONObject jsonObject) {
//					}
//
//					@Override
//					public void requestFail(String url, Response_<JSONObject> response) {
//						super.requestFail(url, response);
//					}
//				})
//				.request();
//
//		String time = String.valueOf(new Date().getTime()).substring(0, 10);
//
//		HttpUtils.baidu()
//				.data("ak", "QH0C4BHWBCmTVgBUyMlBQlDiNvU2CUT2")
//				.data("service_id", "204947")
//				.data("latitude", location.getLatitude())
//				.data("longitude", location.getLongitude())
//				.data("loc_time", Integer.valueOf(time))
//				.data("coord_type_input", "bd09ll")
//				.data("mark", option)
//				.data("entity_name", carNo + workOrderModel.DataList.get(0).WO_NO)
//				.callBack(new JsonRequestCallBack(this) {
//					@Override
//					public void requestSuccess(String url, JSONObject jsonObject) {
//					}
//
//				})
//				.request();
//	}

}
