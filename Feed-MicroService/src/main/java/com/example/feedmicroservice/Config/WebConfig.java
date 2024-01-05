package com.example.feedmicroservice.Config;

import com.example.feedmicroservice.Services.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Lazy
    private final MyInterceptor myInterceptor;

    public WebConfig(MyInterceptor myInterceptor){
        this.myInterceptor = myInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
    }
}
