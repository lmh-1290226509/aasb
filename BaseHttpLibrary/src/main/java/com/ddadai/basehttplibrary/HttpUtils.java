package com.ddadai.basehttplibrary;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.ddadai.basehttplibrary.callback.OnLoginTimeOutListener;
import com.ddadai.basehttplibrary.callback.OnRequestCallBack_;
import com.ddadai.basehttplibrary.callback.VersionCheckCallBack;
import com.ddadai.basehttplibrary.interfaces.CheckSignInterface;
import com.ddadai.basehttplibrary.interfaces.HttpInterface;
import com.ddadai.basehttplibrary.interfaces.SignInterface;
import com.ddadai.basehttplibrary.request.BaseRequest_;
import com.ddadai.basehttplibrary.request.BaseUrl;
import com.ddadai.basehttplibrary.utils.ContentType;
import com.ddadai.basehttplibrary.utils.FileUtil;
import com.ddadai.basehttplibrary.utils.RequestType;
import com.ddadai.basehttplibrary.utils.ShowErrorType;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shi on 2017/10/11.
 */

public class HttpUtils {

    public static final String RSP_CODE = "IsSuccess";
    public static final String RSP_DESC = "ErrCode";
//    public static final String RSP_NO = "resultNo";

    private static BaseUrl mBaseUrl;
    private static HttpInterface mHttpInterface;
    private static SignInterface mSignInterface;
    private static CheckSignInterface mCheckSignInterface;
    private static OnLoginTimeOutListener mOnLoginTimeOutListener;
    private static OkHttpClient mOkHttpClient;

    public static void init(Application application){
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type","application/json");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.NONE);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(application)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier());

        mOkHttpClient = builder.build();
//        mOkHttpClient.dispatcher().setMaxRequests(96);//最大并发数
        mOkHttpClient.dispatcher().setMaxRequestsPerHost(10);//每个主机最大请求数

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(application)                           //必须调用初始化
                .setOkHttpClient(mOkHttpClient)               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数

    }
    public static void initBaseUrl(BaseUrl baseUrl){
        mBaseUrl=baseUrl;
    }

    public static BaseUrl getUrl(){
        return mBaseUrl;
    }

    public static void initHttpInterfaces(HttpInterface httpInterface){
        mHttpInterface=httpInterface;
    }
    public static void initCheckSignInterface(CheckSignInterface checkSignInterface){
        mCheckSignInterface=checkSignInterface;
    }

//    public static HttpInterface getHttpInterface(){
//        return mHttpInterface;
//    }


    public static void initSignInterface(SignInterface signInterface){
        mSignInterface=signInterface;
    }


    public static void initOnLoginTimeOutListener(OnLoginTimeOutListener l){
        mOnLoginTimeOutListener=l;
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private static class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private static class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
    }




    public static RequestBuilder_ baidu() {
        return new RequestBuilder_(getUrl().getBaiduUrl(), RequestType.POST);
    }
    public static RequestBuilder_ get(String uri) {
        return new RequestBuilder_(uri,RequestType.GET);
    }
    public static RequestBuilder_ img() {
        return new RequestBuilder_(getUrl().getFileUrl(),RequestType.IMG);
    }
    public static RequestBuilder_ file() {
        return new RequestBuilder_(getUrl().getFileUrl(),RequestType.FILE);
    }
    public static RequestBuilder_ headImg() {
        return new RequestBuilder_(getUrl().getHeadImgUrl(),RequestType.IMG);
    }

    public static RequestBuilder_ postImageInfo() {
        return new RequestBuilder_(getUrl().postImgInfoUrl(),RequestType.POST);
    }

    public static class RequestBuilder_ {


        public String uri;
        public HttpMap datas;
        public Map<String, Object> files;
        public Map<String, String> headers;
        public boolean isShowDialog = true;
        public String dialogMsg;
        public OnRequestCallBack_ httpCallBack;
        public String tag;
        public ContentType contentType;
        public boolean isLog=true;
        public OnLoginTimeOutListener onLoginTimeOutListener;
        public VersionCheckCallBack versionCheckCallBack;
        public ShowErrorType showErrorType= ShowErrorType.SHOW_TOAST;
        public boolean token=true;
        public RequestType requestType;
        public SignInterface signInterface;
        public CheckSignInterface checkSignInterface;
        public String signKey="";
        public boolean onlyKey=true;
        public int priority = 10;//优先级默认10

        public RequestBuilder_(String uri, RequestType requestType) {
            this.uri = uri;
            headers=new HashMap<>();
            datas=new HttpMap();
            files = new HashMap<>();
            contentType(ContentType.JSON);
            this. requestType=requestType;
            signInterface=mSignInterface;
            checkSignInterface=mCheckSignInterface;
            onLoginTimeOutListener=mOnLoginTimeOutListener;
        }



        public RequestBuilder_ contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }
        public RequestBuilder_ headers(String key, String value) {
            if(TextUtils.isEmpty(key)||TextUtils.isEmpty(value)){
                return this;
            }
            headers.put(key,value);
            return this;
        }

        public RequestBuilder_ headers(Map<String,String> headers) {
            if(headers==null){
                return this;
            }
            this.headers.putAll(headers);
            return this;
        }
        public RequestBuilder_ token(boolean needToken) {
            this.token = needToken;
            return this;
        }
        public RequestBuilder_ showErrorType(ShowErrorType showErrorType) {
            this.showErrorType = showErrorType;
            return this;
        }
        public RequestBuilder_ log(boolean isLog) {
            this.isLog = isLog;
            return this;
        }


