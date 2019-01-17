package com.blks.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by shi on 16/8/29.
 */
public class SharedPreferencesUtil {

    private static SharedPreferences sp;

    private static String spName = "com.woma.rescue";


    private static Context mContext;

    public static void init(Context context){
        mContext=context;
    }

    private static SharedPreferences getDefaultSharedPreferences() {
        return getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences(String name, int mode) {
        return mContext.getSharedPreferences(name, mode);
    }

    public static void put(String key, Object obj) {
        SharedPreferences sp = getDefaultSharedPreferences();
        SharedPreferences.Editor edit = sp.edit();

        if (obj == null&& key!=null) {
            edit.putString(key,saveObject(null));
        }else if (obj == null) {

        }  else if (obj instanceof Boolean) {
            edit.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            edit.putFloat(key, (Float) obj);
        } else if (obj instanceof Long) {
            edit.putLong(key, (Long) obj);
        } else if (obj instanceof Integer) {
            edit.putInt(key, (Integer) obj);
        } else if (obj instanceof String) {
            edit.putString(key, obj.toString());
        } else if (obj != null) {
            edit.putString(key,saveObject(obj));
        }
        edit.commit();
    }




    private static String saveObject(Object object){
        String str="";
        ByteArrayOutputStream toByte = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(toByte);
            oos.writeObject(object);
            str = new String(Base64.encode(toByte.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (toByte != null)
                    toByte.close();
                if (oos != null)
                    oos.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return str;
    }


    public static int get(String s, int defaultValue) {
        return getDefaultSharedPreferences().getInt(s, defaultValue);
    }

    public static float get(String s, float defaultValue) {
        return getDefaultSharedPreferences().getFloat(s, defaultValue);
    }

    public static long get(String s, long defaultValue) {
        return getDefaultSharedPreferences().getLong(s, defaultValue);
    }

    public static String get(String s, String defaultValue) {
        return getDefaultSharedPreferences().getString(s, defaultValue);
    }

    public static boolean get(String s, boolean defaultValue) {
        return getDefaultSharedPreferences().getBoolean(s, defaultValue);
    }



    public static <T> T get(String key, T t) {
        String object = get(key,"");
        if(TextUtils.isEmpty(object)){
            return null;
        }
        byte[] base64Bytes = Base64.decode(object);
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            bais = new ByteArrayInputStream(base64Bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (obj != null) {
            return (T) obj;
        } else {
            return null;
        }
    }

    public static void clear() {//清楚所有数据
        SharedPreferences sp = getDefaultSharedPreferences();
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

}
