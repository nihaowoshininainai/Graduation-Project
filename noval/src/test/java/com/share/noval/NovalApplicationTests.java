package com.share.noval;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NovalApplicationTests {

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

}
