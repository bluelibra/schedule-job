package com.lvhao.schedulejob.config.datasource;

import com.google.common.collect.Maps;
import com.lvhao.schedulejob.common.AppConst;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;

/**
 * 从配置文件中读取db配置
 *
 * @author: lvhao
 * @since: 2016-4-15 17:10
 */
@Configuration
public class TargetDataSourceConfig {

    /**
     * 从默认配置文件出读取db配置
     * 此处需要写上classpath 否则无法找到资源 导致绑定失败
     * 具体查看如下方法
     * {@link org.springframework.core.io.DefaultResourceLoader#getResource}
     * @return
     */
    @Bean
    @ConfigurationProperties(
            locations= "classpath:config/datasource.yml",
            prefix="datasource.default")
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(
            locations= "classpath:config/datasource.yml",
            prefix="datasource.read")
    public DataSource readDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(
            locations= "classpath:config/datasource.yml",
            prefix="datasource.write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 组装db到Map
    // 注册到 DynamicDataSource的targetDataSources属性
    @PostConstruct
    public Map<Object,Object> getDataSourceMap() {
        Map<Object,Object> dataMap = Maps.newHashMap();

        dataMap.put(AppConst.DbKey.DEFAULT, this.defaultDataSource());
        dataMap.put(AppConst.DbKey.READ, this.readDataSource());
        dataMap.put(AppConst.DbKey.WRITE, this.writeDataSource());

        DataSourceContextHolder.appendDbKey2Set(
                AppConst.DbKey.DEFAULT,
                AppConst.DbKey.READ,
                AppConst.DbKey.WRITE
        );
        return dataMap;
    }
}
