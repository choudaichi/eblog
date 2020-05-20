package com.koodo.eblog.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.koodo.eblog.mapper")
public class MybatisPlusConfig {
}
