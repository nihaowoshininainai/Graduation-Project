package com.share.novel.utils;

import java.util.HashMap;
import java.util.Map;

public class TransformUtil {

    //将字符串转换为map
    public static Map<String,String> strToMap(String cookie,String first,String second){
        Map<String,String> map = new HashMap<>();
        String[] split = cookie.split(first);
        for (int i = 0; i < split.length; i++) {
            String[] splitd = split[i].split(second);
            map.put(splitd[0], splitd[1]);
        }

        return map;
    }
}
