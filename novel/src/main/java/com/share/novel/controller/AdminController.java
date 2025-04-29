package com.share.novel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Admin;
import com.share.novel.service.AdminService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    AdminService adminService;

    //登陆
    @PostMapping("/alogin")
    public String login(@RequestBody Admin admin) {
        return adminService.login(admin.getAname(),admin.getPwd());
    }

    //添加管理员
    @PostMapping("/addAdmin")
    public String addAdmin(@RequestBody List<Admin> admins) {
        
        //firstAdmin为发出请求的管理员，secondAdmin为被添加的管理员
        Admin firstAdmin = admins.get(0);
        Admin secondAdmin = admins.get(1);

        return adminService.addAdmin(firstAdmin.getAname(), firstAdmin.getPwd(), secondAdmin);
        
    }
    
    //删除管理员
    @PostMapping("/delAdmin")
    public String delAdmin(@RequestBody List<Admin> admins){

        //firstAdmin为发出请求的管理员，secondAdmin为被删除的管理员
        Admin firstAdmin = admins.get(0);
        Admin secondAdmin = admins.get(1);

        String result = adminService.delAdmin(firstAdmin.getAname(), firstAdmin.getPwd(), secondAdmin);
        
        return result;
    }
}
