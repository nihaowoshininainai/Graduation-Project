package com.share.novel.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Collection;
import com.share.novel.base.Novel;
import com.share.novel.service.CollectionService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.RedisUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserToCollectionController {

    @Autowired
    CollectionService collectionService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ControllerUtil controllerUtil;

    @Value("${collection.uid.nid}")
    String cun;

    @Value("${collections.uid}")
    String cu;

    @Value("${show.novel.like.uid}")
    String snl;

    @Value("${show.novels.like.uid}")
    String snlu;

    @PostMapping("/addCol")
    public Result<?> addCol(HttpServletRequest httpServletRequest,@RequestBody Novel novel){
        Function<Integer,Result<?>> function = uid -> {
            Collection collection = new Collection();
            collection.setUid(uid);
            collection.setNid(novel.getId());
            collection.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            boolean flag = collectionService.save(collection);
            if(flag){
                redisUtil.deleteByPattern(cun+uid+"*");
                redisUtil.deleteByPattern(cu+uid+"*");
                redisUtil.deleteByPattern(snl+uid+"*");
                redisUtil.deleteByPattern(snlu+uid+"*");
                return Result.success("成功收藏", "");
            }
            else
                return Result.error("收藏失败", "");
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }

    @PostMapping("/delCol")
    public Result<?> delCol(HttpServletRequest httpServletRequest,@RequestBody Novel novel){
        Function<Integer,Result<?>> function = uid -> {
            Collection collection = new Collection();
            collection.setUid(uid);
            collection.setNid(novel.getId());
            Map<String,Object> map = Map.of("uid",uid,"nid",novel.getId());
            boolean flag = collectionService.removeByMap(map);
            if(flag){
                redisUtil.deleteByPattern(cun+uid+"*");
                redisUtil.deleteByPattern(cu+uid+"*");
                redisUtil.deleteByPattern(snl+uid+"*");
                redisUtil.deleteByPattern(snlu+uid+"*");
                return Result.success("取消收藏", "");
            }
            else
                return Result.error("取消失败", "");
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }
}
