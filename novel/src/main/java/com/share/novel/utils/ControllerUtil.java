package com.share.novel.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.share.novel.base.User;
import com.share.novel.service.UserService;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

public class ControllerUtil {

    @Value("${jwt.key}")
    String key;

    @Autowired
    UserService userService;

    @Autowired 
    StringRedisTemplate stringRedisTemplate;
    
    //专用于cookie字符串转map
    public static Map<String,String> strToMap(String cookie){
        return TransformUtil.strToMap(cookie, ";", "=");
    }

    //专门用来检测登陆状态
    public String testIsLogin(HttpServletRequest httpServletRequest){
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
                return Result.success("Token存在", uid.toString());
            }
            else
                return Result.error("Token已过期，请重新登陆", "");
            
        }
        else
            return Result.error("非合法token", null);  
    }

    //统一模板，传入特定函数执行不同功能
    public String sameFunc(HttpServletRequest httpServletRequest,Integer uid,Function<Integer,String> function){
        String result = testIsLogin(httpServletRequest);
        List<String> list = Result.jsonStrtoList(result);
        String state = list.get(0);
        if(state.equals("1")){
            uid = Integer.parseInt(list.get(2));
            
            return function.apply(uid);
        }
        return result;
    }
}
