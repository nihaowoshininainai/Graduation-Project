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
import com.share.novel.base.Comments;
import com.share.novel.service.AdminService;
import com.share.novel.service.CommentsService;

import lombok.Data;

@Data
class AdminToComments {
    private Admin admin;
    private Comments comments;
}

@RestController
@RequestMapping("/api")
public class AdminToCommentsController {

    @Autowired
    AdminService adminService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${comments.nid.uid}")
    String cnu;

    @Value("${show.comments.nid}")
    String scn;

    @Value("${show.weigui.user.uid}")
    String swuu;

    // 审核评论统一接口
    @PostMapping("/auditComments")
    public String audit(@RequestBody AdminToComments adminToComments) {
        Admin admin = adminToComments.getAdmin();
        Comments comments = adminToComments.getComments();

        // 判断审核身份
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getAname, admin.getAname());
        lambdaQueryWrapper.eq(Admin::getPwd, admin.getPwd());
        boolean flag = adminService.exists(lambdaQueryWrapper);

        if (flag) {
            boolean cflag = commentsService.updateById(comments);
            if (cflag) {

                // 删除相关小说的评论缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys(cnu + comments.getNid() + ":*"));
                stringRedisTemplate.delete(scn + comments.getNid());

                if (comments.getState() == -1) {
                    String key = swuu + comments.getUid();
                    if (stringRedisTemplate.opsForValue().get(key) != null) {
                        stringRedisTemplate.opsForValue().increment(key, 1);
                    } else
                        stringRedisTemplate.opsForValue().set(key, "1");
                }

                return "操作成功";
            } else
                return "操作失败，请重试";
        } else
            return "管理员密码错误";
    }
}
