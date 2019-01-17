package com.ddadai.basehttplibrary;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**okHttp响应的包装类，处理下载进度
 * Created by ZephanQ on 2016/5/20.
 */
public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private ProgressResponseListener responseListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody body, ProgressResponseListener listener) {
        responseBody = body;
        responseListener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source){
        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                responseListener.onResponseProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };

    }

    /**
     * 进度回调接口
     */
    public static interface ProgressResponseListener {

        void onResponseProgress(long bytesRead, long contentLength, boolean done);

    }

}