package com.looksea.db;

import java.sql.Connection;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DBConnection {

    private static final String host = "50.63.244.123";
    private static final String dbName = "looksea";
    private static final String un = "looksea";
    private static final String pw = "Fiume3640!";

    private static boolean isPoolSetUp;
    private static DataSource datasource;

    public static Connection getConnection() throws Exception {

        if (!isPoolSetUp) {
            DBConnection.setupConnectionPool();
        }
        Connection con = datasource.getConnection();
        return con;
    }

    public static void setupConnectionPool() {

        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://" + host + ":3306/" + dbName);
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername(un);
        p.setPassword(pw);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource = new DataSource();
        datasource.setPoolProperties(p);

        isPoolSetUp = true;
    }
}
