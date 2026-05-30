package com.share.novel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.User;
import com.share.novel.service.UserService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.RedisUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController()
@RequestMapping("/api")
public class UserController {

    @Data
    static class ResetPwd {
        String oldPwd;
        String newPwd;

    }

    @Data
    static class DeleteUserReq {
        String pwd;
    }

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ControllerUtil controllerUtil;

    @Value("${jwt.key}")
    String key;

    @Value("${token.uid}")
    String tu;

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        return userService.register(user.getUname(),user.getPwd());
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        return userService.login(user.getUname(), user.getPwd());
    }

    @PostMapping("/resetPwd")
    public Result<?> resetPwd(HttpServletRequest httpServletRequest,@RequestBody ResetPwd rsp) {
        String newPwd = rsp.getNewPwd();
        String oldPwd = rsp.getOldPwd();
        Function<Integer,Result<?>> function = uid -> {
            Result<?> result = userService.resetPwd(uid, newPwd, oldPwd);
            if(result.isSuccess())
                redisUtil.deleteByPattern(tu+uid+"*");
            return result;
        };
        return controllerUtil.sameFunc(httpServletRequest, function);
    }

    @PostMapping("/deleteUser")
    public Result<?> deleteUser(HttpServletRequest httpServletRequest,@RequestBody DeleteUserReq req){
        Function<Integer,Result<?>> function = uid -> {
            Result<?> result = userService.deleteUser(uid, req.getPwd());
            if(result.isSuccess())
                redisUtil.deleteByPattern(tu+uid+"*");
            return result;
        };
        return controllerUtil.sameFunc(httpServletRequest, function);
    }
}
