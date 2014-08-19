package com.home.oracopy;



import java.io.*;
import java.sql.*;
import java.util.*;

public class Bridge {
    public static final String upload_txt = "upload";
    public static final String download_txt = "download";
    public static final String create_upload_txt = "create_upload";
    public static final String ls_txt = "ls";


    public static String NVL(String input1,String input2) {
        return ((input1 == null) ? input2 : input1);
    }


    public static void main(String[] args) throws Exception {
        Connector myConnector = new Connector();
        ArgReader myArgRead = new ArgReader();
        Map<String, String> inputArgumentMap = myArgRead.spaceParser(args);
        String fileName = NVL(inputArgumentMap.get("-f"),"");
        //Properties props = new Properties();
        String tnsAlias = inputArgumentMap.get("-tns");

        String myAction = inputArgumentMap.get("-a");
        if ( tnsAlias == null ||  tnsAlias.length() == 0 || (fileName == null ) || fileName.length() == 0)
        {
           if (!myAction.equals("ls")) {
               info.printUsage("-");
           }
        }
        //optional if wallet used
        String oraUser = NVL(inputArgumentMap.get("-u"), "");
        String oraPwd = NVL(inputArgumentMap.get("-p"), "");
        String tabName = NVL(inputArgumentMap.get("-table"), "FILE_LOAD");
        String lobFieldName = NVL(inputArgumentMap.get("-blob_field"), "FILE_DATA_BLOB");
        String keyFieldName = NVL(inputArgumentMap.get("-key_field"), "FILE_TAG");
      // System.out.println("oraUser="+oraUser);
      // System.out.println("oraPwd="+oraPwd);

              File file = new File(fileName);
              String keyFieldValue  = NVL(inputArgumentMap.get("-key_val"), fileName);


        if  (    !myAction.equals(upload_txt)
              && !myAction.equals(download_txt)
              && !myAction.equals(create_upload_txt)
              && !myAction.equals(create_upload_txt)
              && !myAction.equals(ls_txt)
                )  {
            System.out.println("===========");
            System.out.println(myAction);
            System.out.println("===========");

            info.printUsage("must be "+upload_txt+" or "+upload_txt+" or "+create_upload_txt+" or "+ls_txt+" ");

        }

        Connection conn = null;

        try {
            conn = myConnector.getMyConn(tnsAlias, oraUser, oraPwd);
        } catch (MyException ex) {
            ex.printStackTrace();
            System.out.println(ex.reason);
           /* if (ex.ex_code != 102) {
                System.exit(-1);
            }
            */
        }
        ParamHolder myParamHolder = new  ParamHolder(conn, file,tabName, lobFieldName, keyFieldName, keyFieldValue);
        if (myAction.equals(create_upload_txt)) {
            TableCreator mytabCreator = new TableCreator();
            mytabCreator.createDbUserTable(conn, tabName);
            Inserter myInserter = new Inserter();
            myInserter.doInsert_jdbc6(myParamHolder);
        } else if (myAction.equals(upload_txt)) {
            Inserter myInserter = new Inserter();
            myInserter.doInsert_jdbc6(myParamHolder);
        } else if (myAction.equals(download_txt)) {
            Fstreamer MyFstreamer = new Fstreamer();
            MyFstreamer.doFile(myParamHolder);
        } else if (myAction.equals(ls_txt)) {
            list Mylist = new list();
            Mylist.do_ls(myParamHolder);
        }
    }
}



