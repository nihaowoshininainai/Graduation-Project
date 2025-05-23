package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("AdminToComments")
public class AdminToComments {
    private Integer id;
    private Integer aid;
    private Integer cid;
    private String createTime;
    private String record;
}
