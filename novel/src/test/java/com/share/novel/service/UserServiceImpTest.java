package com.share.noval.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.share.noval.NovalApplication;
import com.share.noval.base.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = NovalApplication.class)
public class UserServiceImpTest {
    @Autowired
    private UserService userService;

    //测试service是否正常运转
    @Test
    void test(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        System.out.println(userService.count(queryWrapper.eq("uname", "lisa")));
    }
}
