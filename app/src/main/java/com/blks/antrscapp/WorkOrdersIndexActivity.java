package com.blks.antrscapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.blks.app.BaseActivity;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.FileModel;
import com.blks.model.WorkOrderModel;
import com.blks.route.Route;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.ddadai.basehttplibrary.utils.HttpCode;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;

public class WorkOrdersIndexActivity extends BaseActivity implements OnClickListener {

	private ImageView work_orders_back;

	private TextView servceNumber, connectTime, businessSource, clientName, clientPhone,
			caNumber, servceProject, chargingStandard, helpReason, faultPosition,
			orderTime, sendTime, returnTime, locationTime, locationKm, truckKm,
			sendPerson, actualPerson, assistCar, dispatchCarNumber, serviceState,
			orderType, actualCompleteTime, clientSatisfaction, explain;
	private TextView imgsTv,lookImgsTv;

	private String woNo;

	private WorkOrderModel workModel;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_work_orders_index);


		woNo=getIntent().getStringExtra("_WO_NO");
		servceNumber = findViewById(R.id.servceNumber);
		connectTime = findViewById(R.id.connectTime);
		businessSource = findViewById(R.id.businessSource);
		clientName = findViewById(R.id.clientName);
		clientPhone = findViewById(R.id.clientPhone);
		caNumber = findViewById(R.id.caNumber);
		servceProject = findViewById(R.id.servceProject);
		chargingStandard = findViewById(R.id.chargingStandard);
		helpReason = findViewById(R.id.helpReason);
		faultPosition = findViewById(R.id.faultPosition);
		orderTime = findViewById(R.id.orderTime);
		sendTime = findViewById(R.id.sendTime);
		returnTime = findViewById(R.id.returnTime);
		locationTime = findViewById(R.id.locationTime);
		locationKm = findViewById(R.id.locationKm);
		truckKm = findViewById(R.id.truckKm);
		sendPerson = findViewById(R.id.sendPerson);
		actualPerson = findViewById(R.id.actualPerson);
		assistCar = findViewById(R.id.assistCar);
		dispatchCarNumber = findViewById(R.id.dispatchCarNumber);
		serviceState = findViewById(R.id.serviceState);
		orderType = findViewById(R.id.orderType);
		actualCompleteTime = findViewById(R.id.actualCompleteTime);
		clientSatisfaction = findViewById(R.id.clientSatisfaction);
		explain = findViewById(R.id.explain);

		imgsTv=findViewById(R.id.imgsTv);
		lookImgsTv=findViewById(R.id.lookImgsTv);
		lookImgsTv.setVisibility(View.GONE);

		work_orders_back = (ImageView) findViewById(R.id.work_orders_back);
		work_orders_back.setOnClickListener(this);

		getRscWOMstrByWoNo();
		requestFileList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.work_orders_index, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.work_orders_back:
			finish();
			break;

		default:
			break;
		}
		
	}


	/**
	 * 根据工单号获取工单信息
	 */
	private void getRscWOMstrByWoNo() {
		HttpUtils.get(HttpUri.GET_RSC_WOMSTR_BY_WONO)
				.dialog(true)
				.data("woNo", woNo)
				.onlyKey("woNo")
				.callBack(new JsonRequestCallBack(this) {
					@Override
					public void requestSuccess(String url, JSONObject jsonObject) {
						workModel = new Gson().fromJson(jsonObject.toString(), WorkOrderModel.class);
						updateView();
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

	private void updateView() {
		if(workModel!=null&&workModel.DataList!=null){
			WorkOrderModel.DataListModel model = workModel.DataList.get(0);
			servceNumber.setText(model.WO_NO);
			connectTime.setText(model.WO_DATE);
			businessSource.setText(model.WO_FROM_TEXT);
			clientName.setText(model.CUS_NAME);
			clientPhone.setText(model.CUS_MOBILE);
			caNumber.setText(model.CAR_NO);
			servceProject.setText(model.GOODS_NAME);
			chargingStandard.setText(model.SVC_TERMS);
			helpReason.setText(model.RESCUE_REASON_TEXT);
			faultPosition.setText(model.FROM_ADDR);
			orderTime.setText(model.WO_TAKE_DATE);
			sendTime.setText(model.DISPATCH_DATE);


			returnTime.setText(model.RE_CONNECT_DATE);
			locationTime.setText(model.EST_ARRIVE_DATE);

			locationKm.setText(model.ARRIVE_MILEAGE+"KM");
			truckKm.setText(model.TRAILER_MILEAGE+"KM");
			sendPerson.setText(model.DISPATCH_SP);
			actualPerson.setText(model.ACT_SP);
			assistCar.setText(model.RSC_CAR_NO);
			dispatchCarNumber.setText(model.RSC_CAR_NO);
			serviceState.setText(model.RSC_STEP);
			orderType.setText(model.DATA_FROM_TEXT);


			actualCompleteTime.setText(model.ACT_FINISH_DATE);
			clientSatisfaction.setText(model.CSI_VALUE);
			explain.setText(model.CAR_VIN);
		}
	}


	private void requestFileList(){
		HttpUtils.get(HttpUri.GET_RSC_FILE_LIST)
				.data("woNo",woNo)
				.data("fileClass","'图片'")
				.callBack(new JsonRequestCallBack(mThis) {
					@Override
					public void requestSuccess(String url, JSONObject jsonObject) {
						final FileModel fileModel=new Gson().fromJson(jsonObject.toString(),FileModel.class);
						if(fileModel!=null&&fileModel.DataList!=null){
							imgsTv.setText(fileModel.DataList.size()+"张");
							if(fileModel.DataList.size()>0){
								lookImgsTv.setVisibility(View.VISIBLE);
								lookImgsTv.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Route.builder(mThis)
												.class_(LookImgsActivity.class)
												.putExtra("imgs", (Serializable) fileModel.DataList)
												.startActivity();
									}
								});
							}else{
								lookImgsTv.setVisibility(View.GONE);
							}
						}
					}
				})
				.request();
	}

}
