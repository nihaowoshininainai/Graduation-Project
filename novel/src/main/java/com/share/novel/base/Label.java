package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Label")
/*
 * 表名：Label
 * @param id 标签id
 * @param name 标签名
 */
public class Label {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
}
