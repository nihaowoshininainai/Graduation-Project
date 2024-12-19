package com.share.noval.base;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
/*
 * 表名：User
 * uid:用户id
 * createTime:账号创建时间
 * pwd:用户密码
 * uname:用户名
 */
@TableName("`User`")
public class User {
    private int uid;
    private LocalDateTime createTime;
    private String pwd;
    private String uname;
}
