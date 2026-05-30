package com.share.novel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Admin;
import com.share.novel.service.AdminService;
import com.share.novel.utils.Result;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/alogin")
    public Result<?> login(@RequestBody Admin admin) {
        return adminService.login(admin.getAname(),admin.getPwd());
    }

    @PostMapping("/adminLogin")
    public Result<?> adminLogin(@RequestBody Admin admin) {
        return adminService.adminLogin(admin.getAname(), admin.getPwd());
    }

    @PostMapping("/addAdmin")
    public Result<?> addAdmin(@RequestBody List<Admin> admins) {

        Admin firstAdmin = admins.get(0);
        Admin secondAdmin = admins.get(1);

        return adminService.addAdmin(firstAdmin.getAname(), firstAdmin.getPwd(), secondAdmin);

    }

    @PostMapping("/delAdmin")
    public Result<?> delAdmin(@RequestBody List<Admin> admins){

        Admin firstAdmin = admins.get(0);
        Admin secondAdmin = admins.get(1);

        return adminService.delAdmin(firstAdmin.getAname(), firstAdmin.getPwd(), secondAdmin);
    }
}
