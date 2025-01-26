package com.share.novel.controller;

import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Collection;
import com.share.novel.base.Novel;
import com.share.novel.service.CollectionService;
import com.share.novel.utils.ControllerUtil;
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
    ControllerUtil controllerUtil;

    //添加收藏
    @PostMapping("/addCol")
    public String addCol(HttpServletRequest httpServletRequest,@RequestBody Novel novel){
        Function<Integer,String> function = uid -> {
            Collection collection = new Collection();
            collection.setUid(uid);
            collection.setNid(novel.getId());
            boolean flag = collectionService.save(collection);
            if(flag){
                
                //删除查找该用户的收藏缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys("collection:"+uid+":*"));

                return Result.success("成功收藏", "");
            }
            else
                return Result.error("收藏失败", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

    //删除收藏
    @PostMapping("delCol")
    public String delCol(HttpServletRequest httpServletRequest,@RequestBody Novel novel){
        Function<Integer,String> function = uid -> {
            Collection collection = new Collection();
            collection.setUid(uid);
            collection.setNid(novel.getId());
            Map<String,Object> map = Map.of("uid",uid,"nid",novel.getId());
            boolean flag = collectionService.removeByMap(map);
            if(flag){
                
                //删除查找该用户的收藏缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys("collection:"+uid+":*"));

                return Result.success("取消收藏", "");
            }
            else
                return Result.error("取消失败", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }
}
