package com.share.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.Novel;

public interface NovelService extends IService<Novel> {

    //添加小说
    String add(Novel novel);

    //删除小说;
    String del(Novel novel);

    //分页推送,
    String index(String condition,String sort,Integer current,Integer size);

    //分类推送
    String classify(String condition,String sort,Integer current,Integer size,Integer labelId);
    /*
     * @param time 按照时间排序
     * @param hot 按照热度排序
     */
    public enum Condition{
        time,
        hot
    }

    public enum Sort{
        asc,
        desc
    }
    
}