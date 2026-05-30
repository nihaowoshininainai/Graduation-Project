package com.share.novel.utils;

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

    public static Map<String, String> strToMap(String cookie) {
        return TransformUtil.strToMap(cookie, ";", "=");
    }

    public Result<?> testIsLogin(HttpServletRequest httpServletRequest) {
        String cookieHeader = httpServletRequest.getHeader("Cookie");
        if (cookieHeader == null || cookieHeader.isEmpty()) {
            return Result.error("未登录，请先登录", null);
        }
        Map<String, String> cookie = ControllerUtil.strToMap(cookieHeader);
        String token = cookie.get("Token");

        if (token == null || token.isEmpty()) {
            return Result.error("非合法token", null);
        }

        boolean flag = JWTUtil.verify(token, key.getBytes());

        if (flag) {
            Object payloadObj = JWTUtil.parseToken(token).getPayload("user");
            if (payloadObj == null) {
                return Result.error("非合法token", null);
            }
            User user = JSONUtil.toBean(payloadObj.toString(), User.class);
            Integer uid = user.getUid();
            Integer banned = user.getBanned();
            if (banned != null && banned == 1) {
                return Result.error("您已被封号", "");
            }

            String exist = stringRedisTemplate.opsForValue().get("token:" + uid + ":" + token);

            if (exist != null) {
                return Result.success("Token存在", uid);
            } else
                return Result.error("Token已过期，请重新登陆", "");

        } else
            return Result.error("非合法token", null);
    }

    public Result<?> sameFunc(HttpServletRequest httpServletRequest, Function<Integer, Result<?>> function) {
        Result<?> result = testIsLogin(httpServletRequest);
        if (result.isSuccess()) {
            Integer uid = (Integer) result.getData();
            return function.apply(uid);
        }
        return result;
    }
}
