package com.share.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.Admin;

public interface AdminService extends IService<Admin> {

    //管理员登陆
    String login(String aname,String pwd);
    
    //添加管理员
    String addAdmin(String aname,String pwd, Admin admin);

    //删除管理员
    String delAdmin(Integer aid,String pwd);

}
