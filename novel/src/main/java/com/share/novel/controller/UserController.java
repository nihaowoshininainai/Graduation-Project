package com.share.novel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.share.novel.base.User;
import com.share.novel.service.UserService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.Result;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;


@RestController()
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.key}")
    String key;

    //注册
    @PostMapping("/register")
    public String register(String uname,String pwd) {
        return userService.register(uname, pwd);
    }

    //登陆
    @PostMapping("/login")
    public String login(String uname,String pwd){
        return userService.login(uname, pwd);
    }
    
    //修改密码
    @PostMapping("/resetPwd")
    public String resetPwd(HttpServletRequest httpServletRequest,String newPwd,String oldPwd) {
        Map<String,String> cookie = ControllerUtil.strToMap(httpServletRequest.getHeader("Cookie"));
        String token = cookie.get("Token");
        
        //判断token的合法性
        boolean flag = JWTUtil.verify(token, key.getBytes());

        if(flag){
            User user = JSONUtil.toBean(JWTUtil.parseToken(token).getPayload("user").toString(), User.class);
            Integer uid = user.getUid();

            //查找redis中是否有该token
            String exist = stringRedisTemplate.opsForValue().get("token:"+uid+":"+token);

            if(exist!=null){
                String result = userService.resetPwd(uid, newPwd, oldPwd);
                JSONObject map = JSONUtil.parseObj(result);
                String state = map.getStr("state");
                if(state.equals("1"))

                    //删除所有旧密码的token
                    stringRedisTemplate.delete(stringRedisTemplate.keys("token:"+uid+":*"));

                return result;

            }
            else
                return Result.error("token已过期，请重新登陆", null);
        }
        else
            return Result.error("非合法token", null);
    }

    //注销功能
    @PostMapping("/deleteUser")
    public String deleteUser(HttpServletRequest httpServletRequest,String pwd){
        Map<String,String> cookie = ControllerUtil.strToMap(httpServletRequest.getHeader("Cookie"));
        String token = cookie.get("Token");
        
        //判断token合法性
        boolean flag = JWTUtil.verify(token, key.getBytes());
        
        if(flag){
            User user = JSONUtil.toBean(JWTUtil.parseToken(token).getPayload("user").toString(), User.class);
            Integer uid = user.getUid();

            //查找redis中是否有该token
            String exist = stringRedisTemplate.opsForValue().get("token:"+uid+":"+token);

            if(exist!=null){
                String result = userService.deleteUser(uid, pwd);
                JSONObject map = JSONUtil.parseObj(result);
                String state = map.getStr("state");
                if(state.equals("1"))
    
                    //删除所有该用户的token
                    stringRedisTemplate.delete(stringRedisTemplate.keys("token:"+uid+":*"));
    
                return result;
            }
            else
                return Result.error("Token已过期，请重新登陆", "");
            
        }
        else
            return Result.error("非合法token", null);           
    }
}
