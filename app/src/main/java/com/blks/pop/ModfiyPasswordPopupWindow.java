package com.blks.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blks.antrscapp.R;
import com.blks.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModfiyPasswordPopupWindow extends PopupWindow implements
		OnClickListener {

	private View menuView;
	private TextView tv_cancel, tv_save;
	private EditText et_pwd_current, et_pwd_new, et_pwd_sure;
	private Context context;
	private JSONObject object2;
	private JSONArray jsonArray;
	private String present_pwd, new_pwd, confirm_pwd;
	private OnPwdChangeClick onPwdChangeClick;

	public ModfiyPasswordPopupWindow(final Activity context,
									 OnPwdChangeClick itemsOnClick) {
		super(context);
		this.onPwdChangeClick=itemsOnClick;
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menuView = inflater.inflate(R.layout.modfiy_password, null);
		tv_cancel = (TextView) menuView.findViewById(R.id.tv_cancel);
		tv_save = (TextView) menuView.findViewById(R.id.tv_save);
		et_pwd_current = (EditText) menuView.findViewById(R.id.et_pwd_current);
		et_pwd_sure = (EditText) menuView.findViewById(R.id.et_pwd_sure);
		et_pwd_new = (EditText) menuView.findViewById(R.id.et_pwd_new);

		// 取消按钮
		tv_cancel.setOnClickListener(this);
		// 设置按钮监听
		tv_save.setOnClickListener(this);
		// 设置SelectPicPopupWindow的View
		this.setContentView(menuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		menuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = menuView.findViewById(R.id.pop_modify).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:// 取消
			dismiss();
			break;
		case R.id.tv_save:// 保存
			present_pwd = et_pwd_current.getText().toString().trim();
			new_pwd = et_pwd_new.getText().toString().trim();
			confirm_pwd = et_pwd_sure.getText().toString().trim();
			if (TextUtils.isEmpty(present_pwd)) {
				ToastUtil.showPosition(context, "请输入当前密码");
			} else if (TextUtils.isEmpty(new_pwd)) {
				ToastUtil.showPosition(context, "请输入新密码");
			} else if (present_pwd.equals(new_pwd)) {
				ToastUtil.showPosition(context, "新密码不能和当前密码一样");
			} else if (TextUtils.isEmpty(confirm_pwd)) {
				ToastUtil.showPosition(context, "请再次输入新密码");
			} else if (!new_pwd.equals(confirm_pwd)) {
				ToastUtil.showPosition(context, "两次密码输入的不一样");
			} else {
				// 调接口
				if(onPwdChangeClick!=null){
					onPwdChangeClick.pwdChangeClick(present_pwd,new_pwd);
				}else{
					dismiss();
				}
			}
			break;
		default:
			break;
		}
	}

//	// 修改密码
//	private void confirmPassWord() {
//		HttpUtils.get(HttpUri.RESET_PWD)
//				.dialog(false)
//				.data("","")
//				.data("","")
//				.callBack(context)
//				.request();
////		changeArrayDateToJson();
////		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
////		map.put("param", jsonArray.toString());// 用户密码
////		map.put("token", "");// 用户密码
////		FastHttp.ajaxGet(HttpAddress.CONFIRM_PASSWORD, map,
////				App.getInstance().config, new AjaxCallBack() {
////
////					@Override
////					public void callBack(ResponseEntity responseEntity) {
////						String returnString = responseEntity
////								.getContentAsString();
////						Log.e("confirm:", String.valueOf(returnString));
////						if (returnString != null) {
////							// SharePreferenceUtil.setParam(context, "username",
////							// username);
////							// SharePreferenceUtil.setParam(context, "password",
////							// password);
////							// ToastUtil.showShort(context, "登录成功");
////							// openActivity(SeclectCarsActivity.class);
////							// finish();
////						}
////					}
////
////					@Override
////					public boolean stop() {
////						return false;
////					}
////				});
//	}

	/*
	 * 转化成json字符串
	 */
	private void changeArrayDateToJson() { // 把一个集合转换成json格式的字符串
		jsonArray = new JSONArray();
		object2 = new JSONObject();

		try {
			object2.put("name", "usrPwdOld");
			object2.put("value", present_pwd);
			jsonArray.put(object2);

			object2 = new JSONObject();
			object2.put("name", "usrPwdNew");
			object2.put("value", new_pwd);
			jsonArray.put(object2);

			object2 = new JSONObject();
			object2.put("name", "uuid");
			object2.put("value", "34567890-=4567");
			jsonArray.put(object2);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("修改密码", "转换成json字符: " + jsonArray);
		System.out.println("hck" + jsonArray);
	}


	public  interface OnPwdChangeClick{
		void pwdChangeClick(String oldPwd,String pwd);
	}
}
