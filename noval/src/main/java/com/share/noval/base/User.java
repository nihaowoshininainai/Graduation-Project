package com.share.noval.base;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("`User`")
public class User {
    private int uid;
    private LocalDateTime createTime;
    private String pwd;
    private String uname;
}
