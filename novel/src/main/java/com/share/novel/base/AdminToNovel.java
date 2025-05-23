package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("AdminToNovel")
public class AdminToNovel {
    private Integer id;
    private Integer aid;
    private Integer nid;
    private String createTime;
    private String record;
}
