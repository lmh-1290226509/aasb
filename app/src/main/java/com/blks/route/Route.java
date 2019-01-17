package com.blks.route;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by shi on 2017/11/21.
 */

public class Route {




    public static Builder builder(Fragment fragment){
        return new Builder(fragment);
    }
    public static Builder builder(Activity activity){
        return new Builder(activity);
    }
    public static Builder builder(Context context){
        return new Builder(context);
    }


    public static RouteBuilder routeBuilder(Fragment fragment){
        return new RouteBuilder(fragment);
    }
    public static RouteBuilder routeBuilder(Activity activity){
        return new RouteBuilder(activity);
    }
    public static RouteBuilder routeBuilder(Context context){
        return new RouteBuilder(context);
    }


}
