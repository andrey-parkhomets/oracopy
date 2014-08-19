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
        String os$ORACLE_HOME = System.getenv("ORACLE_HOME");
        String os$TNS_ADMIN = System.getenv("TNS_ADMIN");
        String j$oracle_net_tns_admin= System.getProperty("oracle.net.tns_admin");
        String j$oracle_net_wallet_location= System.getProperty("oracle.net.wallet_location");
        System.out.println("$ORACLE_HOME="+os$ORACLE_HOME);
        System.out.println("$TNS_ADMIN"+os$TNS_ADMIN);
        System.out.println("-Doracle.net.tns.admin="+j$oracle_net_tns_admin);
        System.out.println("-Doracle.net.wallet.location="+j$oracle_net_wallet_location);
        if ( j$oracle_net_tns_admin == null ) {
               if (os$TNS_ADMIN == null) {
                   if (os$ORACLE_HOME==null)  {
                       System.out.println("=========================================================================================");
                       System.out.println("Please set java param -Doracle.net.tns_admin or env variables ORACLE_HOME/TNS_ADMIN");
                       System.out.println("=========================================================================================");
                       System.exit(-1);
                   } else { j$oracle_net_tns_admin = os$ORACLE_HOME+"\\network\\admin";
                           System.setProperty("oracle.net.tns_admin",j$oracle_net_tns_admin);
                           System.out.println("by using $ORACLE_HOME: -Doracle.net.tns.admin="+j$oracle_net_tns_admin);
                    }
               } else {    j$oracle_net_tns_admin =  os$TNS_ADMIN;
                           System.setProperty("oracle.net.tns_admin",j$oracle_net_tns_admin);
                           System.out.println("by using $TNS_ADMIN: -Doracle.net.tns.admin="+j$oracle_net_tns_admin);
               }
        }

        //System.getenv("ORACLE_HOME");
        //System.getProperty("oracle.net.tns_admin");
       // System.getProperty("oracle.net.wallet_location");
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
