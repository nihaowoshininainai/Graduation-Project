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

    @Value("${novel.uid}")
    String nu;

    //用户添加文章
    @PostMapping("/addNovel")
    public String addNovel(HttpServletRequest httpServletRequest,@RequestBody UserToNovel userToNovel){
        Function<Integer,String> function = uid -> {
            User user = userToNovel.getUser();
            Novel novel = userToNovel.getNovel();
            String pwd = user.getPwd();

            //检查密码是否正确
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUid, uid);
            lambdaQueryWrapper.eq(User::getPwd, pwd);
            boolean flag = userService.exists(lambdaQueryWrapper);

            String result = new String();
            if(flag){

                //查找是否有同名小说
                LambdaQueryWrapper<Novel> lambdaNovelQueryWrapper = new LambdaQueryWrapper<>();
                lambdaNovelQueryWrapper.eq(Novel::getTitle, novel.getTitle());
                boolean novelFlag = novelService.exists(lambdaNovelQueryWrapper);

                if(novelFlag)
                    result = Result.error("已存在同名小说，请不要重复分享", "");
                else{
                    novel.setUid(uid);
                    novel.setCreateTime();
                    result = novelService.add(novel);

                    //删除用户查询小说的缓存
                    stringRedisTemplate.delete(stringRedisTemplate.keys(nu+uid+"*"));

                }
            }
            else
                result = Result.error("密码错误", "");
            return result;
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

    //用户删除文章
    @PostMapping("/delNovel")
    public String delNovel(HttpServletRequest httpServletRequest,@RequestBody UserToNovel userToNovel){
        Function<Integer,String> function = uid -> {
            User user = userToNovel.getUser();
            Novel novel = userToNovel.getNovel();
            String pwd = user.getPwd();
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUid, uid);
            lambdaQueryWrapper.eq(User::getPwd, pwd);
            boolean flag = userService.exists(lambdaQueryWrapper);
            String result = new String();
            if(flag){
                novel.setUid(uid);
                novel.setCreateTime();
                result = novelService.del(novel);

                //删除用户查询小说的缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys(nu+uid+"*"));

            }
            else
                result = Result.error("密码错误", "");
            return result;
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

    //用户举报小说
    public String reportNovel(HttpServletRequest httpServletRequest,@RequestBody Novel novel){
        Function<Integer,String> function = uid -> {
            novel.setState(2);
            boolean flag = novelService.updateById(novel);
            if(flag)
                return Result.success("举报成功", "");

            return Result.error("举报失败", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }
}
