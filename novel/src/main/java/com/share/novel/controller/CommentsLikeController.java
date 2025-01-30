package com.share.novel.controller;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.CommentsLike;
import com.share.novel.service.CommentsLikeService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class CommentsLikeController {

    @Autowired
    CommentsLikeService commentsLikeService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${commentslike.cid.uid}")
    String ccu;

    @Autowired
    ControllerUtil controllerUtil;

    //点赞
    @PostMapping("/addCommentsLike")
    public String addCommentsLike(HttpServletRequest httpServletRequest,CommentsLike commentsLike){
        Function<Integer,String> function = uid -> {
            stringRedisTemplate.opsForValue().set(ccu+commentsLike.getCid()+":"+uid, "true");

            return Result.success("点赞成功", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

    //取消点赞
    @PostMapping("/delCommentsLike")
    public String delCommentsLike(HttpServletRequest httpServletRequest,CommentsLike commentsLike){
        Function<Integer,String> function = uid -> {
            Boolean flag = stringRedisTemplate.delete(ccu+commentsLike.getCid()+":"+uid);

            if(flag)
                return Result.success("点赞成功", "");
            else
                return Result.error("取消点赞失败，请重试", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }
}
