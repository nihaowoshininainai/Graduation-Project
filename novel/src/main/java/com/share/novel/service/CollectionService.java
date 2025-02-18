package com.share.novel.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.Collection;

public interface CollectionService extends IService<Collection>{

    //根据用户id查询收藏列表
    public List<Collection> getCollectionListByUserId(Integer userId);
}
