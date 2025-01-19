package com.share.novel.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.share.novel.NovelApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = NovelApplication.class)
@MapperScan(basePackageClasses = AdminMapper.class)
public class AdminMapperTest {

    @Autowired
    AdminMapper adminMapper;

    @Test
    void test(){
        adminMapper.selectList(null).forEach(System.out::println);
    }
}
