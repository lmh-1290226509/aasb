package com.blks.antrscapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.blks.app.BaseActivity;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.EventOrderModel_2;
import com.blks.model.UploadFileModel;
import com.blks.pop.RecordCachePopupWindow;
import com.blks.pop.RefreshRecordCachePopupWindow;
import com.blks.utils.AudioFileFunc;
import com.blks.utils.AudioRecordFunc;
import com.blks.utils.Constants;
import com.blks.utils.LoginUtils;
import com.blks.utils.SharePreferenceUtil;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.ddadai.basehttplibrary.utils.HttpCode;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author shaoshuai
 * 录音页面
 */
public class RecordActivity extends BaseActivity implements OnClickListener {

    private TextView tv_record_close, tv_record_second, tv_record_up;
    private Button btn_playback, btn_begin, btn_finish, btn_record;
    AudioRecordFunc recordFunc;
    private Timer timer_down, timer_up;
    private TimerTask timerTask_down, timerTask_up;
    private Context context;

    private RecordCachePopupWindow cacheWindow;
    private RefreshRecordCachePopupWindow refreshCacheWindow;
    private boolean isFirst = true;
    private Handler handler_up = null;

    private int delay = 1000; // 1s
    private int period = 1000; // 1s
    private boolean isPause = false;
    private int j = 0;
    private int i = 120;

    private final int UPDATE_TEXTVIEW = 0;
    private final int UPDATE_DOWN_TEXTVIEW = 1;

