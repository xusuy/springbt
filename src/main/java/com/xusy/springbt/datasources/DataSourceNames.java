package com.xusy.springbt.datasources;

public interface DataSourceNames {
    /**
     * 主数据库
     */
    String MASTER = "master";
    /**
     * 基础数据库
     */
    String BASE = "base";
    /**
     * 认证(开发)
     */
    String AUTHTEST = "authtest";
    /**
     * 流程(开发)
     */
    String PROCESSTEST = "processtest";
    /**
     * 表单(开发)
     */
    String FORMTEST = "formtest";
    /**
     * 报表(开发)
     */
    String REPORTTEST = "reporttest";
    /**
     * 手机(开发)
     */
    String MOBILETEST = "mobiletest";
    /**
     * 数据(开发)
     */
    String DATATEST = "datatest";
    /**
     * 认证(应用)
     */
    String AUTHPROD = "authprod";
    /**
     * 流程(应用)
     */
    String PROCESSPROD = "processprod";
    /**
     * 表单(应用)
     */
    String FORMPROD = "formprod";
    /**
     * 报表(应用)
     */
    String REPORTPROD = "reportprod";
    /**
     * 手机(开发)
     */
    String MOBILEPROD = "mobileprod";
    /**
     * 数据(应用)
     */
    String DATAPROD = "dataprod";

    String getDataSourceName();
}
