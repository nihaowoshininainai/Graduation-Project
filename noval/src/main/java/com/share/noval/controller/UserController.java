package com.share.noval.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.share.noval.base.User;
import com.share.noval.service.UserService;
import com.share.noval.utils.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

@RestController()
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

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
            //加密
            
            return null;
        }
        return Result.error("注册用户失败，请重试", null);
    }
    
}
