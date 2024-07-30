package com.bbYang.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileConfig implements WebMvcConfigurer {

    private final FileProperties properties;

    //특정 요청에 대한 정적 리소스 처리 핸들러
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // /upload/**
        registry.addResourceHandler(properties.getUrl()+"**").addResourceLocations("file:///"+properties.getPath());
        //url로 시작하는 요청을 path경로의 파일로 매핑
    }
}
