package com.home.oracopy;
import java.io.*;
public class info {


    public static void printUsage(String msg) {
        if (msg != null) {
            System.out.println(msg);
        }

       String progName  = Bridge.class.getProtectionDomain().getCodeSource().getLocation().getFile();
       File myJar = new File(progName);
        String myJarName =  myJar.getName();
        System.out.print("Usage: ");
        System.out.println("java    -Doracle.net.tns_admin=$TNS_ADMIN \\");
        System.out.println("        [ -Doracle.net.wallet_location=<secure_path_to_wallet> ]    \\");   //filename=e:\andrey.sql  tns_alias=LNOPMIU1_CB04_OWNER
        System.out.println("    -jar "+myJarName+" ");
        System.out.println(" -f <path_to_file> \\  ");
        System.out.println(" -tns <tns_alias> \\  ");
        System.out.println(" -table <TABLE_NAME> \\  ");
        System.out.println(" [ -key_val <FILE_TAG> ] \\ ");
        System.out.println(" [ -key_field <FILE_TAG_FIELD> ]");
        System.out.println(" -a <upload|create_upload_txt|download|ls|cleanup> \\ ");
        System.out.println(" [ -u <SCHEMA_NAME> ] ");
        System.out.println(" [ -p <SCHEMA_PWD> ] ");
        System.out.println(" [ -blob_field <BLOB_FILED> ]");
        System.out.println();
        System.out.println("  -h\t\tPrints the usage message");
        System.out.println("  -f \t\t File used as source\\destination of blob");
        System.out.println("  -tns \t\t The tns alias of $TNS_ADMIN/tnsnames.ora (can be associated with Oracle Wallet via sqlnet.ora for passwordless access");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println(" <path_to_file>  - the mandatory target/source file");
        System.out.println(" <TABLE_NAME>    - table used as LOB storage");
        System.out.println(" <tns_alias>     - connection string of $TNS_ADMIN/tnsnames.ora");
        System.out.println(" <FILE_TAG>      - the lookup value to segregate duplicated file names");
        System.out.println(" <SCHEMA_NAME>   - database schema for <TABLE_NAME>, used if no wallet configuration");
        System.out.println(" <SCHEMA_PWD>    - password of <SCHEMA_NAME>, used if no wallet configuration");
        System.exit(-1);
    }
}
