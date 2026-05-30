package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("AdminToNovel")
public class AdminToNovel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer aid;
    private Integer nid;
    private String createTime;
    private String record;
}
