package com.heeexy.example.config.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfigurationInitializer implements  WebMvcConfigurer  {
	@Value("${imgPath}")
	private String imgPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/img/**").addResourceLocations("file:"+imgPath);
		/* super.addResourceHandlers(registry); */
    }
}

