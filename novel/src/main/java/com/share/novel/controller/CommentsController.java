package com.share.novel.controller;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Comments;
import com.share.novel.service.CommentsService;
import com.share.novel.utils.ControllerUtil;
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

    //添加评论
    @PostMapping("/addCom")
    public String add(HttpServletRequest httpServletRequest,@RequestBody Comments comments){
        Function<Integer,String> function = uid -> {
            comments.setUid(uid);
            boolean flag = commentsService.save(comments);
            if(flag){
                
                //删除评论缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys("comments:"+comments.getNid()+":*"));

                return Result.success("评论成功", "");
            }
            else
                return Result.error("评论失败，请重试", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

    //删除评论
    @PostMapping("/delCom")
    public String del(HttpServletRequest httpServletRequest,@RequestBody Comments comments){
        Function<Integer,String> function = uid -> {
            boolean flag = commentsService.removeById(comments);
            if(flag){

                //删除评论缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys("comments:"+comments.getNid()+":*"));

                return Result.success("评论删除成功", "");
            }
            else
                return Result.error("评论删除失败，请重新尝试", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

}
