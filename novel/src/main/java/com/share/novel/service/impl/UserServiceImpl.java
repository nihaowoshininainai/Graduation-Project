package com.share.novel.service.impl;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.User;
import com.share.novel.mapper.UserMapper;
import com.share.novel.service.UserService;
import com.share.novel.utils.RandomUtil;
import com.share.novel.utils.Result;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.jwt.JWTUtil;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    UserMapper userMapper;

    @Value("${show.uname.uid}")
    String su;

    @Value("${jwt.key}")
    String key;

    @Value("${show.uid.uname}")
    String suu;

    @Override
    public String getToken(User user){
        Map<String,Object> payload = new HashMap<>();

        user.setPwd(" ");

        payload.put("user", user);

        payload.put("change", System.currentTimeMillis());

        String token = JWTUtil.createToken(payload, key.getBytes());

        stringRedisTemplate.opsForValue().set("token:"+user.getUid()+":"+token, "true", 1, TimeUnit.DAYS);

        return token;
    }

    @Override
    public Result<?> register(String uname,String pwd){
        QueryWrapper<User> isSame = new QueryWrapper<>();
        isSame.eq("uname", uname);
        boolean flag = userMapper.exists(isSame);

        if(!flag){
            User user = new User(uname, BCrypt.hashpw(pwd, BCrypt.gensalt()));
            int isSuccess = userMapper.insert(user);
            if(isSuccess>0){
                User sUser = userMapper.selectOne(isSame);
                String token = getToken(sUser);
                return Result.success("注册成功", token);
            }
            else
                return Result.error("注册失败，请重试", null);
        }
        else
            return Result.error("此用户名已被注册",null);
    }

    @Override
    public Result<?> login(String uname,String pwd){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uname", uname);
        User user = userMapper.selectOne(queryWrapper);

        if(user != null && BCrypt.checkpw(pwd, user.getPwd())){
            String token = getToken(user);

            return Result.success("登陆成功", token);
        }
        else
            return Result.error("登陆失败", null);
    }

    @Override
    public Result<?> resetPwd(Integer uid,String newPwd,String oldPwd){

        User user = userMapper.selectById(uid);
        if(user != null && BCrypt.checkpw(oldPwd, user.getPwd())){

            User updateUser = new User();
            updateUser.setUid(uid);
            updateUser.setPwd(BCrypt.hashpw(newPwd, BCrypt.gensalt()));
            int isSuccess = userMapper.updateById(updateUser);

            if(isSuccess>0){
                User nUser = userMapper.selectById(uid);
                String token = getToken(nUser);

                return Result.success("修改成功", token);
            }
            else
                return Result.error("修改失败", "");
        }
        else
            return Result.error("密码错误", "");

    }

    @Override
    public Result<?> deleteUser(Integer uid,String pwd){

        User user = userMapper.selectById(uid);
        if(user != null && BCrypt.checkpw(pwd, user.getPwd())){

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", uid);
            int d = userMapper.delete(queryWrapper);

            if(d>0)
                return Result.success("成功注销", "");
            else
                return Result.error("注销失败，请重试", "");
        }
        else
            return Result.error("密码错误", "");
    }

    @Override
    public Result<?> getUname(Integer id){
        String redisKey = su+id;
        String cachedJson = stringRedisTemplate.opsForValue().get(redisKey);

        if(cachedJson != null){
            return Result.fromJson(cachedJson);
        }

        User user = userMapper.selectById(id);
        Result<?> result = Result.success("查找成功", user.getUname());
        stringRedisTemplate.opsForValue().set(redisKey, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
        return result;
    }

    @Override
    public Result<?> getUid(String uname){
        String redisKey = suu+uname;
        String cachedJson = stringRedisTemplate.opsForValue().get(redisKey);

        if(cachedJson != null){
            return Result.fromJson(cachedJson);
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUname, uname);
        User user = userMapper.selectOne(queryWrapper);
        Result<?> result = Result.success("查找成功", user.getUid());
        stringRedisTemplate.opsForValue().set(redisKey, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
        return result;
    }

}
