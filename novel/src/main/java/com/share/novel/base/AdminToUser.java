package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("AdminToUser")
public class AdminToUser {
    private Integer id;
    private Integer aid;
    private Integer uid;
    private String createTime;
    private String record;
}