        public RequestBuilder_ data(String key, Object value) {
            if(value!=null){
                datas.put(key, value);
            }
            return this;
        }
        public RequestBuilder_ onlyKey(String signKey) {
            this.signKey=signKey;
            onlyKey=true;
            return this;
        }
        public RequestBuilder_ mulKey(String signKey) {
            this.signKey=signKey;
            onlyKey=false;
            return this;
        }
        public RequestBuilder_ priority(int priority) {
            this.priority=priority;
            return this;
        }

        public RequestBuilder_ file(String key, Object value) {
            files.put(key, value);
            return this;
        }
        public RequestBuilder_ sign(SignInterface signInterface) {
            this.signInterface=signInterface;
            return this;
        }


        public RequestBuilder_ file(Map<String, Object> files) {
            if (files != null) {
                this.files.putAll(files);
            }
            return this;
        }

        public RequestBuilder_ dialogMsg(String dialogMsg) {
            this.dialogMsg = dialogMsg;
            return this;
        }

        public RequestBuilder_ dialog(boolean isShowDialog) {
            this.isShowDialog = isShowDialog;
            return this;
        }

        public RequestBuilder_ callBack(OnRequestCallBack_ cb) {
            this.httpCallBack = cb;
            return this;
        }
        public RequestBuilder_ loginTimeOut(OnLoginTimeOutListener onLoginTimeOutListener) {
            this.onLoginTimeOutListener = onLoginTimeOutListener;
            return this;
        }


        public RequestBuilder_ versionCheck(VersionCheckCallBack versionCheckCallBack) {
            this.versionCheckCallBack = versionCheckCallBack;
            return this;
        }



        public RequestBuilder_ tag(String tag) {
            this.tag = tag;
            return this;
        }


        public void request() {
            BaseRequest_ request = null;
            if(requestType==RequestType.FILE){
                contentType=ContentType.FILE;
            }else if(requestType==RequestType.IMG){
                contentType=ContentType.IMG;
            }
            switch (contentType) {
                case XML:
//                    request=new XmlPost();
                    break;
                case JSON:
                    request=new JsonPost();
                    break;
                case TEXT:
                    request=new TextPost();
                    break;
                case FILE:
                case IMG:
                    request=new FilePost();
                    break;
            }
            if(request!=null){
                if(mHttpInterface!=null){
                    mHttpInterface.baseHttpParams(datas);
                }
                request.request(this);
            }
        }
    }


    /**
     * 带进度的文件下载
     * @param url
     * @param downloadListener
     */
    public static void downloadFile(final String url, final OnDownloadListener downloadListener) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        final ProgressResponseBody.ProgressResponseListener responseListener = new ProgressResponseBody
                .ProgressResponseListener() {
            @Override
            public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                double p = bytesRead * 1.0 / contentLength * 100;
                long percent = Double.valueOf(p).longValue();
                executeCallBack(OnDownloadListener.PROGRESS, downloadListener,
                        null, percent, 0, "");
            }
        };

        OkHttpClient client = mOkHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response.newBuilder()
                        .body(new ProgressResponseBody(response.body(), responseListener))
                        .build();
            }
        }).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                executeCallBack(OnDownloadListener.FAILURE, downloadListener,
                        null, 0, 0, "网络连接超时或网络不畅通");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!FileUtil.isExternalStorageWritable()) {
                    executeCallBack(OnDownloadListener.FAILURE, downloadListener,
                            null, 0, 1, "下载失败,手机存储不可用");
                    return;
                }
                if (!FileUtil.MemoryAvailable(response.body().contentLength())) {
                    executeCallBack(OnDownloadListener.FAILURE, downloadListener,
                            null, 0, 0, "下载失败,手机存储空间不足");
                    return;
                }

                String fileName = url.substring(url.lastIndexOf("/") + 1);
                File saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!saveDir.exists())
                    if (!saveDir.mkdirs()) {
//                        .getInstance().showToast("下载文件夹创建失败");
                        return;
                    }

                try {
                    InputStream is = response.body().byteStream();
                    File file = new File(saveDir, fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int length = 0;

                    while ((length = is.read(buf)) != -1) {
                        fos.write(buf, 0, length);
                    }
                    fos.flush();
                    fos.close();
                    is.close();

                    executeCallBack(OnDownloadListener.SUCCESS, downloadListener,
                            file, 0, 0, "");
                } catch (IOException e) {
                    e.printStackTrace();
                    executeCallBack(OnDownloadListener.FAILURE, downloadListener,
                            null, 0, 1, "下载失败,应用存储权限未开启");
                }

            }
        });

    }

    /**
     * 下载结果
     */
    public interface OnDownloadListener {
        int FAILURE = 0;
        int SUCCESS = 1;
        int PROGRESS = 2;
        void downloadSuccess(File file);
        void downloadFailed(int code, String reason);
        void downloadProgress(long progress);
    }

    /**
     * 下载线程通过handler更新界面
     * @param downloadState
     * @param downloadListener
     * @param file
     * @param progress
     * @param code
     * @param message
     */
    private static void executeCallBack(int downloadState, final OnDownloadListener downloadListener,
                                        final File file, final long progress, final int code, final String message) {

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (downloadListener != null) {

                    if (downloadListener instanceof Activity) {
                        if (((Activity)downloadListener).isFinishing()) {
                            return;
                        }
                    }
                    if (downloadListener instanceof Fragment) {
                        if (((Fragment)downloadListener).getActivity().isFinishing()) {
                            return;
                        }
                    }

                    if (msg.what == OnDownloadListener.SUCCESS) {
                        downloadListener.downloadSuccess(file);
                    }
                    if (msg.what == OnDownloadListener.FAILURE) {
                        downloadListener.downloadFailed(code, message);
                    }
                    if (msg.what == OnDownloadListener.PROGRESS) {
                        downloadListener.downloadProgress(progress);
                    }
                }
            }
        };
        handler.sendEmptyMessage(downloadState);
    }

}
