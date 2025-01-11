package com.share.novel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.share.novel.base.User;
import com.share.novel.service.UserService;
import com.share.novel.utils.Result;

import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;

@RestController()
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //注册
    @PostMapping("/register")
    public String register(String uname,String pwd) {

        //判断是否有重名
        QueryWrapper<User> isSame = new QueryWrapper<>();
        isSame.eq("uname", uname);
        long count = userService.count(isSame);

        //重名
        if(count!=0)
            return Result.error("此用户名已被注册",null);

        //无重名，将用户名与密码存入数据库，将用户名密码加密进行返回
        boolean flag = userService.save(new User(uname, pwd));
        if(flag){
            User user = userService.getOne(isSame);
            Map<String,Object> payload = new HashMap<>();

            //设置token不存敏感信息
            user.setPwd(" ");
            
            payload.put("user", user);

            //加密
            String token = JWTUtil.createToken(payload, stringRedisTemplate.opsForValue().get("secretkey").getBytes());

            //将token存入redis中,设置过期时间为一天
            stringRedisTemplate.opsForValue().set("token:"+token, "true", 1, TimeUnit.DAYS);

            return Result.success("注册成功", token);
        }
        return Result.error("注册用户失败，请重试", null);
    }
    
}
