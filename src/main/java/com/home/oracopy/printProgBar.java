package com.home.oracopy;


public class printProgBar {

    public static void printProgBar(int percent){
        StringBuilder bar = new StringBuilder("[");

        for(int i = 0; i < 50; i++){
            if( i < (percent/2)){
                bar.append("=");
            }else if( i == (percent/2)){
                bar.append(">");
            }else{
                bar.append(".");
            }
        }

        bar.append("]   " + percent + "%     ");
        System.out.print("\r" + bar.toString());
    }

    public static void p1(int percent){
        StringBuilder bar = new StringBuilder("[");
        int vBAR_WIDTH=50;
        String vBAR_CHAR_START= new String("[");
        String vBAR_CHAR_END="]";
        String vBAR_CHAR_EMPTY=".";
        String vBAR_CHAR_FULL="=";
        int vBRACKET_CHARS=2 ;
        int vLIMIT=100 ;

        int vFull_limit = ((vBAR_WIDTH - vBRACKET_CHARS)*percent)/vLIMIT;
        int vEmpty_limit = (vBAR_WIDTH - vBRACKET_CHARS) -  vFull_limit;
        String v_bar_line = vBAR_CHAR_START;
        for(int j = 0; j < vFull_limit; j++){
            v_bar_line=v_bar_line+vBAR_CHAR_FULL;
        }

        for(int j = 0; j < vEmpty_limit; j++){
            v_bar_line=v_bar_line+vBAR_CHAR_EMPTY;
        }

        v_bar_line=v_bar_line+vBAR_CHAR_END+"    "+percent+"%";


        System.out.print("\r" + v_bar_line.toString());
    }

}
