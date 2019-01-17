package com.yongchun.library.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个权限请求
 * Created by limh on 2017/6/23.
 */

public class PermissionUtils {

    private static final int REQUEST_PERMISSION_CODE = 13;
    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;
    private PermissionResultListener resultListener;
    private AlertDialog.Builder dialogBuilder;

    /**
     * 需要实现PermissionResultListener接口
     * @param mContext
     */
    public PermissionUtils(Activity mContext) {
        mActivity = mContext;
        this.mContext = mContext;
        this.resultListener = (PermissionResultListener) mContext;
    }

    public PermissionUtils(Fragment mContext) {
        mFragment = mContext;
        this.mContext = mContext.getActivity();
        this.resultListener = (PermissionResultListener) mContext;
    }

    public void Destroy() {
        if (mActivity != null)
            mActivity = null;
        if (mFragment != null)
            mFragment = null;
        if (resultListener != null)
            resultListener = null;
    }

    /**
     * 检查是否需要向用户解释为什么需要这个权限
     * @param permission
     * @return
     */
    private boolean shouldShowRequestPermissionRationale(String permission) {
        if (mFragment != null)
            return mFragment.shouldShowRequestPermissionRationale(permission);
        else
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission);
    }

    /**
     * 权限请求部分
     * @param permission
     */
    private void requestPermissions(String[] permission) {
        if (mFragment != null)
            mFragment.requestPermissions(permission, REQUEST_PERMISSION_CODE);
        else
            ActivityCompat.requestPermissions((Activity) mContext, permission,
                    REQUEST_PERMISSION_CODE);
    }

    /**
     * 检查权限是否授权并做出相应处理
     * @param permission
     */
    public void RequestPermission(String permission) {
        //6.0以下默认已经授予权限
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //已授权
            if (resultListener != null)
                resultListener.AllowPermission(new String[]{permission});
            return;
        }

        if (ContextCompat.checkSelfPermission(mContext, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (shouldShowRequestPermissionRationale(permission)) {
                if (resultListener != null)
                    resultListener.ShowPermission(new String[]{permission});
            } else {
                //请求权限
                requestPermissions(new String[]{permission});
            }
        } else {
            //已授权
            if (resultListener != null)
                resultListener.AllowPermission(new String[]{permission});
        }
    }

    /**
     * 多个权限
     * @param permission
     */
    public void RequestPermission(String[] permission) {
        //6.0以下默认已经授予权限
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //已授权
            if (resultListener != null)
                resultListener.AllowPermission(permission);
            return;
        }

        List<String> allowList= new ArrayList<>();
        List<String> requestList = new ArrayList<>();
        List<String> showList = new ArrayList<>();

        for (String per: permission) {
            if (ContextCompat.checkSelfPermission(mContext, per)
                    != PackageManager.PERMISSION_GRANTED) {
                // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
                // 向用户解释为什么需要这个权限
                if (shouldShowRequestPermissionRationale(per)) {
                    showList.add(per);
                } else {
                    //请求权限
                    requestList.add(per);
                }
            } else {
                //已授权
                allowList.add(per);
            }
        }

        if (resultListener != null) {
            if (!allowList.isEmpty())
                resultListener.AllowPermission(allowList.toArray(new String[allowList.size()]));
            if (!showList.isEmpty())
                resultListener.ShowPermission(showList.toArray(new String[showList.size()]));
            if (!requestList.isEmpty())
                requestPermissions(requestList.toArray(new String[requestList.size()]));
        }


    }

    /**
     * 权限请求回调结果处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void OnRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            List<String> allowList= new ArrayList<>();
            List<String> deniedList = new ArrayList<>();
            List<String> neverAskList = new ArrayList<>();

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //授权成功
                    allowList.add(permissions[i]);
                } else if (!shouldShowRequestPermissionRationale(permissions[i])){
                    //用户勾选了不再询问
                    neverAskList.add(permissions[i]);
                } else {
                    //拒绝授权
                    deniedList.add(permissions[i]);
                }
            }
            if (resultListener != null) {
                if (!allowList.isEmpty()) {
                    resultListener.AllowPermission(allowList.toArray(new String[allowList.size()]));
                }
                if (!deniedList.isEmpty()) {
                    resultListener.DeniedPermission(deniedList.toArray(new String[deniedList.size()]));
                }
                if (!neverAskList.isEmpty()) {
                    resultListener.NeverAskPermission(neverAskList.toArray(new String[neverAskList.size()]));
                }
            }

        }
    }

    /**
     * @param isNeverAsk 是否为勾选不再提示
     * @param msg 提示文案
     * @param permission 权限
     */
    private void ShowDialog(final boolean isNeverAsk, String msg, final String[] permission) {
        if (dialogBuilder == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                dialogBuilder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
            else
                dialogBuilder = new AlertDialog.Builder(mContext);

            dialogBuilder.setCancelable(false)
                    .setTitle("温馨提示");
        }

        dialogBuilder.setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isNeverAsk)
                            gotoAppSetting(mContext);
                        else
                            proceed(permission);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        if (!isNeverAsk)
                            cancel(permission);
                    }
                })
                .show();
    }

    public void ShowDialog(String msg, String[] permission) {
        ShowDialog(false, msg, permission);
    }

    /**
     * 跳转到APP权限设置界面
     * @param context
     */
    public static void gotoAppSetting(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        ((Activity)context).startActivity(localIntent);
    }

    /**
     * 不再提示
     * @param msg 提示文案
     * @param permission 权限
     */
    public void ShowNeverAskDialog(String msg, String[] permission) {
        ShowDialog(true, msg, permission);
    }

    /**
     * 继续权限请求
     * @param per
     */
    public void proceed(String[] per) {
        if (mContext == null)
            return;
        requestPermissions(per);
    }

    /**
     * 拒绝权限请求
     */
    public void cancel(String[] per) {
        if (resultListener != null)
            resultListener.DeniedPermission(per);
    }

    public interface PermissionResultListener{
        /**
         * 已授权
         */
        void AllowPermission(String[] permission);

        /**
         * 用户拒绝后再次请求提示
         */
        void ShowPermission(String[] permission);

        /**
         * 用户拒绝
         */
        void DeniedPermission(String[] permission);

        /**
         * 用户勾选不再提示
         */
        void NeverAskPermission(String[] permission);
    }

}
