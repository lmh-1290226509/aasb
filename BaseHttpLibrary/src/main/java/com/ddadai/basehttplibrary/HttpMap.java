package com.ddadai.basehttplibrary;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpMap {


    private JSONObject jsonObject;
    private List<KeyValue> models;
    private Map<String,Object> map;

    public HttpMap(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT_WATCH){
            jsonObject=new JSONObject();
        }else{
            models=new ArrayList<>();
            map=new HashMap<>();
        }
    }


    public void put(String key,Object value){
        if(jsonObject!=null){
            try {
                jsonObject.put(key,value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(models!=null){
            map.put(key,value);
            models.add(new KeyValue(key,value));
        }
    }


    public int size(){
        int size=0;
        if(jsonObject!=null){
            size=jsonObject.length();
        }else if (models!=null){
            size=models.size();
        }
        return size;
    }

    public Iterator<String> keys(){
        Iterator<String> keys=null;
        if(jsonObject!=null){
            keys=jsonObject.keys();
        }else if(models!=null){
            List<String> strs=new ArrayList<>();
            for (KeyValue m:
                 models) {
                strs.add(m.key);
            }
            keys=strs.iterator();

        }
        return keys;
    }


    public String optString(String key){
        if(jsonObject!=null){
            return jsonObject.optString(key);
        }else if(models!=null){
            return map.get(key)+"";
        }else{
            return "";
        }
    }
    public Object opt(String key){
        if(jsonObject!=null){
            return jsonObject.opt(key);
        }else if(models!=null){
            return map.get(key);
        }else{
            return "";
        }
    }


    private class KeyValue{
        String key;
        Object value;

        public KeyValue(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

}
