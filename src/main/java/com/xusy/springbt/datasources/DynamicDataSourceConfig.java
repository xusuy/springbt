package com.xusy.springbt.datasources;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置中心：通过@DataSource注释+spring aop动态配置数据源
 * 需要配置时：打开@Configuration注释，参照dataResourcesApplication.yml内容配置对应数据源
 *
 * @Date: 2019/1/8 11:25
 */
@SuppressWarnings("all")
//@Configuration
public class DynamicDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.base")
    public DataSource baseDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.authtest")
    public DataSource authTestDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.processtest")
    public DataSource processTestDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.formtest")
    public DataSource formTestDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean
    @ConfigurationProperties("spring.datasource.druid.reporttest")
    public DataSource reportTestDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.sharedtest")
    public DataSource sharedTestDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.mobiletest")
    public DataSource mobileTestDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.datatest")
    public DataSource dataTestDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.authprod")
    public DataSource authProdDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.processprod")
    public DataSource processProdDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.formprod")
    public DataSource formProdDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.reportprod")
    public DataSource reportProdDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.mobileprod")
    public DataSource mobileProdDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.sharedprod")
    public DataSource sharedProdDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.dataprod")
    public DataSource dataProdDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @SuppressWarnings("all")
    @Bean
    @Primary
    public DynamicDataSource dataSource(DataSource masterDataSource,
                                        DataSource baseDataSource,
                                        DataSource authTestDataSource,
                                        DataSource processTestDataSource,
                                        DataSource formTestDataSource,
                                        DataSource reportTestDataSource,
                                        DataSource mobileTestDataSource,
                                        DataSource dataTestDataSource,
                                        DataSource authProdDataSource,
                                        DataSource processProdDataSource,
                                        DataSource formProdDataSource,
                                        DataSource reportProdDataSource,
                                        DataSource mobileProdDataSource,
                                        DataSource dataProdDataSource
    ) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        // 主数据库
        targetDataSources.put(DataSourceNames.MASTER, masterDataSource);
        // 基础数据库
        targetDataSources.put(DataSourceNames.BASE, baseDataSource);
        // 开发
        targetDataSources.put(DataSourceNames.AUTHTEST, authTestDataSource);
        targetDataSources.put(DataSourceNames.PROCESSTEST, processTestDataSource);
        targetDataSources.put(DataSourceNames.FORMTEST, formTestDataSource);
        targetDataSources.put(DataSourceNames.REPORTTEST, reportTestDataSource);
        targetDataSources.put(DataSourceNames.MOBILETEST, mobileTestDataSource);
        targetDataSources.put(DataSourceNames.DATATEST, dataTestDataSource);
        // 应用
        targetDataSources.put(DataSourceNames.AUTHPROD, authProdDataSource);
        targetDataSources.put(DataSourceNames.PROCESSPROD, processProdDataSource);
        targetDataSources.put(DataSourceNames.FORMPROD, formProdDataSource);
        targetDataSources.put(DataSourceNames.REPORTPROD, reportProdDataSource);
        targetDataSources.put(DataSourceNames.MOBILEPROD, mobileProdDataSource);
        targetDataSources.put(DataSourceNames.DATAPROD, dataProdDataSource);
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }

}
