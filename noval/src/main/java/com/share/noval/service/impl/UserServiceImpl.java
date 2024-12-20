package com.share.noval.service.impl;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.noval.base.User;
import com.share.noval.mapper.UserMapper;
import com.share.noval.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {


}
