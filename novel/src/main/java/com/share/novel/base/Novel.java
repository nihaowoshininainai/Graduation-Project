package com.share.novel.base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.hutool.json.JSONUtil;
import lombok.Data;

@Data
@TableName("Novel")
/*
 * 表名：Novel
 * @param id 小说id
 * @param title 小说标题
 * @param content 小说内容
 * @param createTime 创建时间
 * @param uid 用户id
 * @param views 浏览量
 * @param state 小说状态，0审核中，-1审核不通过，1审核通过，2被举报需要管理员审核，3管理员核实违规下架处理
 */
public class Novel {
    @TableId(type = IdType.AUTO)
    Integer id;
    String title;
    String content;
    String createTime;
    Integer uid;
    Integer views;
    Integer state;
    public void setCreateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        createTime = LocalDateTime.now().format(formatter);
    }

    @Override
    public String toString() {
        Map<String,String> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("title", title);
        map.put("content", content);
        map.put("createTime", createTime);
        map.put("uid", uid.toString());
        map.put("views", views.toString());
        map.put("state", state.toString());

        return JSONUtil.toJsonStr(map);
    }
    
}

