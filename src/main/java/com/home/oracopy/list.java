package com.home.oracopy;


import java.io.File;
import java.sql.*;

public class list {
    public void do_ls(ParamHolder myParamHolder){

        Connection dbConnection = null;
        PreparedStatement pstatement = null;

        String SelectTabSQL = "select File_Name\n" +
                            "      ,"+myParamHolder.keyFieldName.toUpperCase()+"\n" +
                            "      ,round(dbms_lob.getlength("+myParamHolder.clobFieldName.toUpperCase()+")/1024) as Kb_size\n" +
                            "      ,Load_Date\n" +
                            "      ,Load_By\n" +
                            "  from "+myParamHolder.table.toUpperCase() +" order by Load_Date desc" ;
        try {
            dbConnection = myParamHolder.conn;
            pstatement = myParamHolder.conn.prepareStatement(SelectTabSQL);
            ResultSet rs = pstatement.executeQuery();
            System.out.printf("====================================================\n");
            System.out.printf("=======FileName====== ; ==========KeyVal============ \n");
            while(rs.next()){
                //Retrieve by column name
                String FileNameHead  = rs.getString("File_Name");
                String keyFieldNameHead = rs.getString(myParamHolder.keyFieldName.toUpperCase());

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
                    myParamHolder.conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(myParamHolder.conn!=null)
                    myParamHolder.conn.close();
            }catch(SQLException se){
                se.printStackTrace();

            }//end finally try
        }//end try

    }//end main
}