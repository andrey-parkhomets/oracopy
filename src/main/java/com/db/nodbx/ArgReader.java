package com.db.nodbx;


import java.util.*;

//import java.math.*;

public class ArgReader {
    public static final String minus_txt = "-";
    public static final String help_txt = "-h";
    public static final String[] h_strs = new String[]{"-h", "--h", "--help", "-help"};

    public Map<String, String> spaceParser(String[] argv) {
        Map<String, String> map = new HashMap<String, String>();
        // String[] entry;
        int i = 0;
        String myParam = null;
        String myValue = null;

        if //(Arrays.asList(argv).containsAll(Arrays.asList(h_strs)))
                ( isHelpExist(Arrays.asList(argv),Arrays.asList(h_strs)))
        //if (Arrays.asList(argv).contains(help_txt))
        {
            info.printUsage("Usage info:");
        }
        for (
                String s : argv) {
            if ((i % 2) == 0) {
                if (minus_txt.equals(argv[i].substring(0, 1))) {
                    myParam = argv[i].toLowerCase();
                } else {
                    info.printUsage("Param " + argv[i].toString() + "is wrong");
                    //System.exit(1);
                }
            } else {
                myValue = argv[i].toString();
            }
            if (myValue != null) {
                if ((myValue.length() == 0) && ((i % 2) != 0)) {
                    info.printUsage("parameter " + myParam + " value must be set");
                } else {
                    map.put(myParam, myValue);
                    myValue = null;
                }
            }
            i += 1;
        }

        return map;

    }



    public boolean isHelpExist(Collection<String> helpList, Collection<String> argList) {
        Map<String, Integer> map_a1 = new HashMap<String, Integer>();

        for (String i : helpList) {
            map_a1.put(i, 1);
        }
        for (String j : argList) {
            if (map_a1.containsKey(j)) {
                return true;
            }
        }
        return false;
    }


}
