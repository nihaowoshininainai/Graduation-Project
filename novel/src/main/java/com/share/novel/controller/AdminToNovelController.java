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

import com.share.novel.base.Admin;
import com.share.novel.base.Novel;
import com.share.novel.mapper.AdminToNovelMapper;
import com.share.novel.service.AdminService;
import com.share.novel.service.NovelService;
import com.share.novel.utils.RedisUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    RedisUtil redisUtil;

    @Value("${novel.id}")
    String ni;

    @Value("${novel.uid}")
    String nu;

    @Value("${show.weigui.user.uid}")
    String swuu;

    @Data
    static class AdminToNovel {
        private Novel novel;
    }

    @PostMapping("/auditNovel")
    public Result<?> audit(@RequestBody AdminToNovel adminToNovel, @RequestParam String record, HttpServletRequest request) {
        Result<?> authResult = adminService.testAdminLogin(request);
        if (!authResult.isSuccess()) {
            return Result.error("管理员身份验证失败", null);
        }
        Admin admin = (Admin) authResult.getData();
        Novel novel = adminToNovel.getNovel();

        boolean nflag = novelService.updateById(novel);
        if (nflag) {

            com.share.novel.base.AdminToNovel novelRecord = new com.share.novel.base.AdminToNovel();
            novelRecord.setAid(admin.getAid());
            novelRecord.setNid(novel.getId());
            novelRecord.setRecord(record);
            novelRecord.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            adminToNovelMapper.insert(novelRecord);

            redisUtil.deleteByPattern(ni + novel.getId() + "*");
            redisUtil.deleteByPattern(nu + novel.getUid() + "*");
            redisUtil.deleteByPattern("show:novels:*");

            if (novel.getState() == -1 || novel.getState() == 3) {
                String key = swuu + novel.getUid();
                if (stringRedisTemplate.opsForValue().get(key) != null) {
                    stringRedisTemplate.opsForValue().increment(key, 1);
                } else
                    stringRedisTemplate.opsForValue().set(key, "1");
            }

            return Result.success("操作成功", null);
        } else
            return Result.error("操作失败，请重试", null);
    }

    @PostMapping("/deleteNovelByAdmin")
    public Result<?> delete(@RequestBody AdminToNovel adminToNovel, HttpServletRequest request) {
        Result<?> authResult = adminService.testAdminLogin(request);
        if (!authResult.isSuccess()) {
            return Result.error("管理员身份验证失败", null);
        }
        Novel novel = adminToNovel.getNovel();

        boolean nflag = novelService.removeById(novel.getId());
        if (nflag) {

            redisUtil.deleteByPattern(ni + novel.getId() + "*");
            redisUtil.deleteByPattern(nu + novel.getUid() + "*");
            redisUtil.deleteByPattern("show:novels:*");

            return Result.success("操作成功", null);
        } else
            return Result.error("操作失败，请重试", null);
    }
}
