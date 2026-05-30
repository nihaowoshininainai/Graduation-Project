package com.share.novel.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Result<T> {

    private String state;
    private String message;
    private T data;

    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.state = "1";
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(String message, T data) {
        Result<T> result = new Result<>();
        result.state = "0";
        result.message = message;
        result.data = data;
        return result;
    }

    public boolean isSuccess() {
        return "1".equals(state);
    }

    public String toJson() {
        return JSONUtil.toJsonStr(this);
    }

    public static Result<Object> fromJson(String json) {
        JSONObject obj = JSONUtil.parseObj(json);
        Result<Object> result = new Result<>();
        result.state = obj.getStr("state");
        result.message = obj.getStr("message");
        result.data = obj.get("data");
        return result;
    }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
