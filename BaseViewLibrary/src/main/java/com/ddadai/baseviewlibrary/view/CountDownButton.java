package com.ddadai.baseviewlibrary.view;



import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CountDownButton extends Button implements OnClickListener{

	public CountDownButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOnClickListener(this);
		if(!TextUtils.isEmpty(getText())){
			text=getText().toString();
		}
	}

	public CountDownButton(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public CountDownButton(Context context) {
		this(context,null);
	}

	@Override
	public void onClick(View v) {
		
		
		if(mOnCountButtonClickListener!=null&&mOnCountButtonClickListener.start()){
			start();
		}
	}

	
	private Runnable countDownRun=new Runnable() {
		
		@Override
		public void run() {
				index--;
				if(index>0){
					setText("重新发送("+index+"s)");
					postDelayed(countDownRun, 1000);
				}else{
					setEnabled(true);
					setText(text);
					if(mOnCountButtonClickListener!=null){
						mOnCountButtonClickListener.timeOut();
					}
				}
			
		}
	};
	
	
	
	
	private String text="发送验证码";
	private int index;//当前计时
	private int count=90;//总秒数
	private OnCountButtonClickListener mOnCountButtonClickListener;
	
	/**
	 * 设置秒数,不设置默认是60
	 * @param count
	 */
	public void setSecCount(int count){
		this.count=count;
	}
	
	
	public void start(){
		setEnabled(false);
		index=count;
		post(countDownRun);
	}
	
	/**
	 * 重置
	 */
	public void reset(){
		index=0;
	}
	
	public void setOnCountButtonClickListener(OnCountButtonClickListener countButtonClickListener){
		this.mOnCountButtonClickListener=countButtonClickListener;
	}
	
	public interface OnCountButtonClickListener{
		boolean start();
		void timeOut();
	}
}
