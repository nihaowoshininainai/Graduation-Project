package com.share.novel.service.impl;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.User;
import com.share.novel.mapper.UserMapper;
import com.share.novel.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {


}
