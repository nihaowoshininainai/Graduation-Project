package com.share.noval.mapper;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = {
    DataSourceAutoConfiguration.class,
    MybatisPlusAutoConfiguration.class,
    DataSource.class,
    SqlSessionFactory.class
})
@MapperScan(basePackageClasses = UserMapper.class)
//UserMapper的测试类
public class UserMapperTest {
    
    @Autowired
    private UserMapper userMapper;

    @Test
    void test(){
        userMapper.selectList(null).forEach(System.out::println);
    }
}
