package com.share.noval.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.hutool.json.JSONUtil;

//固定用来传输给前端的格式
/*
 * @param message 说明文字
 * @param result 请求数据的结果
 */
@Component
public class Result {

    //请求成功
    public String success(String message,Object data) {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("state", "1");
        result.put("message", message);
        result.put("data", data.toString());
        return JSONUtil.toJsonStr(result);
    }

    //请求失败
    public String error(String message,Object data){
        Map<String, String> result = new LinkedHashMap<>();
        result.put("state", "0");
        result.put("message", message);
        if(data==null)
            result.put("data", "null");
        else
            result.put("data", data.toString());
        return JSONUtil.toJsonStr(result);        
    }
}
