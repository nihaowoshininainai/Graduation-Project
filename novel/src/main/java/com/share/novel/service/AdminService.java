package com.share.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.Admin;
import com.share.novel.base.AdminToNovel;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

public interface AdminService extends IService<Admin> {

    Result<?> login(String aname,String pwd);

    Result<?> addAdmin(String aname,String pwd, Admin admin);

    Result<?> delAdmin(String aname,String pwd, Admin admin);

    java.util.List<AdminToNovel> getShenheRecord();

    Result<?> adminLogin(String aname, String pwd);

    Result<?> testAdminLogin(HttpServletRequest request);

}
