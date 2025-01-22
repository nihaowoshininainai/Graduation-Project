package com.share.novel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.share.novel.utils.ControllerUtil;

@Configuration
public class ControllerUtilConfig {

    @Bean
    public ControllerUtil controllerUtil(){
        ControllerUtil controllerUtil = new ControllerUtil();
        return controllerUtil;
    }
}
