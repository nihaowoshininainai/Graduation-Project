package com.share.novel.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.service.LabelService;
import com.share.novel.service.NovelService;
import com.share.novel.service.UserService;
import com.share.novel.utils.RandomUtil;
import com.share.novel.utils.Result;

@RestController
@RequestMapping("/api")
public class ShowController {

    @Autowired
    NovelService novelService;

    @Autowired
    UserService userService;

    @Autowired
    LabelService labelService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${label.all}")
    String la;

    //首页小说请求
    @GetMapping("/index")
    String show(@RequestParam(required = false) String condition,@RequestParam(required = false) String sort,@RequestParam("current") Integer current,@RequestParam("size") Integer size){
        
        return novelService.index(condition, sort, current, size);
    }

    //分类小说请求
    @GetMapping("/classify")
    String classify(@RequestParam(required = false) String condition,@RequestParam(required = false) String sort,@RequestParam("current") Integer current,@RequestParam("size") Integer size,@RequestParam("labelId") Integer labelId){
        
        return novelService.classify(condition, sort, current, size, labelId);
    }
    //根据id查询用户名
    @GetMapping("/showUname")
    String showUname(@RequestParam("id") Integer id){

        return userService.getUname(id);
    }

    //获取标签
    @GetMapping("/labels")
    public String getLabel(){
        //查询redis中是否存有缓存
        String result = stringRedisTemplate.opsForValue().get(la);

        if(result==null){
            result = Result.success("查询成功", labelService.list());
            stringRedisTemplate.opsForValue().set(la, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        return result;
    }
    
}