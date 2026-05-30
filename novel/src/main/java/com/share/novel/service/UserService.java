package com.share.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.User;
import com.share.novel.utils.Result;

public interface UserService extends IService<User>{

    String getToken(User user);

    Result<?> register(String uname,String pwd);

    Result<?> login(String uname,String pwd);

    Result<?> resetPwd(Integer uid,String newPwd,String oldPwd);

    Result<?> deleteUser(Integer uid,String pwd);

    Result<?> getUname(Integer id);

    Result<?> getUid(String uname);

}
