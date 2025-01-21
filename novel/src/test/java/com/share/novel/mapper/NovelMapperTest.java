package com.share.novel.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.share.novel.NovelApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = NovelApplication.class)
@MapperScan(basePackageClasses = NovelMapper.class)
public class NovelMapperTest {

    @Autowired
    NovelMapper novelMapper;

    @Test
    void show(){
        novelMapper.selectList(null).forEach(System.out::println);
    }
}
