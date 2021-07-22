package com.xxl.job.admin.supportdb;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * 数据源配置
 *
 * @author liuyz
 * @version 1.0.0
 * @date 2021-7-22 17:16:41
 */
@Configuration
public class DataSourceConfig {
    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties p = new Properties();
        p.setProperty("MySQL", "mysql");
        p.setProperty("Kingbase", "kingbase");
//        p.setProperty("Oracle", "oracle");
//        p.setProperty("PostgreSQL", "postgresql");
//        p.setProperty("DB2", "db2");
//        p.setProperty("SQL Server", "sqlserver");
        databaseIdProvider.setProperties(p);
        return databaseIdProvider;
    }

    @Primary
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setDatabaseIdProvider(databaseIdProvider());
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return factoryBean;
    }
}

