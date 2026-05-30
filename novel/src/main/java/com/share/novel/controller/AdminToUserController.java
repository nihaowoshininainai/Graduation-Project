package com.share.novel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.User;
import com.share.novel.service.AdminService;
import com.share.novel.service.UserService;
import com.share.novel.utils.RedisUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
class AdminToUser {
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
    RedisUtil redisUtil;

    @Value("${token.uid}")
    String tu;

    @Value("${user.id}")
    String ui;

    @Value("${show.weigui.user.uid}")
    String swuu;

    @PostMapping("/auditUser")
    public Result<?> audit(@RequestBody AdminToUser adminToUser, HttpServletRequest request) {
        Result<?> authResult = adminService.testAdminLogin(request);
        if (!authResult.isSuccess()) {
            return Result.error("管理员身份验证失败", null);
        }
        User user = adminToUser.getUser();

        boolean uflag = userService.updateById(user);
        if (uflag) {

            redisUtil.deleteByPattern(tu + user.getUid() + "*");
            redisUtil.deleteByPattern(ui + user.getUid());
            redisUtil.deleteByPattern(swuu + user.getUid());

            return Result.success("操作成功", null);
        } else
            return Result.error("操作失败，请重试", null);
    }

    @PostMapping("/deleteUserByAdmin")
    public Result<?> deleteUser(@RequestBody AdminToUser adminToUser, HttpServletRequest request) {
        Result<?> authResult = adminService.testAdminLogin(request);
        if (!authResult.isSuccess()) {
            return Result.error("管理员身份验证失败", null);
        }
        User user = adminToUser.getUser();

        boolean uflag = userService.removeById(user.getUid());
        if (uflag) {

            redisUtil.deleteByPattern(tu + user.getUid() + "*");
            redisUtil.deleteByPattern(ui + user.getUid());
            redisUtil.deleteByPattern(swuu + user.getUid());
            return Result.success("操作成功", null);
        } else
            return Result.error("操作失败，请重试", null);
    }

    @PostMapping("/resetPwdByAdmin")
    public Result<?> resetPwd(@RequestBody AdminToUser adminToUser, HttpServletRequest request) {
        Result<?> authResult = adminService.testAdminLogin(request);
        if (!authResult.isSuccess()) {
            return Result.error("管理员身份验证失败", null);
        }
        User user = adminToUser.getUser();

        boolean uflag = userService.updateById(user);
        if (uflag) {

            redisUtil.deleteByPattern(tu + user.getUid() + "*");
            redisUtil.deleteByPattern(ui + user.getUid());
            redisUtil.deleteByPattern(swuu + user.getUid());
            return Result.success("操作成功", null);
        } else
            return Result.error("操作失败，请重试", null);
    }
}
