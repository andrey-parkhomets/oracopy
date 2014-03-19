package com.home.oracopy;


import java.io.File;
import java.sql.*;

public class list {
    public void do_ls(Connection conn, File file, String table, String lobFieldName, String keyFieldName, String keyFieldValue){

        Connection dbConnection = null;
        PreparedStatement pstatement = null;

        String SelectTabSQL = "select File_Name\n" +
                            "      ,"+keyFieldName.toUpperCase()+"\n" +
                            "      ,round(dbms_lob.getlength("+lobFieldName.toUpperCase()+")/1024) as Kb_size\n" +
                            "      ,Load_Date\n" +
                            "      ,Load_By\n" +
                            "  from "+table.toUpperCase() +" order by Load_Date desc" ;
        try {
            dbConnection = conn;
            pstatement = conn.prepareStatement(SelectTabSQL);
            ResultSet rs = pstatement.executeQuery();
            System.out.printf("====================================================\n");
            System.out.printf("=======FileName====== ; ==========KeyVal============ \n");
            while(rs.next()){
                //Retrieve by column name
                String FileNameHead  = rs.getString("File_Name");
                String keyFieldNameHead = rs.getString(keyFieldName.toUpperCase());

                //Display values
                System.out.print(""+FileNameHead+"  ;  ");
                System.out.print(""+keyFieldNameHead+"");
                System.out.print("\n");
            }
            rs.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(pstatement!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();

            }//end finally try
        }//end try

    }//end main
}