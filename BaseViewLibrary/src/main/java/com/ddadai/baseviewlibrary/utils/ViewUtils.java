package com.ddadai.baseviewlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ViewUtils {

	/** 得到分辨率高度 */
	public static int heightPs = -1;
	/** 得到分辨率宽度 */
	public static int widthPs = -1;
	/** 得到屏幕密度 */
	public static int densityDpi = -1;
	/** 得到X轴密度 */
	public static float Xdpi = -1;
	/** 得到Y轴密度 */
	public static float Ydpi = -1;
	
	/**切图上的宽***/
	private static int CutWidth=640;
	/**切图上的宽***/
	private static int CutHeight=800;

	/***
	 * 得到手机的屏幕基本信息
	 * 一开始要先调用这个
	 * @param context
	 */
	public static void initScreen(Context context) {
		try {
			if(heightPs!=-1){
				return;
			}
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
			heightPs = dm.heightPixels;
			widthPs = dm.widthPixels;
			densityDpi = dm.densityDpi;
			Xdpi = dm.xdpi;
			Ydpi = dm.ydpi;
//			LogUtil.i("手机分辨率", "分辨率：" + widthPs + "X" + heightPs + "    屏幕密度："+ densityDpi + "    宽高密度：" + Xdpi + "X" + Ydpi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	/**控制显示或者不显示*/
	public static void setShowView(View view) {
		if (view.getVisibility() == View.GONE) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	
	
	/**控制每4个字母就空一个空格**/
	public static void bankCardNumAddSpace(final EditText mEditText) {
		mEditText.addTextChangedListener(new TextWatcher() {
			int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;

			int location = 0;// 记录光标的位置
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				beforeTextLength = s.length();
				if (buffer.length() > 0) {
					buffer.delete(0, buffer.length());
				}
				konggeNumberB = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == ' ') {
						konggeNumberB++;
					}
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				onTextLength = s.length();
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3
						|| isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isChanged) {
					location = mEditText.getSelectionEnd();
					int index = 0;
					while (index < buffer.length()) {
						if (buffer.charAt(index) == ' ') {
							buffer.deleteCharAt(index);
						} else {
							index++;
						}
					}

					index = 0;
					int konggeNumberC = 0;
					while (index < buffer.length()) {
						if ((index == 4 || index == 9 || index == 14 || index == 19)) {
							buffer.insert(index, ' ');
							konggeNumberC++;
						}
						index++;
					}

					if (konggeNumberC > konggeNumberB) {
						location += (konggeNumberC - konggeNumberB);
					}

					tempChar = new char[buffer.length()];
					buffer.getChars(0, buffer.length(), tempChar, 0);
					String str = buffer.toString();
					if (location > str.length()) {
						location = str.length();
					} else if (location < 0) {
						location = 0;
					}

					mEditText.setText(str);
					Editable etable = mEditText.getText();
					Selection.setSelection(etable, location);
					isChanged = false;
				}
			}
		});
	}



	public static void showView(View view){
		view.setVisibility(View.VISIBLE);
	}
	public static void goneView(View view){
		view.setVisibility(View.GONE);
	}
	
	
	


	/**针对切图,获取等比例的宽***/
	public static int getViewWidth(int width){
		return widthPs*width/CutWidth;
	}
	/**针对切图,获取等比例的高***/
	public static int getViewHeight(int width,int height){
		return height*getViewWidth(width)/width;
	}
	
	/**设置view的宽高大小,输入切图上的大小,代码会在不同机子上进行不同比例的放大缩小**/
	public static void setViewSize(View view,int width,int height){
		if(view==null){
			return ;
			
		}
		android.view.ViewGroup.LayoutParams params=view.getLayoutParams();
		
		int trueWidth=0;
		int trueHeight=0;
		
		if(height<=0){
			trueHeight=LayoutParams.WRAP_CONTENT;
			trueWidth=widthPs*width/CutWidth;
		}else if(width<=0){
			trueWidth=LayoutParams.WRAP_CONTENT;
			trueHeight=heightPs*height/CutHeight;
		}else {
			trueWidth=widthPs*width/CutWidth;
			trueHeight=height*trueWidth/width;
		}
		
		try {
			ViewParent parent = view.getParent();
			if(parent!=null){
				if(parent instanceof LinearLayout){
					if(null==params){
						params=new LinearLayout.LayoutParams(trueWidth,trueHeight);
						view.setLayoutParams(params);
						return;
					}
				}else if(parent instanceof RelativeLayout){
					if(null==params){
						params=new RelativeLayout.LayoutParams(trueWidth,trueHeight);
						view.setLayoutParams(params);
						return;
					}
				}else  if(parent instanceof AbsListView){
					if(null==params){
						params=new AbsListView.LayoutParams(trueWidth,trueHeight);
						view.setLayoutParams(params);
						return;
					}
				}else {
					if(null==params){
						params=new LinearLayout.LayoutParams(trueWidth,trueHeight);
						view.setLayoutParams(params);
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(params!=null){
			params.width=trueWidth;
			params.height=trueHeight;
		}
	}


	public static void setViewMargin(View view,int left,int top,int right,int bottom){
		try {
			MarginLayoutParams params= (MarginLayoutParams) view.getLayoutParams();
			params.leftMargin=getViewWidth(left);
			params.topMargin=getViewWidth(top);
			params.rightMargin=getViewWidth(right);
			params.bottomMargin=getViewWidth(bottom);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setViewPadding(View view,int left,int top,int right,int bottom){
		view.setPadding(getViewWidth(left), getViewWidth(top), getViewWidth(right), getViewWidth(bottom));
	}

	public static void setTextSize(TextView tv,float size) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,widthPs*size/CutWidth);
	}

	
	


	/**
	 * 把密度dip单位转化为像数px单位
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dipToPx(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}
	
	
	
	/**
	 * 设置下划线
	 * @param tv
	 */
	public static void setTvUnderLine(TextView tv){
		try {
			tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static <T extends View> T findView(Activity act,int id){
		return act.findViewById(id);
	}
	public static <T extends View> T findView(View view,int id){
		return view.findViewById(id);
	}





	/**
	 *  设置edittext只能输入小数点后两位
	 */
	public static void afterDotTwo(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 限制最多能输入9位整数
				if (s.toString().contains(".")) {
					if (s.toString().indexOf(".") > 9) {
						s = s.toString().subSequence(0,9) + s.toString().substring(s.toString().indexOf("."));
						editText.setText(s);
						editText.setSelection(9);
					}
				}else {
					if (s.toString().length() > 9){
						s = s.toString().subSequence(0,9);
						editText.setText(s);
						editText.setSelection(9);
					}
				}
				// 判断小数点后只能输入两位
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				//如果第一个数字为0，第二个不为点，就不允许输入
				if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				if (editText.getText().toString().trim() != null && !editText.getText().toString().trim().equals("")) {
					if (editText.getText().toString().trim().substring(0, 1).equals(".")) {
						editText.setText("0" + editText.getText().toString().trim());
						editText.setSelection(1);
					}
				}
			}
		});
	}


}
