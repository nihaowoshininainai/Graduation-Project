package com.share.noval.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import cn.hutool.json.JSONUtil;

//固定用来传输给前端的格式
/*
 * @param message 说明文字
 * @param result 请求数据的结果
 */
public class Result {

    private String message;
    private Object result;

    public Result(String message, Object result) {
        this.message = message;
        this.result = result;
    }

    //请求成功
    public String success() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("state", "1");
        result.put("message", this.message);
        result.put("result", this.result.toString());
        return JSONUtil.toJsonStr(result);
    }

    //请求失败
    public String error(){
        Map<String, String> result = new LinkedHashMap<>();
        result.put("state", "0");
        result.put("message", this.message);
        if(this.result==null)
            result.put("result", "null");
        else
            result.put("result", this.result.toString());
        return JSONUtil.toJsonStr(result);        
    }
}
