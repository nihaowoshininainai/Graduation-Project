package com.share.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.Novel;
import com.share.novel.utils.Result;

public interface NovelService extends IService<Novel> {

    Result<?> add(Novel novel);

    Result<?> del(Novel novel);

    Result<?> index(String condition,String sort,Integer current,Integer size);

    Result<?> classify(String condition,String sort,Integer current,Integer size,Integer labelId);

    Result<?> search(String condition,String sort,Integer current,Integer size,String keyword);

    public enum Condition{
        time,
        hot
    }

    public enum Sort{
        asc,
        desc
    }

}
