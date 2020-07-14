package com.koodo.eblog.config;

import com.koodo.eblog.common.lang.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class EblogInterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    Consts consts;


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new EblogInterceptor()).excludePathPatterns("/res/**");
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**", "/upload/avatar/**").addResourceLocations("classpath:/static/", "file:///" + consts.getUploadDir() + "/avatar/");
        super.addResourceHandlers(registry);
    }
}
