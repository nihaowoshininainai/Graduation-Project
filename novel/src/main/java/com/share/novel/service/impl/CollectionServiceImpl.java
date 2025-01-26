package com.share.novel.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.Collection;
import com.share.novel.mapper.CollectionMapper;
import com.share.novel.service.CollectionService;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper,Collection> implements CollectionService {

}
