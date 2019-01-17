package com.blks.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by shi on 2017/3/26.
 */

public class SerializableMap implements Serializable {
    Map<String,Object> map;

    public SerializableMap(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
