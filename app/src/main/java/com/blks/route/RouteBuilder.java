package com.blks.route;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;

/**
 * Created by shi on 2017/11/21.
 */

public class RouteBuilder extends Builder {

    protected String scheme="";
    protected String host="";
    protected String path="";
//    protected String

    public RouteBuilder(Context context) {
        super(context);
    }

    public RouteBuilder(Fragment fragment) {
        super(fragment);
    }

    public RouteBuilder(Activity activity) {
        super(activity);
    }


    public RouteBuilder scheme(String scheme){
        this.scheme=scheme;
        return this;
    }

    public RouteBuilder host(String host){
        this.host=host;
        return this;
    }
    public RouteBuilder path(String path){
        this.path=path;
        return this;
    }

    public RouteBuilder uri(){
        data(Uri.parse(scheme+"://"+host+"/"+path));
        return this;
    }

}
