package com.blks.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shi on 2017/2/22.
 */

public class ParseUtil {


    //转化为int类型
    public static int Int(String number){
        if(TextUtils.isEmpty(number)){
            number="0";
        }
        int i=0;
        try {
            i= Integer.parseInt(number);
        }catch (Exception e){
            e.printStackTrace();
        }
        return i;
    }

    public static double Double(String number){
        if(TextUtils.isEmpty(number)){
            number="0";
        }
        double i=0;
        try {
            i= Double.parseDouble(number);
        }catch (Exception e){
            e.printStackTrace();
        }
        return i;
    }


    //分转化为元
    public  static String Yuan(int fen){
        return Yuan(fen*1.0f/100);
    }
    //分转化为元
    public  static String Yuan(long fen){
        return Yuan(fen*1.0f/100);
    }

    //格式化元，2位小数
    public static String Yuan(double yuan){
        String y="";
        //占位符可以使用0和#两种，当使用0的时候会严格按照样式来进行匹配，不够的时候会补0，而使用#时会将前后的0进行忽略
        DecimalFormat df=new DecimalFormat("0.##");
        try {
            y=df.format(yuan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return y;
    }
    //格式化元，2位小数
    public static String Yuan_00(double yuan){
        String y="";
        //占位符可以使用0和#两种，当使用0的时候会严格按照样式来进行匹配，不够的时候会补0，而使用#时会将前后的0进行忽略
        DecimalFormat df=new DecimalFormat("0.00");
        try {
            y=df.format(yuan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return y;
    }


    public static int Fen(String yuan){
        return (int) (Double(yuan)*100);
    }

    /**
     * 集合转json数组
     * @param list
     * @return
     */
    public static JSONArray List2Json(List list) {

        JSONArray array = new JSONArray();
        Gson gson = new Gson();
        for (int i = 0; i < list.size(); i++) {

            try {
                JSONObject object = new JSONObject(gson.toJson(list.get(i)));
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return array;
    }

    public static <T> List String2List(String jsonStr, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();

        JsonArray array = new JsonParser().parse(jsonStr).getAsJsonArray();
        for (JsonElement jsonElement : array) {
            list.add(gson.fromJson(jsonElement, cls));
        }

        return list;
    }

}
