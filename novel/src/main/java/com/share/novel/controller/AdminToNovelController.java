package com.share.novel.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.share.novel.base.Admin;
import com.share.novel.base.Novel;
import com.share.novel.mapper.AdminToNovelMapper;
import com.share.novel.service.AdminService;
import com.share.novel.service.NovelService;

import lombok.Data;

@RestController
@RequestMapping("/api")
public class AdminToNovelController {

    @Autowired
    NovelService novelService;

    @Autowired
    AdminService adminService;

    @Autowired
    AdminToNovelMapper adminToNovelMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${novel.id}")
    String ni;

    @Value("${novel.uid}")
    String nu;

    @Value("${show.weigui.user.uid}")
    String swuu;

    @Data
    static class AdminToNovel {
        private Admin admin;
        private Novel novel;
    }

    // 审核小说统一接口
    @PostMapping("/auditNovel")
    public String audit(@RequestBody AdminToNovel adminToNovel,@RequestParam String record) {
        Admin admin = adminToNovel.getAdmin();
        Novel novel = adminToNovel.getNovel();

        // 判断管理员身份
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getAname, admin.getAname());
        lambdaQueryWrapper.eq(Admin::getPwd, admin.getPwd());
        boolean flag = adminService.exists(lambdaQueryWrapper);

        if (flag) {
            boolean nflag = novelService.updateById(novel);
            if (nflag) {

                com.share.novel.base.AdminToNovel novelRecord = new com.share.novel.base.AdminToNovel();
                novelRecord.setAid(admin.getAid());
                novelRecord.setNid(novel.getId());
                novelRecord.setRecord(record);
                novelRecord.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                adminToNovelMapper.insert(novelRecord);

                // 删除相关小说缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys(ni + novel.getId() + "*"));
                stringRedisTemplate.delete(stringRedisTemplate.keys(nu + novel.getUid() + "*"));
                stringRedisTemplate.delete(stringRedisTemplate.keys(("show:novels:*")));

                if (novel.getState() == -1 || novel.getState() == 3) {
                    String key = swuu + novel.getUid();
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

    // 删除小说
    @PostMapping("/deleteNovelByAdmin")
    public String delete(@RequestBody AdminToNovel adminToNovel) {
        Admin admin = adminToNovel.getAdmin();
        Novel novel = adminToNovel.getNovel();

        // 判断管理员身份
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getAname, admin.getAname());
        lambdaQueryWrapper.eq(Admin::getPwd, admin.getPwd());
        boolean flag = adminService.exists(lambdaQueryWrapper);

        if (flag) {
            boolean nflag = novelService.removeById(novel.getId());
            if (nflag) {

                // 删除相关小说缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys(ni + novel.getId() + "*"));
                stringRedisTemplate.delete(stringRedisTemplate.keys(nu + novel.getUid() + "*"));
                stringRedisTemplate.delete(stringRedisTemplate.keys(("show:novels:*")));

                return "操作成功";
            } else
                return "操作失败，请重试";

        } else
            return "管理员密码错误";
    }
}
