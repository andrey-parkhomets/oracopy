package com.home.oracopy;

import java.sql.*;

public class TableCreator {
    public void createDbUserTable(Connection conn, String table) throws SQLException {

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
}
