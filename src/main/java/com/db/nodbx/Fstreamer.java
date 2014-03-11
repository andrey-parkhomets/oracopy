package com.db.nodbx;

import java.io.File;
import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class Fstreamer {
    public void doFile(Connection conn, File file, String table, String blobField, String keyField, String keyValue) {
        try {    //File MyFile = new File(file.getPath() + file.getName());
            if (!file.exists()) {
                file.createNewFile() ;
            }


            String colType = null;
            PreparedStatement pstmt0 = conn.prepareStatement("select     max(x.DATA_TYPE) as DATA_TYPE    from user_tab_columns x where x.TABLE_NAME = ? and x.COLUMN_NAME = ? ");

            pstmt0.setString(1, table.toUpperCase());
            pstmt0.setString(2, blobField.toUpperCase());

            ResultSet rs0 = pstmt0.executeQuery();

            while (rs0.next()) {
                colType = rs0.getString("DATA_TYPE");

                if (colType == null || colType.length() == 0) {
                    System.out.print("========== Error: can't identify datatype for " + table.toUpperCase() + "."+blobField.toUpperCase()+" ! Object\\attribute not exist for this connection ?");
                    System.exit(-1);
                }
            }

            FileInputStream fis = new FileInputStream(file);
            PreparedStatement pstmt = conn.prepareStatement("select  " + blobField.toUpperCase() + "  from " + table.toUpperCase() + " where " + keyField + "=? and rownum < 2");
            pstmt.setString(1, keyValue);
            ResultSet rs = pstmt.executeQuery();
            long filebytesize = 0;
            int bufSize = 1024;
            if (rs.next()) {
                if (colType.equals("BLOB")) {
                    Blob blob = rs.getBlob(blobField);
                    filebytesize = blob.length();
                    if   (filebytesize == 0L ){
                        System.out.println(" ================================");
                        System.out.println(" Null file length detected !!!");
                        System.out.println(" ================================");
                    }
                    System.out.println(" the ETA File size byte = " + filebytesize);
                    InputStream blobInputStream = null;
                    blobInputStream = blob.getBinaryStream();
                    FileOutputStream out = new FileOutputStream(file);
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
                    Clob clob = rs.getClob(blobField);
                    filebytesize = clob.length();
                    System.out.println(" the ETA File size byte = " + filebytesize);
                    InputStream clobInputStream = null;
                    clobInputStream = clob.getAsciiStream();
                    FileOutputStream out = new FileOutputStream(file);
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

    public void write2File(Connection conn, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(file);
            PreparedStatement pstmt = conn.prepareStatement("select  file_data_blob  from FILE_LOAD where file_name=? and rownum < 2");
            pstmt.setString(1, file.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob("file_data_blob");
                InputStream in = blob.getBinaryStream();
                OutputStream out = new FileOutputStream(file);
                byte[] buff = blob.getBytes(1, (int) blob.length());
                out.write(buff);
                out.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}