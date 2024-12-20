package com.share.noval.base;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("`User`")
/*
 * 表名：User
 * @param uid 用户id
 * @param createTime 账号创建时间
 * @param pwd 用户密码
 * @param uname 用户名
 */
public class User {
    @TableId(type = IdType.AUTO)
    private Integer uid;
    private LocalDateTime createTime;
    private String pwd;
    private String uname;
    public User(String uname,String pwd){
        this.uname = uname;
        this.pwd = pwd;
        this.createTime = LocalDateTime.now();
    }
}
