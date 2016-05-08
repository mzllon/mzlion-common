package com.mzlion.okhttp.web.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Help spring to load DispatchServlet
 *
 * @author mzlion
 */
@EnableWebMvc
@Configuration
@ComponentScan(value = "com.mzlion.okhttp.controller")
public class WebContextConfig extends WebMvcConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(WebContextConfig.class);


}
