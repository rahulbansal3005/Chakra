package com.increff.chakra.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(value = { SpringConstants.PACKAGE_ACCOUNT, SpringConstants.PACKAGE_CHAKRA})
@EnableWebMvc
@PropertySource(value = "file:chakra.properties", ignoreResourceNotFound = false)
public class AppConfig {



}
