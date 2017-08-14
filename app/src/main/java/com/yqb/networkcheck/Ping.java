package com.yqb.networkcheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by QJZ on 2017/8/14.
 */

public class Ping {


    public static String ping(String address) {
        String result = "";
        String line = null;
        try {
            Process process = Runtime.getRuntime().exec("ping -c 3 -w 5 " + address);
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = buf.readLine()) != null)
                result += line + "\n";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
