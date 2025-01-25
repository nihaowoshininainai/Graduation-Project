package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Collection")
/*
 * @param uid 用户id
 * @param nid 小说id
 */
public class Collection {

    @TableId(type = IdType.AUTO)
    Integer id;
    Integer uid;
    Integer nid;
}
