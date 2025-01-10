package com.share.novel;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class NovelApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private DataSource dataSource;

	//测试mysql连接
	@Test
	void connectMysqlTest(){
		try {
			System.out.println("连接数据库："+dataSource.getConnection());
		} catch (SQLException e) {
			System.out.println("连接失败");
		}
	}

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	//测试redis连接
	@Test
	void connectRedisTest(){
		stringRedisTemplate.opsForValue().set("test", "test");
		System.out.println(stringRedisTemplate.opsForValue().get("test"));
	}

}
