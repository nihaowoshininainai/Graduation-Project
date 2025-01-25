package com.share.novel.base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.hutool.json.JSONUtil;
import lombok.Data;

@Data
@TableName("`User`")
/*
 * 表名：User
 * @param uid 用户id
 * @param createTime 账号创建时间
 * @param pwd 用户密码
 * @param uname 用户名
 * @param banned 封禁状态，0是正常，1是封禁
 */
public class User {
    @TableId(type = IdType.AUTO)
    private Integer uid;
    private String createTime;
    private String pwd;
    private String uname;
    private Integer banned;
    public User(){}
    public User(String uname,String pwd){
        this.uname = uname;
        this.pwd = pwd;
        this.createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString(){
        Map<String,String> map = new HashMap<>();
        map.put("uid", uid.toString());
        map.put("createTime", createTime);
        map.put("pwd", pwd);
        map.put("uname", uname);
        return JSONUtil.toJsonStr(map);
    }
}
