package com.epam.lab.spider.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Boyarsky Vitaliy on 12.06.2015.
 */
public class PoolConnection {

    private static DataSource dataSource = null;

    private PoolConnection() {
        super();
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            dataSource = init();
        }
        return dataSource.getConnection();
    }

    private static DataSource init() {
        DataSource ds = null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            // Look up our data source
             ds = (DataSource) envCtx.lookup("jdbc/vk_spider");

            // Allocate and use a connection from the pool

            /*InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/vk_spider");*/
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return ds;
    }

}
