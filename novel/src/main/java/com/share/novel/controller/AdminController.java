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
    public String login(String aname,String pwd) {
        return adminService.login(aname, pwd);
    }

    //添加管理员
    @PostMapping("/addAdmin")
    public String addAdmin(@RequestBody List<Admin> admin) {
        
        //firstAdmin为发出请求的管理员，secondAdmin为被添加的管理员
        Admin firstAdmin = admin.get(0);
        Admin secondAdmin = admin.get(1);

        return adminService.addAdmin(firstAdmin.getAname(), firstAdmin.getPwd(), secondAdmin);
        
    }
    
    
}
