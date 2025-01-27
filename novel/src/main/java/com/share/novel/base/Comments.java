package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Comments")
/*
 * @param nid 小说id
 * @param uid 用户id
 * @param content 内容
 * @param state 评论状态，0为正常状态，1为被举报，-1为被封禁
 */
public class Comments {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer nid;
    Integer uid;
    Integer content;
    Integer state;
}
