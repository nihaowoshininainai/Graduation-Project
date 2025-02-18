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

        //设置token不存敏感信息
        user.setPwd(" ");

        payload.put("user", user);

        //添加此负载为了使相同用户多次登陆有不同jwt，提高安全性
        payload.put("change", System.currentTimeMillis());

        String token = JWTUtil.createToken(payload, key.getBytes());

        //生成的token存入redis
        stringRedisTemplate.opsForValue().set("token:"+user.getUid()+":"+token, "true", 1, TimeUnit.DAYS);

        return token;
    }

    @Override
    public String register(String uname,String pwd){
        //判断是否有重名
        QueryWrapper<User> isSame = new QueryWrapper<>();
        isSame.eq("uname", uname);
        boolean flag = userMapper.exists(isSame);

        //无重名，将用户名与密码存入数据库，将用户信息转成token进行返回
        if(!flag){
            User user = new User(uname, pwd);
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
    public String login(String uname,String pwd){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uname", uname);
        queryWrapper.eq("pwd", pwd);
        boolean flag = userMapper.exists(queryWrapper);

        if(flag){
            User user = userMapper.selectOne(queryWrapper);
            String token = getToken(user);

            return Result.success("登陆成功", token);
        }
        else
            return Result.error("登陆失败", null);
    }

    @Override
    public String resetPwd(Integer uid,String newPwd,String oldPwd){
        
        //验证旧密码正确性
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("pwd", oldPwd);
        boolean flag = userMapper.exists(queryWrapper);

        if(flag){

            //将新密码存入mysql
            User user = new User();
            user.setUid(uid);
            user.setPwd(newPwd);
            int isSuccess = userMapper.updateById(user);

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
    public String deleteUser(Integer uid,String pwd){

        //验证密码正确性
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("pwd", pwd);
        boolean flag = userMapper.exists(queryWrapper);

        if(flag){

            //将该用户从mysql中删除
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
    public String getUname(Integer id){
        //先在redis中查询
        String key = su+id;
        String result = stringRedisTemplate.opsForValue().get(key);

        if(result==null){
            User user = userMapper.selectById(id);
            result = Result.success("查找成功", user.getUname());
            stringRedisTemplate.opsForValue().set(key, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        return result;
    }

    @Override
    public String getUid(String uname){
        //先在redis中查询
        String key = suu+uname;
        String result = stringRedisTemplate.opsForValue().get(key);

        if(result==null){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUname, uname);
            User user = userMapper.selectOne(queryWrapper);
            result = Result.success("查找成功", user.getUid());
            stringRedisTemplate.opsForValue().set(key, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        return result;
    }
    
}
