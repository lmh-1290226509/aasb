package com.blks.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;

import com.blks.customer.DownloadProgressDialog;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.VersionModel;
import com.ddadai.basehttplibrary.HttpUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;

public class ApkUpdateUtils {

    Context context;
    boolean showToast;
    private File file;

    public ApkUpdateUtils(Context context, boolean showToast) {
        this.context = context;
        this.showToast = showToast;
    }

    /**
     * 检查版本更新
     */
    public void checkVersionRequest(){
        HttpUtils.get(HttpUri.GET_VERSION_INFO)
                .dialog(showToast)
                .data("verSystem","rscapp")
                .onlyKey("verSystem")
                .callBack(new JsonRequestCallBack(context) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        VersionModel vm=new Gson().fromJson(jsonObject.toString(),VersionModel.class);
                        if(vm.DataList!=null&&vm.DataList.size()!=0){
                            final VersionModel.DataListModel model = vm.DataList.get(0);
                            int newCode;
                            try{
                                newCode = Integer.valueOf(model.VERSION_NO);
                            } catch (Exception e) {
                                newCode = SystemUtils.versionCode;
                                e.printStackTrace();
                            }
                            if(SystemUtils.versionCode < newCode){
                                if("1".equals(model.IS_FORCE)){
                                    showDialogForUpdate(true,model);
                                }else{
                                    showDialogForUpdate(false,model);
                                }
                            }else{
                                if (showToast) {
                                    ToastUtil.showShort(context, "亲，已经是最新版本了");
                                }
                            }
                        }
                    }
                })
                .request();
    }


    private void showDialogForUpdate(boolean isForce, final VersionModel.DataListModel model){
        if(isForce){
            DownloadAPK(model.VERSION_PACKAGE_URL);
            return;
        }
        AlertDialog.Builder dialog=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            dialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        } else {
            dialog = new AlertDialog.Builder(context);
        }
        dialog.setTitle("温馨提示")
                .setMessage(model.VERSION_CONTENT)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        DownloadAPK(model.VERSION_PACKAGE_URL);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    /**
     * APK下载
     * @param url
     */
    private void DownloadAPK(final String url) {

        int theme;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            theme = android.R.style.Theme_Material_Light_Dialog_Alert;
        else if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            theme = ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT;
        else
            theme = ProgressDialog.THEME_HOLO_LIGHT;

        final DownloadProgressDialog progressDialog = new DownloadProgressDialog(context, theme);
        progressDialog.show();


        HttpUtils.downloadFile(url, new HttpUtils.OnDownloadListener() {
            @Override
            public void downloadSuccess(File file) {
                progressDialog.dismiss();
                ApkUpdateUtils.this.file = file;
                installApp();
            }

            @Override
            public void downloadFailed(int code, String reason) {
                progressDialog.dismiss();
                ToastUtil.showLong(context, reason);
//                if (code == 1) {
//                    Uri uri = Uri.parse(url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            uri);
//                    if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
//                                != null) {
//                        context.startActivity(intent);
//                    }
//
//                }
            }

            @Override
            public void downloadProgress(long progress) {
                progressDialog.setProgress(Long.valueOf(progress).intValue());
            }
        });

    }

    /**
     * app安装
     */
    private void installApp(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //6.0及以下跳转安装界面
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            //7.0及以上
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.blks.antrscapp.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            //兼容8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    ToastUtil.showLong(context, "请在设置中允许安装未知来源应用");
                    startInstallPermissionSettingActivity();
                    return;
                }
            }
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        new AlertDialog.Builder(context)
                .setTitle("温馨提示")
                .setMessage("应用下载完成")
                .setCancelable(false)
                .setPositiveButton("安装", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        installApp();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
