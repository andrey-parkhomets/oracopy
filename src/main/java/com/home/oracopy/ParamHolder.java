package com.home.oracopy;


import java.io.File;
import java.sql.Connection;

public class ParamHolder {
    public  Connection conn ;
    public  File file ;
    public  String table ;
    public  String clobFieldName;
    public  String keyFieldName;
    public  String keyFieldValue;

   // constructor
    public ParamHolder (Connection initConn, File initFile, String initTable, String initClobFieldName, String initKeyFieldName, String initKeyFieldValue) {
        conn = initConn;
        file = initFile;
        table = initTable;
        clobFieldName = initClobFieldName;
        keyFieldName = initKeyFieldName;
        keyFieldValue = initKeyFieldValue;
    }
}
