package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Collection")
/*
 * @param uid 用户id
 * 
 * @param nid 小说id
 */
public class Collection {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private Integer nid;
    private String createTime;
}
