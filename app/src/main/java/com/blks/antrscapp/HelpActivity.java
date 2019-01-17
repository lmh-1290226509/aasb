package com.blks.antrscapp;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blks.app.BaseActivity;

public class HelpActivity extends BaseActivity implements OnClickListener {

	private ImageView img_back;
	private WebView webView;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_help);
		initView();// 初始化数据
	}

	private void initView() {
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		String url = "file:///android_asset/workflow.html";
		progressBar = (ProgressBar) findViewById(R.id.progress);
		webView = (WebView) findViewById(R.id.web_view);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		//支持javascript
		webView.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		webView.getSettings().setSupportZoom(true);
		// 设置出现缩放工具
		webView.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		webView.getSettings().setUseWideViewPort(true);
		//自适应屏幕
		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);



		//https 验证
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new CustomWebViewClient());
		webView.loadUrl(url);

	}

	private class CustomWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			//https 验证
			handler.proceed();//接受证书
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;

		default:
			break;
		}
	}
}
