package com.ooad.good;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.ooad.good.mapper")
@SpringBootApplication
public class GoodModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodModuleApplication.class, args);
	}

}
