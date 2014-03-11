package com.db.nodbx;


import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;

import java.io.*;
import java.sql.*;

import static java.lang.Math.round;

public class Inserter {
    private DatabaseMetaData  dbMetaData              = null;
    public void doInsert(Connection conn, File file)

            throws IOException, SQLException {
        //private DatabaseMetaData  dbMetaData              = null;
        FileInputStream     inputFileInputStream    = null;
        String              sqlText                 = null;
        Statement           stmt                    = null;
        ResultSet           rset                    = null;
        BLOB                blob                   = null;
        int                 chunkSize;
        byte[]              binaryBuffer;
        long                position;
        int                 bytesRead               = 0;
        int                 bytesWritten            = 0;
        int                 totbytesRead            = 0;
        int                 totbytesWritten         = 0;

        try {

            stmt = conn.createStatement();


            inputFileInputStream = new FileInputStream(file);

            sqlText =
                    "INSERT INTO file_load ( file_name, file_data_blob, load_date, load_by ) " +
                            "   VALUES( '" + file.getName() + "', EMPTY_BLOB() , sysdate , sys_context('USERENV', 'OS_USER')||'@'||sys_context('USERENV', 'HOST') )";
            stmt.executeUpdate(sqlText);

            sqlText =
                    "SELECT file_data_blob " +
                            "FROM   file_load " +
                            "WHERE  file_name =  '" +  file.getName() + "'"+
                            "FOR UPDATE";
            rset = stmt.executeQuery(sqlText);
            rset.next();
            blob = ((OracleResultSet) rset).getBLOB("file_data_blob");
            chunkSize = blob.getChunkSize();
            binaryBuffer = new byte[chunkSize];

            position = 1;
            while ((bytesRead = inputFileInputStream.read(binaryBuffer)) != -1) {

                //if ( dbMetaData.getJDBCMajorVersion() < 10 ) {
               //     bytesWritten = image.putBytes(position, binaryBuffer, bytesRead);
               // } else {
                    bytesWritten = blob.setBytes(position, binaryBuffer, 0, bytesRead);
               // }

                position        += bytesRead;
                totbytesRead    += bytesRead;
                totbytesWritten += bytesWritten;
            }

            inputFileInputStream.close();

            conn.commit();
            rset.close();
            stmt.close();

            System.out.println(
                    "==========================================================\n" +
                            "  SET METHOD\n" +
                            "==========================================================\n" +
                            "Wrote file " + file.getName() + " to BLOB column.\n" +
                            totbytesRead + " bytes read.\n" +
                            totbytesWritten + " bytes written.\n"
            );

        } catch (IOException e) {
            System.out.println("Caught I/O Exception: (Write BLOB value - Set Method).");
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: (Write BLOB value - Set Method).");
            System.out.println("SQL:\n" + sqlText);
            e.printStackTrace();
            throw e;
        }

    }



 //jdbc6 for java 1.6
    public void doInsert_jdbc6(Connection conn, File file, String table, String clobFieldName, String keyFieldName, String keyFieldValue) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("insert into "+table.toUpperCase()+"( file_name, "+clobFieldName.toUpperCase()+", file_tag) values ( ? ,? , ?)");

            pstmt.setString(1, file.getName());
            pstmt.setString(3, keyFieldValue);

            //BLOB myblob = null;
          ////!  BLOB myblob = (BLOB) conn.createBlob();
            BLOB myblob = BLOB.createTemporary(conn, false, BLOB.DURATION_SESSION);
            int bufSize = myblob.getBufferSize();
            OutputStream blobOutputStream = null;
            InputStream buffIn = null;
            FileInputStream     inputFileInputStream    = null;

            try {
                blobOutputStream = myblob.setBinaryStream(1);
                inputFileInputStream    = new FileInputStream(file);
                buffIn = new BufferedInputStream( inputFileInputStream, bufSize);
                byte[] buffer = new byte[bufSize];
                long filebytesize = 0;
                filebytesize =  file.length();
                System.out.println(" the filebytesize ="+ filebytesize);
                int length = -1;
                //int cnt  = 0;
                long bytedone = 0;
                int pecentdone;
                while ((length = buffIn.read(buffer)) != -1) {
                    blobOutputStream.write(buffer, 0, length);
                    //cnt=cnt+1;
                    bytedone    = bytedone+length;
                    pecentdone = (int)(((double)bytedone / (double) filebytesize) * 100d);
                    printProgBar.printProgBar(pecentdone);
                }
                blobOutputStream.flush();
            } finally {
                if (buffIn != null) {
                    try {
                        buffIn.close();
                    } catch (Exception e) { }
                }
                if (blobOutputStream != null) {
                    try {
                        blobOutputStream.close();
                    } catch (Exception e) { }
                }
            }

            pstmt.setBlob(2, myblob);
            pstmt.executeUpdate();
            System.out.println("insert into file_load of file_name="
                    + file.getName() + ", is ok");
            inputFileInputStream.close();
            blobOutputStream.close();
            conn.commit();
            buffIn.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
