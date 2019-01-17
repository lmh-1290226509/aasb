package com.ddadai.basehttplibrary.request;

import android.text.TextUtils;

import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.BaseStringCallBack;
import com.ddadai.basehttplibrary.utils.MD5Util;
import com.ddadai.basehttplibrary.utils.RequestType;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

import okhttp3.MediaType;

/**
 * Created by shi on 2017/2/8.
 */

public abstract class BaseRequest_ {

    protected String httpBaseUrl = HttpUtils.getUrl().getUrl();
//


    public void request(HttpUtils.RequestBuilder_ postRequest_) {
        Request request = null;

        String finalUrl = httpBaseUrl + postRequest_.uri;
        if (postRequest_.uri.contains("http")) {
            finalUrl = postRequest_.uri;
        }


        switch (postRequest_.requestType) {
            case IMG:
            case FILE:
            case POST:
                request = OkGo.post(finalUrl);
                break;
            case GET:
                request = OkGo.get(finalUrl);
                break;
        }
        request.tag(postRequest_.uri);
        request.priority(postRequest_.priority);
//        OkHttpClient okHttpClient = OkGo.getInstance().getOkHttpClient();
//        OkHttpClient.Builder builder=new OkHttpClient.Builder(okHttpClient);
//        okHttpClient1.newBuilder()
//        okHttpClient.interceptors().clear();
//        request.client()

        if (postRequest_.headers != null && !postRequest_.headers.isEmpty()) {
            for (String key : postRequest_.headers.keySet()) {
                request.headers(key, postRequest_.headers.get(key));
            }
        }


        if (postRequest_.files != null && !postRequest_.files.isEmpty()
                && (postRequest_.requestType == RequestType.POST
                ||postRequest_.requestType == RequestType.FILE
               || postRequest_.requestType == RequestType.IMG) ) {
            if (postRequest_.files != null && !postRequest_.files.isEmpty()) {
                for (String key : postRequest_.files.keySet()) {
                    Object obj = postRequest_.files.get(key);
                    File f = null;
                    if (obj == null) {
                        continue;
                    } else if (obj instanceof File) {
                        f = (File) obj;
                    } else if (obj instanceof String) {
                        f = new File(obj.toString());
                    }
                    String mediaType="";
                    if(postRequest_.requestType==RequestType.FILE){
                        mediaType="audio/wav";
                    }else if(postRequest_.requestType==RequestType.IMG){
                        mediaType="text/jpg";
                    }
                    ((PostRequest) request).params(key,f,f.getName(),MediaType.parse(mediaType));
//                    ((PostRequest) request).upBytes(bytes, MEDIA_TYPE_STREAM);
                    break;
                }
            }
        } else {
            //纯文本，不带文件
                switch (postRequest_.requestType) {
                    case POST:
                        if (!TextUtils.isEmpty(postRequest_.signKey)) {
//                            if(postRequest_.onlyKey){
//
//                            } else {
//
//                            }
                            Iterator<String> keys_ = postRequest_.datas.keys();
                            JSONObject json_ = new JSONObject();
                            while (keys_.hasNext()) {
                                String next = keys_.next();
                                try {
                                    json_.put(next, postRequest_.datas.opt(next));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                json_.put("token", MD5Util.getMD5(postRequest_.datas.optString(postRequest_.signKey) + "bPm&,Yun!shANg@aPP%$jiAMi;"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ((PostRequest) request).params("jsonStr", json_.toString());

                        } else {
                            //默认不签名
                            Iterator<String> keys_ = postRequest_.datas.keys();
                            JSONObject json_ = new JSONObject();
                            while (keys_.hasNext()) {
                                String next = keys_.next();
                                try {
                                    json_.put(next, postRequest_.datas.optString(next));
                                    ((PostRequest) request).params(next, postRequest_.datas.optString(next));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

//                        ((PostRequest) request).params("data", json_.toString())
                        ;

                        break;
                    case GET:

                        JSONObject params = null;
                        if (postRequest_.datas != null) {
                            int size = postRequest_.datas.size();
//                if(size==1){
//                    Iterator<String> keys = postRequest_.datas.keys();
//                    String key="";
//                    while (keys.hasNext()){
//                        key= keys.next();
//                    }
//                    (request).params(key,postRequest_.datas.optString(key));
//                    (request).params("token", MD5Util.getMD5(postRequest_.datas.optString(key)+"bPm&,Yun!shANg@aPP%$jiAMi;"));
//                }  else
                            if (!TextUtils.isEmpty(postRequest_.signKey)) {
                                if(postRequest_.onlyKey){
                                    (request).params(postRequest_.signKey, postRequest_.datas.optString(postRequest_.signKey));
                                    (request).params("token", MD5Util.getMD5(postRequest_.datas.optString(postRequest_.signKey) + "bPm&,Yun!shANg@aPP%$jiAMi;"));
                                }else{
                                    Iterator<String> keys = postRequest_.datas.keys();
                                    JSONArray ja = new JSONArray();
                                    while (keys.hasNext()) {

                                        JSONObject json = new JSONObject();
                                        String next = keys.next();
                                        try {
                                            json.put("name", next);
                                            json.put("value", postRequest_.datas.optString(next));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        ja.put(json);
                                    }
                                    (request).params("param", ja.toString());
                                    (request).params("token", MD5Util.getMD5(postRequest_.datas.optString(postRequest_.signKey) + "bPm&,Yun!shANg@aPP%$jiAMi;"));
                                }
                            } else {
                                Iterator<String> keys = postRequest_.datas.keys();
                                JSONArray ja = new JSONArray();
                                while (keys.hasNext()) {

                                    JSONObject json = new JSONObject();
                                    String next = keys.next();
                                    try {
                                        json.put("name", next);
                                        json.put("value", postRequest_.datas.optString(next));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    ja.put(json);
                                }
                                (request).params("param", ja.toString());
                                (request).params("token", MD5Util.getMD5(ja.toString() + "bPm&,Yun!shANg@aPP%$jiAMi;"));
                            }

                        break;
                }

            }

            // 有文件
            if (postRequest_.files != null && !postRequest_.files.isEmpty()) {
                for (String key : postRequest_.files.keySet()) {
                    Object obj = postRequest_.files.get(key);
                    if (obj == null) {
                        continue;
                    } else if (obj instanceof File) {
                        ((PostRequest) request).params(key, (File) obj);
                    } else if (obj instanceof String) {
                        File file = new File(obj.toString());
                        ((PostRequest) request).params(key, file);
                    }
                }
            }
        }


        AbsCallback cb = null;
        BaseStringCallBack callBack = getCallBack();
        if (callBack != null) {
            callBack.setPostRequest_(postRequest_);
            cb = callBack;
        }
        request.execute(cb);
    }

    public abstract BaseStringCallBack getCallBack();


}
