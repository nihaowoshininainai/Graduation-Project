package com.share.novel.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.Collection;
import com.share.novel.mapper.CollectionMapper;
import com.share.novel.service.CollectionService;
import com.share.novel.utils.RandomUtil;

import cn.hutool.json.JSONUtil;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper,Collection> implements CollectionService {

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${collections.uid}")
    String cu;

    @Override
    public List<Collection> getCollectionListByUserId(Integer userId) {
        String key = cu + userId;

        //查询redis中是否有该用户的收藏列表
        String collectionList = stringRedisTemplate.opsForValue().get(key);

        if(collectionList == null) {
            LambdaQueryWrapper<Collection> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Collection::getUid, userId);
            List<Collection> list = collectionMapper.selectList(wrapper);
            String result = JSONUtil.toJsonStr(list);
            stringRedisTemplate.opsForValue().set(key, result, RandomUtil.getRandom(),TimeUnit.HOURS);

            return list;
        }
        
        List<Collection> list = JSONUtil.toList(collectionList, Collection.class);

        return list;
    }
}
