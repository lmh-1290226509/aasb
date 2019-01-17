package com.blks.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.blks.antrscapp.R;
import com.blks.antrscapp.UserHandbookAct;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.GetTotalCountModel;
import com.blks.utils.Constants;
import com.blks.utils.LoginUtils;
import com.blks.utils.Util;
import com.blks.customer.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.ddadai.basehttplibrary.utils.HttpCode;
import com.google.gson.Gson;

import org.json.JSONObject;

public class HomeFragment extends Fragment implements OnClickListener {

	private Context context;
	private ImageView img_help;
	private CircleImageView userHeadImg;
	private Button btn_orders;
	private boolean isStop = true;
	private TextView carNoTv, carTypeTv, userNameTv, tv_home_status, tv_home_state;
	private GifView gif;
	private TextView monthCountTv,dayCountTv;
	private RatingBar rt_star1;
	private String headUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		context = this.getActivity();
		findview(view);
		inputData();
		return view;
	}

	private void findview(View view) {
		userHeadImg = (CircleImageView) view.findViewById(R.id.userHeadImg);
		img_help = (ImageView) view.findViewById(R.id.img_help);
		img_help.setOnClickListener(this);
		btn_orders = (Button) view.findViewById(R.id.btn_orders);
		btn_orders.setOnClickListener(this);
		tv_home_status = (TextView) view.findViewById(R.id.tv_home_status);
		tv_home_state = (TextView) view.findViewById(R.id.tv_home_state);
		carNoTv = view.findViewById(R.id.carNoTv);
		carTypeTv = view.findViewById(R.id.carTypeTv);
		userNameTv = view.findViewById(R.id.userNameTv);
		rt_star1 = view.findViewById(R.id.rt_star1);

		// 动态图
		gif = (GifView) view.findViewById(R.id.gif);
		gif.setGifImage(R.drawable.icon_status);
		gif.setGifImageType(GifImageType.COVER);

		dayCountTv=view.findViewById(R.id.dayCountTv);
		monthCountTv=view.findViewById(R.id.monthCountTv);
	}

	private void inputData() {
		if (LoginUtils.getCarModel() != null) {
			carNoTv.setText(LoginUtils.getCarModel().ASSETS_NO);
			carTypeTv.setText(LoginUtils.getCarModel().BV_VALUE);
		}
		if (LoginUtils.getLoginModel() != null) {
			userNameTv.setText(LoginUtils.getLoginModel().EMP_NAME);
			int star = TextUtils.isEmpty(LoginUtils.getLoginModel().USR_GRADE) ? 0 : Integer.parseInt(LoginUtils.getLoginModel().USR_GRADE);
			rt_star1.setRating(star);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		String urlImg = RoadSideCarApplication.getInstance().HeadUrl();
		if (urlImg != null && !urlImg.equals(headUrl)) {

			headUrl = urlImg;

			Glide.with(this)
					.load(headUrl)
					.asBitmap()
					.into(new SimpleTarget<Bitmap>() {

						@Override
						public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
							userHeadImg.setBitmap(resource);
						}
					});
		}
		isStop = !LoginUtils.getUserStatus().equals(Constants.UserStatus.READY);
		updateButton();
	}

	/**
	 * 更新按钮状态
	 */
	private void updateButton() {
		if (isStop) {
			btn_orders.setText("待    命");
			btn_orders.setBackgroundResource(R.drawable.wait_orders);
			tv_home_status.setVisibility(View.VISIBLE);
			tv_home_state.setText("暂停");
			gif.setVisibility(View.GONE);
		} else {
			btn_orders.setText("暂    停");
			btn_orders.setBackgroundResource(R.drawable.red);
			tv_home_status.setVisibility(View.GONE);
			tv_home_state.setText("待命中。。。。");
			gif.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_help:
			Util.moveTo(context, UserHandbookAct.class);
			break;
		case R.id.btn_orders:
			if (LoginUtils.getLoginModel() != null) {

				if (isStop) {
					requestDoingCount();
				} else {
					updateButtonStatus();
				}

			}

			break;
		default:
			break;
		}
	}


	@Override
	public void onStart() {
		super.onStart();
		requestTotalCount();
	}

	/**
	 * 查询当前是否有救援中的
	 */
	private void requestDoingCount(){
		if (LoginUtils.getLoginModel() != null) {
			HttpUtils.get(HttpUri.GET_RSC_DOING_CNT)
					.dialog(false)
					.data("usrId",LoginUtils.getLoginModel().USR_ID)
					.onlyKey("usrId")
					.callBack(new JsonRequestCallBack(getContext()) {
						@Override
						public void requestSuccess(String url, JSONObject jsonObject) {

							LoginUtils.setUserStatus(Constants.UserStatus.BUSY);

							RoadSideCarApplication.getInstance().showToast("您有"
									+jsonObject.optInt("iTotalCnt")+"条未完成的救援任务！请及时处理！");
						}

						@Override
						public void requestFail(String url, Response_<JSONObject> response) {
							super.requestFail(url, response);

							if (HttpCode.NETWORK_ERROR.equals(response.code)) {
								RoadSideCarApplication.getInstance().showToast("切换状态失败！"+response.msg);
								return;
							}
							//更改按钮
							updateButtonStatus();
						}
					})
					.request();
		}
	}

	/**
	 * 查询当月、今日订单数
	 */
	private void requestTotalCount(){
		if (LoginUtils.getLoginModel() != null) {
			HttpUtils.get(HttpUri.GET_RSC_TOTAL_CNT)
					.dialog(false)
					.data("usrId",LoginUtils.getLoginModel().USR_ID)
					.onlyKey("usrId")
					.callBack(new JsonRequestCallBack(getContext()) {
						@Override
						public void requestSuccess(String url, JSONObject jsonObject) {
							GetTotalCountModel model=new Gson().fromJson(jsonObject.toString(),GetTotalCountModel.class);
							if(model!=null&&model.DataList!=null&&!model.DataList.isEmpty()){
								GetTotalCountModel.DataListModel dataListModel = model.DataList.get(0);
								dayCountTv.setText(dataListModel.TODAY_CNT);
								monthCountTv.setText(dataListModel.MONTH_CNT);
							}
						}

						@Override
						public void requestFail(String url, Response_<JSONObject> response) {
//						super.requestFail(url, response);
						}
					})
					.request();
		}
	}

	/**
	 * 更改按钮状态
	 */
	private void updateButtonStatus() {
		HttpUtils.get(HttpUri.UPDATE_CURR_STATUS)
				.dialog(true)
				.data("usrId", LoginUtils.getLoginModel().USR_ID)
				.data("status", isStop ? Constants.UserStatus.READY : Constants.UserStatus.BREAK)//当前状态 ？ 要更改的状态   2待命  3离开
				.callBack(new JsonRequestCallBack(getActivity()){

					@Override
					public void requestSuccess(String url, JSONObject jsonObject) {
						isStop = !isStop;
						updateButton();

						//更改用户状态
						LoginUtils.setUserStatus(isStop ? Constants.UserStatus.BREAK : Constants.UserStatus.READY);
					}

					@Override
					public void requestFail(String url, Response_<JSONObject> response) {
						super.requestFail(url, response);
						RoadSideCarApplication.getInstance().showToast("切换状态失败！"+response.msg);
					}
				})
				.request();
	}


}
