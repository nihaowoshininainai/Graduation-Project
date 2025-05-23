package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("CommentsLike")
/*
 * @params cid 评论id
 * 
 * @params uid 用户id
 */
public class CommentsLike {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer cid;
    private Integer uid;
    private String createTime;
}
