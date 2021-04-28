package com.exc.unipi_agenda.model;

import java.sql.*;

public class Db
{
    public static final int MysqlDuplicatePkexception = 1062;

    public static int getMysqlDuplicatePkexception() {
        return MysqlDuplicatePkexception;
    }

    public static Connection getConnection() {
        try{
            String connectionUrl = "jdbc:mysql://localhost:3306/unipi_agenda?serverTimezone=UTC";
            String connectionUsername = "root";
            String connectionPassword = "root";
            return DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);
        } catch (SQLException e) {
            return null;
        }
    }
}