    private String woNo, outSourceNo;
    private int starRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record);
        context = this;

        woNo = getIntent().getStringExtra(Constants._WO_NO);
        outSourceNo = getIntent().getStringExtra(Constants._OUT_SOURCE_NO);
        starRating = getIntent().getIntExtra(Constants._KEY_INT, 0);

        AudioFileFunc.AUDIO_WAV_FILENAME = woNo+".wav";

        recordFunc = AudioRecordFunc.getInstance();
        initView();// 初始化数据
    }

    @SuppressLint("HandlerLeak")
    private void initView() {
        tv_record_close = (TextView) findViewById(R.id.tv_record_close);
        tv_record_close.setOnClickListener(this);
        btn_begin = (Button) findClickView(R.id.btn_begin);
        btn_record = (Button) findClickView(R.id.btn_record);
        btn_finish = (Button) findClickView(R.id.btn_finish);
        btn_playback = (Button) findClickView(R.id.btn_playback);
        tv_record_second = (TextView) findViewById(R.id.tv_record_second);
        tv_record_up = (TextView) findViewById(R.id.tv_record_up);

        handler_up = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TEXTVIEW:// 正计时
                        tv_record_up.setText(String.valueOf(j));
                        if (j == 120) {
                            recordFunc.stopRecordAndFile();
                            stopTimer();
                            stopDown();
                            recordCache();
                            // 上传录音
                        }
                        break;
                    case UPDATE_DOWN_TEXTVIEW:// 倒计时
                        tv_record_second.setText(String.valueOf(i));
//                        if (tv_record_second.equals("0")) {
//                            // 上传录音
//                        }
                        break;
                    default:
                        break;
                }
            }
        };

        getAudioInfo();
    }

    /**
     * 开始正计时
     */

    private void starUp() {
        if (timer_up == null) {
            timer_up = new Timer();
        }

        if (timerTask_up == null) {
            timerTask_up = new TimerTask() {
                @Override
                public void run() {
                    sendMessage(UPDATE_TEXTVIEW);
                    do {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    } while (isPause);
                    j++;
                }
            };
        }

        if (timer_up != null && timerTask_up != null)
            timer_up.schedule(timerTask_up, delay, period);

    }

    public void sendMessage(int id) {
        if (handler_up != null) {
            Message message = Message.obtain(handler_up, id);
            handler_up.sendMessage(message);
        }
    }

    /**
     * 正计时结束
     */
    private void stopTimer() {
        if (timer_up != null) {
            timer_up.cancel();
            timer_up = null;
        }
        if (timerTask_up != null) {
            timerTask_up.cancel();
            timerTask_up = null;
        }
    }

    /**
     * 倒计时开始
     */
    private void starDown() {
        if (timer_down == null) {
            timer_down = new Timer();
        }

        if (timerTask_down == null) {
            timerTask_down = new TimerTask() {
                @Override
                public void run() {
                    sendMessage(UPDATE_DOWN_TEXTVIEW);
                    do {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    } while (isPause);
                    i--;
                }
            };
        }

        if (timer_down != null && timerTask_down != null)
            timer_down.schedule(timerTask_down, delay, period);
    }

    /**
     * 倒计时暂停
     */
    private void stopDown() {
        if (timer_down != null) {
            timer_down.cancel();
            timer_down = null;
        }
        if (timerTask_down != null) {
            timerTask_down.cancel();
            timerTask_down = null;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_record_close:
//			intent = new Intent(context, HomePagerActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			finish();
                onBackPressed();
                break;
            case R.id.btn_begin:// 开始录音
                btn_begin.setEnabled(false);
                if (isFirst) {
                    btn_begin.setText("正在录音");
                    btn_begin.setBackgroundResource(R.drawable.green);
                    recordFunc.startRecordAndFile();
                    starDown();
                    starUp();
                    isFirst = false;
                } else {
                    Log.e("走到这里了吗", "不知道");
                    if (btn_playback.getVisibility() == View.VISIBLE) {
                        RefreshCache();
                    }
                }
                break;
            case R.id.btn_finish:// 结束录音
                if (j == 0) {
                    return;
                }

                recordFunc.stopRecordAndFile();
                stopTimer();
                stopDown();
                recordCache();
                if (btn_playback.getVisibility() == View.VISIBLE) {
                    Log.e("走到这里了吗", "sdfghjkhgfdsfg");
                    btn_finish.setClickable(false);
                    btn_finish.setEnabled(false);
                }
                break;
            case R.id.btn_playback:// 回放录音
                recordFunc.stopRecordAndFile();
                recordFunc.StartPlaying();
                break;
            case R.id.btn_record:
                if (j == 0) {
                    ToastUtil.showLong(this, "请先录音！");
                    return;
                }
                if (recordFunc.isRecord()) {
                    ToastUtil.showLong(this, "请先结束录音！");
                    return;
                }

                btn_record.setEnabled(false);

                if (recordFunc == null || recordFunc.getAudioPath() == null) {
                    requestSaveConment(null);
                } else {
                    requestUpload();
                }

                break;
            default:
                break;
        }
    }


    private void recordCache() {
        cacheWindow = new RecordCachePopupWindow(context, cacheOnClick);
        cacheWindow.showAtLocation(
                ((Activity) context).findViewById(R.id.record), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    private void RefreshCache() {
        refreshCacheWindow = new RefreshRecordCachePopupWindow(context,
                refreshOnClick);
        refreshCacheWindow.showAtLocation(
                ((Activity) context).findViewById(R.id.record), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private OnClickListener cacheOnClick = new OnClickListener() {

        public void onClick(View v) {
            cacheWindow.dismiss();
            switch (v.getId()) {
                case R.id.tv_record_continue:// 保存

                    // 实际是上传文件
                    btn_playback.setVisibility(View.VISIBLE);
                    cacheWindow.dismiss();
                    btn_begin.setText("开始录音");
                    btn_begin.setEnabled(true);
                    btn_begin.setBackgroundResource(R.drawable.login_button);
                    btn_finish.setVisibility(View.GONE);
                    isFirst = false;

                    //保存录音信息
                    saveAudioInfo();
                    break;
                case R.id.tv_record_cancel:// 放弃

                    cacheWindow.dismiss();
                    btn_begin.setText("开始录音");
                    btn_begin.setEnabled(true);
                    btn_begin.setBackgroundResource(R.drawable.login_button);
                    j = 0;
                    i = 120;
                    tv_record_second.setText("120");
                    tv_record_up.setText("0");
                    isFirst = true;
                    break;
                default:
                    break;
            }
        }
    };

    // 为弹出窗口实现监听类
    private OnClickListener refreshOnClick = new OnClickListener() {

        public void onClick(View v) {
            refreshCacheWindow.dismiss();
            switch (v.getId()) {
                case R.id.tv_refresh_continue:// 确定
                    refreshCacheWindow.dismiss();
                    btn_playback.setVisibility(View.GONE);
                    btn_begin.setText("正在录音");
                    btn_begin.setBackgroundResource(R.drawable.green);
                    btn_finish.setVisibility(View.VISIBLE);
                    i = 120;
                    j = 0;
                    recordFunc.startRecordAndFile();
                    isFirst = false;
                    starDown();
                    starUp();
                    break;
                case R.id.tv_refresh_cancel:// 取消
                    btn_begin.setEnabled(true);
                    refreshCacheWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (recordFunc != null) {
            recordFunc.stopRecordAndFile();
            recordFunc.StopPlaying();
        }
        stopTimer();
        stopDown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recordFunc != null) {
            recordFunc.stopRecordAndFile();
            recordFunc.StopPlaying();
        }
        stopTimer();
        stopDown();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
        return true;
    }


    private void requestUpload() {
        HttpUtils.file().file(recordFunc.getAudioPath(), recordFunc.getAudioPath())
                .callBack(new JsonRequestCallBack(mThis) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        btn_record.setEnabled(true);
                        UploadFileModel uploadFileModel = new Gson().fromJson(jsonObject.toString(), UploadFileModel.class);
                        requestSaveConment(uploadFileModel);
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
//                        mImageInfoList.remove(model);
////                        mImageInfoList.remove(mImageInfoList.indexOf(model));
//                        adapter.setList(mImageInfoList);
                        btn_record.setEnabled(true);
                        if (HttpCode.NETWORK_ERROR.equals(response.code)) {
                            ToastUtil.showShort(context, response.msg);
                        }
                    }

                })
                .request();
    }


    private void requestSaveConment(UploadFileModel model) {
        if (TextUtils.isEmpty(woNo)) {
            btn_record.setEnabled(true);
            return;
        }

        HttpUtils.get(HttpUri.SAVE_EVALUATE_INFO)
                .dialog(true)
                .data("woNo", woNo)
                .data("evaluate", starRating)
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
                .data("usrName", LoginUtils.getLoginModel().USR_NAME)
                .data("audio", model == null ? "" : model.saveid)
                .data("trailer_mileage", "")
                .data("arrive_mileage", "")
                .data("usrRealName", LoginUtils.getLoginModel().USR_REAL_NAME)
                .data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
                .mulKey("woNo")
                .callBack(new JsonRequestCallBack(mThis) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        RoadSideCarApplication.getInstance().showToast("谢谢您的评价！");
                        btn_record.setEnabled(true);
                        //发送工单信息到位置更新服务
                        EventOrderModel_2 orderModel = new EventOrderModel_2();
                        orderModel.setWoNo(woNo);
                        orderModel.setOutNo(outSourceNo);
                        orderModel.setOption("F");

                        EventBus.getDefault().post(orderModel);

                        modifyUserState(Constants.UserStatus.ONLINE);


                        onBackPressed();

                        removeStar();
                        removeAudioInfo();

                        finish();
//                        Intent intent = new Intent(RecordActivity.this, HomePagerActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("bar_id", 2);
//                        RecordActivity.this.startActivity(intent);
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        btn_record.setEnabled(true);
                    }
                })
                .request();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        recordFunc.stopRecordAndFile();
        recordFunc.StopPlaying();
        stopTimer();
        stopDown();
    }

    /**
     * 删除存储的评价
     */
    private void removeStar() {

        int clienStar = (int) SharePreferenceUtil.get(this, woNo + "star", -1);
        if (clienStar != -1) {
            SharePreferenceUtil.remove(this, woNo + "star");
        }
    }

    /**
     * 保存录音信息
     */
    private void saveAudioInfo() {
        SharePreferenceUtil.put(this, woNo+"audio", j);
    }

    /**
     * 查询录音信息
     */
    private void getAudioInfo() {

        int audioTim = (int) SharePreferenceUtil.get(this, woNo + "audio", -1);
        if (audioTim != -1) {

            if (TextUtils.isEmpty(recordFunc.getAudioPath())) {
                recordFunc.setNewAudioName(AudioFileFunc.getWavFilePath());
            }

            File file = new File(recordFunc.getAudioPath());
            if (file.exists() && file.length() > 0) {

                btn_playback.setVisibility(View.VISIBLE);
                btn_begin.setText("开始录音");
                btn_begin.setEnabled(true);
                btn_begin.setBackgroundResource(R.drawable.login_button);
                btn_finish.setVisibility(View.GONE);
                isFirst = false;

                j = audioTim;
                i = 120 - j;
                tv_record_up.setText(String.valueOf(j));
                tv_record_second.setText(String.valueOf(i));
            }

        }

    }

    /**
     * 删除录音信息
     */
    private void removeAudioInfo() {

        int audioTim = (int) SharePreferenceUtil.get(this, woNo + "audio", -1);
        if (audioTim != -1) {
            SharePreferenceUtil.remove(this, woNo+"audio");
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
//				.data("woNo", workOrderModel.DataList.get(0).WO_NO)
//				.data("outNo", workOrderModel.DataList.get(0).OUT_SOURCE_NO)
//				.data("option", option)
//				.mulKey("usrId")
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
