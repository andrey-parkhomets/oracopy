package com.home.oracopy;


public class info {

    public static void printUsage(String msg) {
        if (msg != null) {
            System.out.println(msg);
        }

        System.out.print("Usage: ");
        System.out.println("java [-Doracle.net.tns_admin=$TNS_ADMIN] \\");
        System.out.println("      -Doracle.net.wallet_location=<secure_path_to_wallet> \\");   //filename=e:\andrey.sql  tns_alias=LNOPMIU1_CB04_OWNER
        System.out.println(" -f <path_to_file> \\");
        System.out.println(" -tns <tns_alias> \\");
        System.out.println(" -table <TABLE_NAME> \\");
        System.out.println(" -key_val <FILE_TAG> \\");
        System.out.println(" -a <upload|create_upload_txt|download> \\");
        System.out.println();
        System.out.println("  -h\t\tPrints the usage message");
        System.out.println("  -f \t\t File used as source\\destination of blob");
        System.out.println("  -tns \t\t The tns alias of $TNS_ADMIN/tnsnames.ora registred in Oracle Wallet");
        System.exit(-1);
    }
}
