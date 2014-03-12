package com.home.oracopy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {


    public Connection getMyConn(String tns, String user, String pwd) throws MyException {
        // System.out.println("get in getMyConn");
        Connection myConn = null;
        if (myConn != null) {
            System.out.println("return connection");
            return myConn;
        }


        Properties props = new Properties();

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {

            MyException ex = new MyException(e);
            ex.reason = "no oracle driver";
            ex.ex_code = 101;
            throw ex;
        }
        // oracle.net.tns_admin should be set as System property only!!!
        //System.setProperty("oracle.net.tns_admin","E:\\app\\%USERNAME%\\product\\11.2.0\\client_1\\network\\admin\\wallet");
        //props.setProperty("oracle.net.wallet_location","(SOURCE=(METHOD=file)(METHOD_DATA=(DIRECTORY=" +
        //        "E:\\app\\%USERNAME%\\product\\11.2.0\\client_1\\network\\admin\\wallet)))");
        //System.setProperty("oracle.net.tns_admin",props.getProperty("oracle.net.wallet_location"));
        String url = "jdbc:oracle:thin:"+user+"/"+pwd+"@"+ tns;
        //String url = "jdbc:oracle:thin:/@"+ tns;
        //System.out.print("url: "+url);
        try {

            myConn = DriverManager.getConnection(url, props);//, "USER","PWD");

        } catch (SQLException e) {
            MyException ex = new MyException(e);
            ex.reason = "cannot get connection";
            ex.ex_code = 102;
            throw ex;
        }


        System.out.println("Connected to tns = " + tns);

        return myConn;
    }


}
