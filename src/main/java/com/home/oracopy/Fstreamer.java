package com.home.oracopy;

import java.io.File;
import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class Fstreamer {
    public void doFile(ParamHolder myParamHolder//Connection conn, File file, String table, String blobField, String keyField, String keyValue
                        ) {
        try {    //File MyFile = new File(file.getPath() + file.getName());
            if (!myParamHolder.file.exists()) {
                myParamHolder.file.createNewFile() ;
            }


            String colType = null;
            PreparedStatement pstmt0 = myParamHolder.conn.prepareStatement("select     max(x.DATA_TYPE) as DATA_TYPE    from user_tab_columns x where x.TABLE_NAME = ? and x.COLUMN_NAME = ? ");

            pstmt0.setString(1, myParamHolder.table.toUpperCase());
            pstmt0.setString(2, myParamHolder.clobFieldName.toUpperCase());

            ResultSet rs0 = pstmt0.executeQuery();

            while (rs0.next()) {
                colType = rs0.getString("DATA_TYPE");

                if (colType == null || colType.length() == 0) {
                    System.out.print("========== Error: can't identify datatype for " + myParamHolder.table.toUpperCase() + "."+myParamHolder.clobFieldName.toUpperCase()+" ! Object\\attribute not exist for this connection ?");
                    System.exit(-1);
                }
            }

            FileInputStream fis = new FileInputStream(myParamHolder.file);
            PreparedStatement pstmt = myParamHolder.conn.prepareStatement("select  " + myParamHolder.clobFieldName.toUpperCase() + "  from " + myParamHolder.table.toUpperCase() + " where " + myParamHolder.keyFieldName + "=? and rownum < 2");
            pstmt.setString(1, myParamHolder.keyFieldValue);
            ResultSet rs = pstmt.executeQuery();
            long filebytesize = 0;
            int bufSize = 1024;
            if (rs.next()) {
                if (colType.equals("BLOB")) {
                    Blob blob = rs.getBlob(myParamHolder.clobFieldName);
                    filebytesize = blob.length();
                    if   (filebytesize == 0L ){
                        System.out.println(" ================================");
                        System.out.println(" Null file length detected !!!");
                        System.out.println(" ================================");
                    }
                    System.out.println(" the ETA File size byte = " + filebytesize);
                    InputStream blobInputStream = null;
                    blobInputStream = blob.getBinaryStream();
                    FileOutputStream out = new FileOutputStream(myParamHolder.file);
                    BufferedOutputStream bos = new BufferedOutputStream(out);
                    long bytedone = 0;
                    byte[] buffer = new byte[bufSize];
                    int length = -1;
                    //int cnt  = 0;
                    int pecentdone;
                    while ((length = blobInputStream.read(buffer)) != -1) {
                        //System.out.println("cnt=" + cnt+ " bytedone="+bytedone);
                        // cnt=cnt+1;
                        bytedone = bytedone + length;
                        bos.write(buffer, 0, length);
                        pecentdone = (int) (((double) bytedone / (double) filebytesize) * 100d);
                        printProgBar.printProgBar(pecentdone);
                    }
                    bos.flush();
                    bos.close();
                } else if (colType.equals("CLOB")) {
                    Clob clob = rs.getClob(myParamHolder.clobFieldName);
                    filebytesize = clob.length();
                    System.out.println(" the ETA File size byte = " + filebytesize);
                    InputStream clobInputStream = null;
                    clobInputStream = clob.getAsciiStream();
                    FileOutputStream out = new FileOutputStream(myParamHolder.file);
                    BufferedOutputStream bos = new BufferedOutputStream(out);
                    long bytedone = 0;
                    byte[] buffer = new byte[bufSize];
                    int length = -1;
                    //int cnt  = 0;
                    int pecentdone;
                    while ((length = clobInputStream.read(buffer)) != -1) {
                        bytedone = bytedone + length;
                        bos.write(buffer, 0, length);
                        pecentdone = (int) (((double) bytedone / (double) filebytesize) * 100d);
                        printProgBar.printProgBar(pecentdone);
                    }
                    bos.flush();
                    bos.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}