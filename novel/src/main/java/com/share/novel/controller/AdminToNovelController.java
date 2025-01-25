package com.share.novel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.share.novel.base.Admin;
import com.share.novel.base.Novel;
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

    @Data
    static class AdminToNovel {
        private Admin admin;
        private Novel novel;  
    }

    //审核统一接口
    public String audit(@RequestBody AdminToNovel adminToNovel){
        Admin admin = adminToNovel.getAdmin();
        Novel novel = adminToNovel.getNovel();

        //判断管理员身份
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getAname, admin.getAname());
        lambdaQueryWrapper.eq(Admin::getPwd, admin.getPwd());
        boolean flag = adminService.exists(lambdaQueryWrapper);

        if(flag){
            boolean uflag = novelService.updateById(novel);
            if(uflag)
                return "操作成功";
            else 
                return "操作失败，请重试";
        }
        else
            return "管理员密码错误";
    }
}
