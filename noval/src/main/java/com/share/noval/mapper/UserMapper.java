package com.share.noval.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.share.noval.base.User;

@Mapper
//用户表持久层
public interface UserMapper extends BaseMapper<User> {

}
