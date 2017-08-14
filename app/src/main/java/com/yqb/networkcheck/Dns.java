package com.yqb.networkcheck;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by QJZ on 2017/8/14.
 */

public class Dns {

    private static final String TAG = "DNS";

    /**
     *  根据域名解析出IP地址集合
     * @param address 域名
     * @return 域名解析出的IP地址集合
     * @throws UnknownHostException
     */
    public static ArrayList<String> parseDNS(String address) throws UnknownHostException{
        Log.d(TAG, "开始进行DNS解析");

        ArrayList<String> result = new ArrayList<>();
        try {
            InetAddress[] ipAddress = InetAddress.getAllByName(address);
            for(InetAddress inetAddress : ipAddress){
                result.add(inetAddress.getHostAddress());
            }

            Log.d(TAG, "DNS解析成功");
            Log.d(TAG, "域名："+address+"解析出的IP地址为：");

        } catch (UnknownHostException e) {

            Log.d(TAG, "未知域名，域名解析失败");

            throw e;
        }
        return result;
    }

}
