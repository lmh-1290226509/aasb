package com.blks.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.blks.antrscapp.R;
import com.blks.app.CustomDialog;

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
    private CustomDialog permissionDialog;

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

            if (permission.equals(Manifest.permission.CAMERA)) {
                if (checkCameraPermission()) {
                    if (resultListener != null)
                        resultListener.AllowPermission(new String[]{permission});
                } else {
                    if (resultListener != null)
                        resultListener.DeniedPermission(new String[]{permission});
                }
            } else {
                //已授权
                if (resultListener != null)
                    resultListener.AllowPermission(new String[]{permission});
            }

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

        List<String> allowList= new ArrayList<>();
        List<String> requestList = new ArrayList<>();
        List<String> showList = new ArrayList<>();

        //6.0以下默认已经授予权限
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //判断是否包含camera权限
            for (String per:permission) {
                if (per.equals(Manifest.permission.CAMERA) && !checkCameraPermission()) {
                    if (resultListener != null) {
                        resultListener.DeniedPermission(new String[] {per});
                    }
                } else {
                    allowList.add(per);
                }
            }

            if (resultListener != null && !allowList.isEmpty()) {
                resultListener.AllowPermission(allowList.toArray(new String[allowList.size()]));
            }
            return;
        }

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
     * 6.0
     * @return
     */
    private boolean checkCameraPermission() {
        /**
         * 通过尝试打开相机的方式判断有无拍照权限
         * @return
         */
        boolean isCanUse = true;
        Camera mCamera  = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * @param isNeverAsk 是否为勾选不再提示
     * @param msg 提示文案
     * @param permission 权限
     */
    private void ShowDialog(final boolean isNeverAsk, String msg, final String[] permission) {
        if (permissionDialog == null) {
            permissionDialog = new CustomDialog(mContext, R.style.mystyle, new CustomDialog.CustomDialogListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.confirm_btn:// 确定
                            if (isNeverAsk) {
                                IntentUtil.gotoAppSetting(mContext);
                            } else {
                                proceed(permission);
                            }
                            permissionDialog.dismiss();
                            break;
                        case R.id.cancel_btn:// 取消
                            if (!isNeverAsk) {
                                cancel(permission);
                            }
                            permissionDialog.dismiss();
                            break;
                    }
                }
            });
        }

        permissionDialog
                .SetMessage(msg)
                .SetCanceledOnTouchOutside(false)
                .show();
    }

    public void ShowDialog(String msg, String[] permission) {
        ShowDialog(false, msg, permission);
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
