package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("NovelLabel")
/*
 * @param nid 小说id
 * @param lid 标签id
 */
public class NovelLabel {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer nid;
    private Integer lid;

    public NovelLabel(){}

    public NovelLabel(Integer nid,Integer lid){
        this.nid = nid;
        this.lid = lid;
    }
}
