package com.kosa2.hospital.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")           // 전체 경로에 대해 인터셉터 적용
                .excludePathPatterns(             // 예외(로그인 필요 없는 경로)
                        "/login",
                        "/",
                        "/doctors/new",
                        "/new",
                        "/logout",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/error"
                );
    }
}