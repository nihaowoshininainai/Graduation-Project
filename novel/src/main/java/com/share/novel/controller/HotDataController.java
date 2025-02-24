package com.share.novel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Novel;
import com.share.novel.service.NovelService;

@RestController
@RequestMapping("/api")
public class HotDataController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    NovelService novelService;

    @Value("${show.novel.views.nid}")
    String snvn;

    @Value("${commentslike.cid.uid}")
    String ccu;

    //获取浏览量
    @GetMapping("/getViews")
    public Integer getViews(@RequestParam("nid") Integer nid) {
        //查询redis中是否有该小说的浏览量
        String key = snvn + nid;
        String result = stringRedisTemplate.opsForValue().get(key);

        if(result == null) {
            Novel novel = novelService.getById(nid);
            Integer views = novel.getViews();
            result = views.toString();
            stringRedisTemplate.opsForValue().set(key, result);
        }


        return Integer.parseInt(result);
    }

    //浏览量加一
    @GetMapping("/addViews")
    public Integer addViews(@RequestParam("nid") Integer nid) {
        //查询redis中是否有该小说的浏览量
        String key = snvn + nid;
        String result = stringRedisTemplate.opsForValue().get(key);

        if(result == null) {
            synchronized(HotDataController.class){
                String res = stringRedisTemplate.opsForValue().get(key);
                if(res == null) {
                    Novel novel = novelService.getById(nid);
                    Integer views = novel.getViews();
                    result = views.toString();
                    stringRedisTemplate.opsForValue().set(key, result);
                }
            }
        }

        stringRedisTemplate.opsForValue().increment(key, 1);

        return Integer.parseInt(stringRedisTemplate.opsForValue().get(key));
    }

    //获取评论是否点赞
    @GetMapping("/getCommentsLike")
    public Boolean getCommentsLike(@RequestParam("cid") Integer cid,@RequestParam("uid") Integer uid) {
        //查询redis中是否有该评论是否点赞
        String key = ccu + cid+ ":" + uid;
        String result = stringRedisTemplate.opsForValue().get(key);

        if(result == null) {
            return false;
        }else {
            return true;
        }
    }

}
