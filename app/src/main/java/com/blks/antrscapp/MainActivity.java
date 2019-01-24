package com.blks.antrscapp;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blks.app.BaseActivity;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.LoginModel;
import com.blks.service.HeartService;
import com.blks.service.SiteService;
import com.blks.utils.ApkUpdateUtils;
import com.blks.utils.Constants;
import com.blks.utils.LoginUtils;
import com.blks.utils.SharePreferenceUtil;
import com.blks.utils.SystemUtils;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements OnClickListener {

    private final String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private EditText et_account, et_psd;
    private Button btn_login;
    private static String username = "", password = "", uuid = "";
    private static Boolean isExit = false;
    private JSONObject objLogin;
    private ImageView main_username_delete, main_pwd_delete;
    private ApkUpdateUtils apkUpdateUtils;

    @Override
    protected boolean needPermission() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        SystemUtils.getScreen(this);
        initView();// 初始化数据

        String name = SharePreferenceUtil.get(mThis, "userName", "")
                .toString();
        String psw = SharePreferenceUtil.get(mThis, "passWord", "")
                .toString();
        et_account.setText(name);
        et_psd.setText(psw);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

        }
        permissionUtils.RequestPermission(permissions);
    }

    private void initView() {
        //获取唯一标识
        String androidID = Settings.Secure.getString(mThis.getContentResolver(), Settings.Secure.ANDROID_ID);
        uuid = androidID + Build.SERIAL;

        apkUpdateUtils = new ApkUpdateUtils(this, false);

        et_account = (EditText) findViewById(R.id.et_account);
        et_psd = (EditText) findViewById(R.id.et_psd);
        btn_login = (Button) findViewById(R.id.btn_login);
        main_username_delete = (ImageView) findViewById(R.id.main_username_delete);
        main_pwd_delete = (ImageView) findViewById(R.id.main_pwd_delete);
        btn_login.setOnClickListener(this);
        main_username_delete.setOnClickListener(this);
        main_pwd_delete.setOnClickListener(this);
        /*
         * 设置用户名文本输入变化监听
         */
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (et_account.length() != 0) {
                    btn_login.setEnabled(true);
                    btn_login.setBackgroundResource(R.drawable.green);
                } else {
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundResource(R.drawable.gray);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    main_username_delete.setVisibility(View.VISIBLE);
                } else {
                    main_username_delete.setVisibility(View.GONE);
                }
            }
        });
        /*
         * 设置密码文本输入变化监听
         */
        et_psd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    main_pwd_delete.setVisibility(View.VISIBLE);
                } else {
                    main_pwd_delete.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.main_username_delete:
                et_account.setText(null);
                et_psd.setText(null);
                break;
            case R.id.main_pwd_delete:
                et_psd.setText(null);
                break;
            default:
                break;
        }
    }

    /**
     * F18Q932J932
     */
    private void login() {

        btn_login.setEnabled(false);

        HttpUtils.get(HttpUri.LOGIN)
                .dialog(true)
                .data("userName", et_account.getText().toString())
                .data("userPassword", et_psd.getText().toString())
                .data("uuid", uuid)
                .data("loginFrom", "rscapp")
                .data("version", SystemUtils.versionName)
                .data("clientOS", "ANDRIOD")
//				.md5SignUserId(false)
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        btn_login.setEnabled(true);

                        LoginModel model = new Gson().fromJson(jsonObject.toString(), LoginModel.class);
                        if (model != null && model.DataList != null && model.DataList.size() != 0) {
                            LoginUtils.setLoginModel(model.DataList.get(0));

                            LoginUtils.setLoginStatus(Constants.LoginStatus.ONLINE);
                            LoginUtils.setUserStatus(Constants.UserStatus.ONLINE);
                            SharePreferenceUtil.put(mThis, "userName", et_account.getText().toString());
                            SharePreferenceUtil.put(mThis, "passWord", et_psd.getText().toString());
                            startService(new Intent(mThis, HeartService.class));
                            startService(new Intent(mThis, SiteService.class));
                            openActivity(SeclectCarsActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        btn_login.setEnabled(true);
                        ToastUtil.showShort(mThis, response.msg);
                    }
                })
                .request();
    }

    /*
     * 转化成json字符串
     */
    private void changeArrayDateToJson() { // 把一个集合转换成json格式的字符串
        // username = login_account.getText().toString().trim();
        // password = login_pwd.getText().toString().trim();
        // Log.e("userName:", username);
        // Log.e("passWord:", password);
        objLogin = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
        try {
            objLogin.put("user_name", username); // 从集合取出数据，放入JSONObject里面
            objLogin.put("user_pwd", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("login", "转换成json字符: " + objLogin);
    }

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
            ToastUtil.showShort(this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void AllowPermission(String[] permission) {
        for (String per : permission) {
            if (per.equals(permissions[2])) {
                RoadSideCarApplication.getInstance().getLocationService().start();
            } else if (per.equals(permissions[1]) && !Constants.LoginStatus.ONLINE.equals(LoginUtils.getLoginStatus())) {
                apkUpdateUtils.checkVersionRequest();
            }
        }
    }

    @Override
    public void ShowPermission(String[] permission) {
        permissionUtils.proceed(permission);
    }

    @Override
    public void DeniedPermission(String[] permission) {
        finish();
        System.exit(0);
    }

    @Override
    public void NeverAskPermission(String[] permission) {
        permissionUtils.ShowNeverAskDialog("应用权限被禁止，请前往设置中开启！", permission);
    }
}
