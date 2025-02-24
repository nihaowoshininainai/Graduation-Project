package com.share.novel.controller;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    ControllerUtil controllerUtil;

    @Value("${commentslike.cid.uid}")
    String ccu;

    //点赞
    @GetMapping("/addCommentsLike")
    public String addCommentsLike(HttpServletRequest httpServletRequest,@RequestParam("cid") Integer cid){
        Function<Integer,String> function = uid -> {
            stringRedisTemplate.opsForValue().set(ccu+cid+":"+uid, "true");

            return Result.success("点赞成功", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

    //取消点赞
    @GetMapping("/delCommentsLike")
    public String delCommentsLike(HttpServletRequest httpServletRequest,@RequestParam("cid") Integer cid){
        Function<Integer,String> function = uid -> {
            Boolean flag = stringRedisTemplate.delete(ccu+cid+":"+uid);

            if(flag)
                return Result.success("取消点赞", "");
            else
                return Result.error("取消点赞失败，请重试", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }
}
