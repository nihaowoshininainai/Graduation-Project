package com.share.novel.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.User;

/*
 * @param uname 用户名
 * @param pwd 密码
 * @param newPwd 新密码
 * @param oldPwd 旧密码
 */

public interface UserService extends IService<User>{

    //一个通用User生成token方法
    String getToken(User user);

    //注册
    String register(String uname,String pwd);

    //登陆
    String login(String uname,String pwd);

    //修改密码
    String resetPwd(Integer uid,String newPwd,String oldPwd);

    //注销功能
    String deleteUser(Integer uid,String pwd);

}
