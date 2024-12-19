package com.share.noval.utils;


//固定用来传输给前端的格式
/*
 * @param state 状态值，1为请求成功，0为失败
 * @param message 说明文字
 * @param result 请求数据的结果
 */
public class Result {
    private Integer state;
    private String message;
    private Object result;
    public Result(Integer state,String message,Object result){
        if(state!=1&&state!=0)
            System.out.println("不合法输入，请输入0：代表请求失败；1：代表成功");
        else{
            this.state = state;
            this.message = message;
            this.result = result;
        }
    }
}
