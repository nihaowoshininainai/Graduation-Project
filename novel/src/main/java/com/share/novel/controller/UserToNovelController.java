package com.share.novel.controller;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.share.novel.base.Novel;
import com.share.novel.base.User;
import com.share.novel.service.NovelService;
import com.share.novel.service.UserService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.RedisUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@RestController
@RequestMapping("/api")
public class UserToNovelController {

    @Data
    static class UserToNovel {
        private Novel novel;
        private User user;
    }

    @Autowired
    ControllerUtil controllerUtil;

    @Autowired
    NovelService novelService;

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Value("${novel.uid}")
    String nu;

    @Value("${show.novels.uid}")
    String snu;

    @PostMapping("/addNovel")
    public Result<?> addNovel(HttpServletRequest httpServletRequest,@RequestBody UserToNovel userToNovel){
        Function<Integer,Result<?>> function = uid -> {
            User user = userToNovel.getUser();
            Novel novel = userToNovel.getNovel();
            String pwd = user.getPwd();

            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUid, uid);
            User storedUser = userService.getOne(lambdaQueryWrapper);

            Result<?> result;
            if(storedUser != null && cn.hutool.crypto.digest.BCrypt.checkpw(pwd, storedUser.getPwd())){

                LambdaQueryWrapper<Novel> lambdaNovelQueryWrapper = new LambdaQueryWrapper<>();
                lambdaNovelQueryWrapper.eq(Novel::getTitle, novel.getTitle());
                boolean novelFlag = novelService.exists(lambdaNovelQueryWrapper);

                if(novelFlag)
                    result = Result.error("已存在同名小说，请不要重复分享", "");
                else{
                    novel.setUid(uid);
                    novel.setCreateTime();
                    result = novelService.add(novel);

                    redisUtil.deleteByPattern(nu+uid+"*");

                }
            }
            else
                result = Result.error("密码错误", "");
            return result;
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }

    @PostMapping("/delNovel")
    public Result<?> delNovel(HttpServletRequest httpServletRequest,@RequestBody UserToNovel userToNovel){
        Function<Integer,Result<?>> function = uid -> {
            User user = userToNovel.getUser();
            Novel novel = userToNovel.getNovel();
            String pwd = user.getPwd();
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUid, uid);
            User storedUser = userService.getOne(lambdaQueryWrapper);

            Result<?> result;
            if(storedUser != null && cn.hutool.crypto.digest.BCrypt.checkpw(pwd, storedUser.getPwd())){
                novel.setUid(uid);
                novel.setCreateTime();
                result = novelService.del(novel);

                stringRedisTemplate.delete(nu+uid);
                stringRedisTemplate.delete(snu+uid);

            }
            else
                result = Result.error("密码错误", "");
            return result;
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }

    @PostMapping("/reportNovel")
    public Result<?> reportNovel(HttpServletRequest httpServletRequest,@RequestBody Novel novel){
        Function<Integer,Result<?>> function = uid -> {
            novel.setState(2);
            boolean flag = novelService.updateById(novel);
            if(flag)
                return Result.success("举报成功", "");

            return Result.error("举报失败", "");
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }
}
