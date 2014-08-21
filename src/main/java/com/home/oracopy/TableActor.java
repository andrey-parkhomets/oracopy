package com.home.oracopy;

import java.sql.*;

public class TableActor {
    public void createTable(Connection conn, String table) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        String createTableSQL = "CREATE TABLE " + table.toUpperCase() + " \n( \n"
                + "file_name VARCHAR(500) , \n"
                + "file_tag VARCHAR(500) , \n"
                + "file_data_blob BLOB , \n"
                + "load_date  DATE default sysdate , \n"
                + "load_by VARCHAR(200) default sys_context('USERENV', 'OS_USER')||'@'||sys_context('USERENV', 'HOST')||':'||sys_context('USERENV', 'MODULE') \n"
                + ")\n";
        try {
            dbConnection = conn;
            statement = dbConnection.createStatement();
            System.out.println(createTableSQL);
            // execute the SQL stetement
            statement.execute(createTableSQL);
            System.out.println("Table \"" + table + "\" is created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    }
    public void cleanTable(Connection conn, String table, String keyField, String keyVal ) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        String tableAction;
        if (keyVal == null || keyVal.length() == 0) {
             tableAction = "DELETE FROM " + table.toUpperCase() ;
        } else
             tableAction = "DELETE FROM " + table.toUpperCase() + " WHERE "+keyField.toUpperCase()+"='"+keyVal+"'";

        try {
            dbConnection = conn;
            statement = dbConnection.createStatement();
            System.out.println(tableAction);
            // execute the SQL stetement
            int rows = statement.executeUpdate(tableAction);
            if (rows!=0) {
                System.out.println("Table \"" + table + "\" is cleaned! " + rows + " rows affected");
            } else {System.out.println("no one rows affected");}
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    }
}
