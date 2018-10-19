package com.excilys.cdb.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MainWebAppInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
	    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
	    context.register(AppConfig.class);
	    ContextLoaderListener contextLoaderListener = new ContextLoaderListener(context);
	    servletContext.addListener(contextLoaderListener);
	}
}

