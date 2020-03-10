package com.xusy.springbt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan(basePackages = "com.xusy.springbt.mapper")
@EnableAsync
public class SpringbtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbtApplication.class, args);
    }
}
