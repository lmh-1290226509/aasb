package com.blks.antrscapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blks.app.BaseActivity;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.WorkOrderModel;
import com.blks.utils.Constants;
import com.blks.utils.SharePreferenceUtil;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.ddadai.basehttplibrary.utils.HttpCode;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ClientSureActivity extends BaseActivity implements OnClickListener {

	private TextView tv_client_close;
	private Button btn_record;
	private RatingBar ratingBar;
	private TextView woNoTv, serverTypeTv, carNoTv, startTmTv, startLocTv, endLocTv
			,fromLocTv, arriveMileage, faultType, pictureNumberTv;

	private String woNo, picNum;
	private WorkOrderModel.DataListModel workOrderData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_client_sure);
		initView();// 初始化数据
	}

	private void initView() {

		woNo = getIntent().getStringExtra(Constants._WO_NO);
		workOrderData = (WorkOrderModel.DataListModel) getIntent().getSerializableExtra(Constants._MODEL);
		picNum = getIntent().getStringExtra(Constants._KEY_STR);

		woNoTv = findViewById(R.id.woNoTv);
		serverTypeTv = findViewById(R.id.serverTypeTv);
		carNoTv = findViewById(R.id.carNoTv);
		startTmTv = findViewById(R.id.startTmTv);
		startLocTv = findViewById(R.id.startLocTv);
		endLocTv = findViewById(R.id.endLocTv);
		fromLocTv = findViewById(R.id.fromLocTv);
		arriveMileage = findViewById(R.id.arriveMileage);
		faultType = findViewById(R.id.faultType);
		pictureNumberTv = findViewById(R.id.pictureNumberTv);
		ratingBar = findViewById(R.id.room_ratingbar);
		tv_client_close = (TextView) findViewById(R.id.tv_client_close);
		btn_record = (Button) findViewById(R.id.btn_record);

		tv_client_close.setOnClickListener(this);
		btn_record.setOnClickListener(this);

		if (workOrderData == null) {
			getRscWOMstrByWoNo();
		} else {
			updateView();
		}

	}

	private void updateView() {
		if (workOrderData != null) {

			woNoTv.setText(workOrderData.WO_NO);
			serverTypeTv.setText(workOrderData.GOODS_NAME);
			carNoTv.setText(workOrderData.CAR_NO);
			startLocTv.setText(workOrderData.CAR_START_ADDR_PROVINCE
					+workOrderData.CAR_START_ADDR_CITY
					+workOrderData.CAR_START_ADDR_REGION
					+workOrderData.CAR_START_ADDR_DETAIL);

			startTmTv.setText(workOrderData.WO_TAKE_DATE);
			endLocTv.setText(workOrderData.ACT_ARRIVE_DATE);
			fromLocTv.setText(workOrderData.FROM_ADDR_DETAIL);
			arriveMileage.setText(workOrderData.ARRIVE_MILEAGE+"KM");

			if ("上海平安非事故".equals(workOrderData.WO_FROM_TEXT)) {
				faultType.setText("非事故");
			} else {
				faultType.setText("事故");
			}

			if (!TextUtils.isEmpty(picNum)) {
				pictureNumberTv.setText(picNum);
				savePictureNumber();
			} else {
				String number = String.valueOf(SharePreferenceUtil.get(this, workOrderData.WO_NO+"picNum", ""));
				pictureNumberTv.setText(TextUtils.isEmpty(number) ? workOrderData.UDF1 : number);
			}

			removePicture();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client_sure, menu);
		return true;
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
						WorkOrderModel workOrderModel = new Gson().fromJson(jsonObject.toString(), WorkOrderModel.class);

						if (workOrderModel.DataList != null && workOrderModel.DataList.size() > 0) {
							workOrderData = workOrderModel.DataList.get(0);
							updateView();
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

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_client_close:
//			intent = new Intent(this, HomePagerActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.putExtra("bar_id", 2);
//			startActivity(intent);
			finish();
			break;
		case R.id.btn_record:
			if (workOrderData != null) {

				intent = new Intent(ClientSureActivity.this, RecordActivity.class);
				intent.putExtra(Constants._WO_NO, workOrderData.WO_NO);
				intent.putExtra(Constants._OUT_SOURCE_NO, workOrderData.OUT_SOURCE_NO);
				intent.putExtra(Constants._KEY_INT, (int)ratingBar.getRating());
				startActivity(intent);

				SharePreferenceUtil.put(mThis, workOrderData.WO_NO+"star", (int)ratingBar.getRating());
				removPictureNumber();
				finish();
			} else {
				ToastUtil.showShort(mThis, "数据加载失败，请退出后再进入");
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 查询本地是否有图片,有则移除
	 */
	private void removePicture() {

		String imgString = SharePreferenceUtil.get(this, workOrderData.WO_NO, "").toString();
		if (!TextUtils.isEmpty(imgString)) {
			SharePreferenceUtil.remove(this, workOrderData.WO_NO);
		}

	}

	/**
	 * 临时保存图片数量
	 */
	private void savePictureNumber() {

		SharePreferenceUtil.put(this, workOrderData.WO_NO+"picNum", picNum);

	}

	/**
	 * 删除临时保存图片数量
	 */
	private void removPictureNumber() {

		String number = String.valueOf(SharePreferenceUtil.get(this, workOrderData.WO_NO+"picNum", ""));
		if (!TextUtils.isEmpty(number)) {
			SharePreferenceUtil.remove(this, workOrderData.WO_NO+"picNum");
		}

	}

}
