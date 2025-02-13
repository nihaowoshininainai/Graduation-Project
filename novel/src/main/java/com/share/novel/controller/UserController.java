package com.share.novel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.User;
import com.share.novel.service.UserService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.Result;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController()
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ControllerUtil controllerUtil;

    @Value("${jwt.key}")
    String key;

    @Value("${token.uid}")
    String tu;

    //注册
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user.getUname(),user.getPwd());
    }

    //登陆
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.login(user.getUname(), user.getPwd());
    }
    
    //修改密码
    @PostMapping("/resetPwd")
    public String resetPwd(HttpServletRequest httpServletRequest,String newPwd,String oldPwd) {
        Function<Integer,String> function = uid -> {
            String result = userService.resetPwd(uid, newPwd, oldPwd);
            JSONObject map = JSONUtil.parseObj(result);
            String state = map.getStr("state");
            if(state.equals("1"))

                //删除所有旧密码的token
                stringRedisTemplate.delete(stringRedisTemplate.keys(tu+uid+"*"));

            return result;
        };
        Integer uid = null;
        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

    //注销功能
    @PostMapping("/deleteUser")
    public String deleteUser(HttpServletRequest httpServletRequest,String pwd){
        Function<Integer,String> function = uid -> {
            String result = userService.deleteUser(uid, pwd);
            List<String> list = Result.jsonStrtoList(result);
            String stateIf = list.get(0);
            if(stateIf.equals("1"))
    
                //删除所有该用户的token
                stringRedisTemplate.delete(stringRedisTemplate.keys(tu+uid+"*"));
    
            return result;
        };
        Integer uid = null;
        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }
}
