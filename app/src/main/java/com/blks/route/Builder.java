package com.blks.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shi on 2017/11/21.
 */

public class Builder {


    protected Intent mIntent;
    protected Context mContext;
    protected Fragment mFragment;
    protected Activity mActivity;

    public Builder(Context context) {
        mContext = context;
        mIntent = new Intent();
    }

    public Builder(Fragment fragment) {
        mFragment = fragment;
        mActivity=mFragment.getActivity();
        mContext=mFragment.getContext();
        mIntent = new Intent();
    }

    public Builder(Activity activity) {
        mActivity=activity;
        mContext=activity;
        mIntent = new Intent();
    }

    public Builder class_(Class cls){
        mIntent.setClass(mContext,cls);
        return this;
    }

    public Builder action(String action) {
        mIntent.setAction(action);
        return this;
    }


    public Builder data(Uri data) {
        mIntent.setData(data);

        return this;
    }

    public Builder flags(int flags) {
        mIntent.addFlags(flags);
        return this;
    }

    public Builder putExtra(String name, String value) {
        mIntent.putExtra(name, value);
        return this;
    }

    public Builder putExtra(String name, int value) {
        mIntent.putExtra(name, value);
        return this;
    }

    public Builder putExtra(String name, Serializable value) {
        if (value == null) {
            return this;
        }
        mIntent.putExtra(name, value);
        return this;
    }

    public Builder putExtra(String name, Parcelable value) {
        if (value == null) {
            return this;
        }
        mIntent.putExtra(name, value);
        return this;
    }

    public Builder putExtra(String name, ArrayList<? extends Parcelable> value) {
        if (value == null) {
            return this;
        }
        mIntent.putParcelableArrayListExtra(name, value);
        return this;
    }

    public Builder putExtra(String name, boolean value) {
        mIntent.putExtra(name, value);
        return this;
    }


    public void startActivity(int requestCode) {
        if (requestCode <= 0) {
            if(mFragment!=null){
                mFragment.startActivity(mIntent);
            }else if(mActivity!=null){
                mActivity.startActivity(mIntent);
            }else if(mContext!=null){
                mContext.startActivity(mIntent);
            }
        } else {
            if(mFragment!=null){
                mFragment.startActivityForResult(mIntent,requestCode);
            }else if(mActivity!=null){
                mActivity.startActivityForResult(mIntent,requestCode);
            }else if(mContext!=null){
                mContext.startActivity(mIntent);
            }
        }
    }


    public void startActivity() {
        startActivity(0);
    }

}
