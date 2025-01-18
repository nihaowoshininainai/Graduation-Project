package com.share.novel.utils;

import java.util.Map;

public class ControllerUtil {
    
    //专用于cookie字符串转map
    public static Map<String,String> strToMap(String cookie){
        return TransformUtil.strToMap(cookie, ";", "=");
    }
}
