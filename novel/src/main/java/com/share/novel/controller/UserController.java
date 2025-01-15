package com.share.novel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.share.novel.base.User;
import com.share.novel.service.UserService;
import com.share.novel.utils.Result;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    //一个通用User生成token方法
    String getToken(User user){
        Map<String,Object> payload = new HashMap<>();

        //设置token不存敏感信息
        user.setPwd(" ");

        payload.put("user", user);

        //添加此负载为了使相同用户多次登陆有不同jwt，提高安全性
        payload.put("change", System.currentTimeMillis());

        String token = JWTUtil.createToken(payload, stringRedisTemplate.opsForValue().get("secretkey").getBytes());

        //生成的token存入redis
        stringRedisTemplate.opsForValue().set("token:"+user.getUname()+":"+token, "true", 1, TimeUnit.DAYS);

        return token;
    }

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
            String token = getToken(user);

            return Result.success("注册成功", token);
        }
        return Result.error("注册用户失败，请重试", null);
    }

    //登陆
    @PostMapping("/login")
    public String login(String uname,String pwd){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uname", uname);
        queryWrapper.eq("pwd", pwd);
        User user = userService.getOne(queryWrapper);

        if(user!=null){
            String token = getToken(user);
            
            return Result.success("登陆成功", token);
        }
        else
            return Result.error("登陆失败", null);
    }

    //将字符串转换为map
    Map<String,String> strToMap(String cookie){
        Map<String,String> map = new HashMap<>();
        String[] split = cookie.split(";");
        for (int i = 0; i < split.length; i++) {
            String[] splitd = split[i].split("=");
            map.put(splitd[0], splitd[1]);
        }

        return map;
    }
    
    //修改密码
    @PostMapping("/resetPwd")
    public String resetPwd(HttpServletRequest httpServletRequest,String newPwd,String oldPwd) {
        Map<String,String> cookie = strToMap(httpServletRequest.getHeader("Cookie"));
        String token = cookie.get("Token");
        
        //判断token合法性
        boolean flag = JWTUtil.verify(token, key.getBytes());

        if(flag){
            User user = JSONUtil.toBean(JWTUtil.parseToken(token).getPayload("user").toString(), User.class);
            String uname = user.getUname();

            //验证旧密码正确性
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uname", uname);
            queryWrapper.eq("pwd", oldPwd);
            User userOne = userService.getOne(queryWrapper);
            if(userOne!=null){
                //查找redis中是否有该token
                String exist = stringRedisTemplate.opsForValue().get("token:"+uname+":"+token);
                if(exist!=null){
                    //删除所有旧密码的token
                    stringRedisTemplate.delete(stringRedisTemplate.keys("token:"+uname+":*"));

                    //将新密码存入mysql
                    user.setPwd(newPwd);
                    userService.updateById(user);

                    user.setPwd(" ");
                    String newToken = getToken(user);
                    return Result.success("修改成功", newToken);
                }
                else
                    return Result.error("token已过期，请重新登陆", null);
            }
            else
                return Result.error("密码错误", null);
        }
        else
            return Result.error("非合法token", null);
        
        
    }
}
