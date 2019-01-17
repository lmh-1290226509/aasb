package com.ddadai.basehttplibrary.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by shi on 2017/1/17.
 */

public class UriCache {

    List<String> uris;
    public UriCache(){
        uris=new ArrayList<String>();
    }

    public void addUri(String uri){
        uris.add(uri);
    }

    public List<String> getUris(){
        return  uris;
    }

    public void clear(){
        uris.clear();
    }



}
