package com.mzlion.okhttp.web.spring.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * <p>
 * 2016-05-07 Config SessionFactory
 * </p>
 *
 * @author mzlion
 */
@Configuration
@Import(DataSourceConfig.class)
@MapperScan(basePackages = "com.mzlion.okhttp.dao")
public class SqlSessionFactoryConfig {
    private final Logger logger = LoggerFactory.getLogger(SqlSessionFactoryConfig.class);

    @Resource(name = "dataSource")
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        logger.info(" ===> Init SqlSessionFactoryBean starting.");
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(this.dataSource);
        logger.info(" ===> Init SqlSessionFactoryBean finished.");
        return sqlSessionFactoryBean;
    }

    @Bean(name = "dataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource);
    }
}
