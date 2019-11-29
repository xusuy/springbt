package com.xusy.springbt.datasources;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> THREAD_LOCALS = new ThreadLocal<>();

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(String dataSource) {
        THREAD_LOCALS.set(dataSource);
    }

    public static String getDataSource() {
        return THREAD_LOCALS.get();
    }

    public static void clearDataSource() {
        THREAD_LOCALS.remove();
    }

}
