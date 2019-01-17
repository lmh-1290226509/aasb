package com.blks.customer;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by limh on 2018/2/24.
 */

public class DownloadProgressDialog extends android.app.ProgressDialog {

    public DownloadProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setProgressStyle(STYLE_HORIZONTAL);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setTitle("版本更新");
        setMessage("安装包正在下载中，请稍候");
        setMax(100);
        super.onCreate(savedInstanceState);
    }
}
