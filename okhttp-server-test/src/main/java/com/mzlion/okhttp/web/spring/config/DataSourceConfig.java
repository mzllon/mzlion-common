package com.mzlion.okhttp.web.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * <p>
 * 2016-05-07 Init Datasource
 * </p>
 *
 * @author mzlion
 */
@Configuration
@PropertySource(value = "classpath:/jdbc.properties")
public class DataSourceConfig {
    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${jdbc.driverClassName}")
    private String driverClassName;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;


    @Value("classpath:sql/schema.sql")
    private Resource schemaScript;

    @Value("classpath:sql/init-data.sql")
    private Resource dataScript;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        logger.info(" <=== init datasource starting.");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setPassword(password);
        logger.info(" ===> init datasource finished.");

        logger.info(" ===> prepare create tables and init data");
        DatabasePopulatorUtils.execute(this.createDatabasePopulator(), dataSource);
        return dataSource;
    }

    private DatabasePopulator createDatabasePopulator() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(schemaScript);
        resourceDatabasePopulator.addScript(dataScript);
        return resourceDatabasePopulator;
    }

}
