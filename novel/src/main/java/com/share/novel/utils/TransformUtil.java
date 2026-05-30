package com.share.novel.utils;

import java.util.HashMap;
import java.util.Map;

public class TransformUtil {

    //将字符串转换为map
    public static Map<String,String> strToMap(String cookie,String first,String second){
        Map<String,String> map = new HashMap<>();
        if(cookie == null || cookie.isEmpty()){
            return map;
        }
        String[] split = cookie.split(first);
        for (String s : split) {
            String trimmed = s.trim();
            if(trimmed.isEmpty()){
                continue;
            }
            String[] splitd = trimmed.split(second, 2);
            if(splitd.length == 2){
                map.put(splitd[0].trim(), splitd[1].trim());
            }
        }

        return map;
    }
}
