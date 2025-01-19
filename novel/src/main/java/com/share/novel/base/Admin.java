package com.share.novel.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Admin")
/*
 * 表名：Admin
 * @param aid 管理员id
 * @param aname 管理员名
 * @param pwd 密码
 */
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer aid;
    private String aname;
    private String pwd;
}
