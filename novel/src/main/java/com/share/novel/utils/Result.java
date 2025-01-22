package com.share.novel.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

//固定用来传输给前端的格式
public class Result {

    //请求成功
    /*
     * @param message 说明文字
     * @param data 需要JSON格式化的数据
     * @returns result 请求数据的结果
     */
    public static String success(String message,Object data) {
        Map<String, Object> result = new TreeMap<>();
        result.put("state", "1");
        result.put("message", message);
        result.put("data", data);
        return JSONUtil.toJsonStr(result);
    }

    //请求失败
    /*
     * @param message 说明文字
     * @param data 需要JSON格式化的数据
     * @returns result 请求数据的结果
     */
    public static String error(String message,Object data){
        Map<String, Object> result = new TreeMap<>();
        result.put("state", "0");
        result.put("message", message);
        if(data==null)
            result.put("data", "null");
        else
            result.put("data", data);
        return JSONUtil.toJsonStr(result);        
    }

    /*
     * 将json字符串中属性提取出来，存入list传出
     */
    public static List<String> jsonStrtoList(String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String state = jsonObject.getStr("state");
        String message = jsonObject.getStr("message");
        String data = jsonObject.getStr("data");
        List<String> list = new ArrayList<>();
        list.add(state);
        list.add(message);
        list.add(data);
        return list;
    }
}
