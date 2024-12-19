package com.share.noval.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.share.noval.NovalApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = NovalApplication.class)
@MapperScan(basePackageClasses = UserMapper.class)
public class UserMapperTest {
    
    @Autowired
    private UserMapper userMapper;

    @Test
    void test(){
        userMapper.selectList(null).forEach(System.out::println);
    }
}
