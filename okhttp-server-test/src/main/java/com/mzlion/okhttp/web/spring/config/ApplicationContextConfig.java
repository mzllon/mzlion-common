package com.mzlion.okhttp.web.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * <p>
 * 2016-05-07 help spring to load controller
 * </p>
 *
 * @author mzlion
 */
@Configuration
@ComponentScan(value = "com.mzlion.okhttp.service.impl")
@Import(SqlSessionFactoryConfig.class)
public class ApplicationContextConfig {

    /**
     * Tell spring to resolve placeholder string ,such as '${jdbc.url}'.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
