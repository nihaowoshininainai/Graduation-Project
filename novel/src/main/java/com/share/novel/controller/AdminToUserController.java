package com.share.novel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.share.novel.base.Admin;
import com.share.novel.base.User;
import com.share.novel.service.AdminService;
import com.share.novel.service.UserService;

import lombok.Data;

@Data
class AdminToUser {
    private Admin admin;
    private User user;
}

@RestController
@RequestMapping("/api")
public class AdminToUserController {

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${token.uid}")
    String tu;

    @Value("${user.id}")
    String ui;

    // 审核用户统一接口
    @PostMapping("/auditUser")
    public String audit(@RequestBody AdminToUser adminToUser) {
        Admin admin = adminToUser.getAdmin();
        User user = adminToUser.getUser();

        // 判断管理员身份
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getAname, admin.getAname());
        lambdaQueryWrapper.eq(Admin::getPwd, admin.getPwd());
        boolean flag = adminService.exists(lambdaQueryWrapper);

        if (flag) {
            boolean uflag = userService.updateById(user);
            if (uflag) {

                // 删除相关用户缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys(tu + user.getUid() + "*"));
                stringRedisTemplate.delete(stringRedisTemplate.keys(ui + user.getUid()));

                return "操作成功";
            } else
                return "操作失败，请重试";
        } else
            return "管理员密码错误";
    }
}
