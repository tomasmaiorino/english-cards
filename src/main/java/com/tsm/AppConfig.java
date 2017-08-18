package com.tsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tsm.cards.util.PostCallsInterceptor;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private PostCallsInterceptor postInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(postInterceptor);
    }
}
