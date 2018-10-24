package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


@EnableWebMvc
@Configuration
@ComponentScan(basePackages ={"com.excilys.cdb.config", "com.excilys.cdb.mapper", "com.excilys.cdb.persistence",
		"com.excilys.cdb.service", "com.excilys.cdb.servlet", "com.excilys.cdb.ui", "com.excilys.cdb.controller"})
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
    public LocaleChangeInterceptor localeInterceptor(){
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
	}

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }
}
