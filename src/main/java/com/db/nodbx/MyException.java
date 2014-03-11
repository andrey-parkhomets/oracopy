package com.db.nodbx;


public class MyException extends Exception {

    public String reason;
    public Integer ex_code;
    public MyException(Throwable cause) {
        super(cause);
    }
}
