package com.share.novel.controller;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Comments;
import com.share.novel.service.CommentsService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.RedisUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class CommentsController {

    @Autowired
    CommentsService commentsService;

    @Autowired
    ControllerUtil controllerUtil;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Value("${comments.nid.uid}")
    String cnu;

    @Value("${show.comments.nid}")
    String scn;

    @PostMapping("/addCom")
    public Result<?> add(HttpServletRequest httpServletRequest,@RequestBody Comments comments){
        Function<Integer,Result<?>> function = uid -> {
            comments.setUid(uid);
            boolean flag = commentsService.save(comments);
            if(flag){
                redisUtil.deleteByPattern(cnu+comments.getNid()+":*");
                redisUtil.deleteByPattern(scn+comments.getNid()+":*");
                return Result.success("评论成功", "");
            }
            else
                return Result.error("评论失败，请重试", "");
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }

    @PostMapping("/delCom")
    public Result<?> del(HttpServletRequest httpServletRequest,@RequestBody Comments comments){
        Function<Integer,Result<?>> function = uid -> {
            boolean flag = commentsService.removeById(comments);
            if(flag){
                redisUtil.deleteByPattern(cnu+comments.getNid()+":*");
                return Result.success("评论删除成功", "");
            }
            else
                return Result.error("评论删除失败，请重新尝试", "");
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }

}
