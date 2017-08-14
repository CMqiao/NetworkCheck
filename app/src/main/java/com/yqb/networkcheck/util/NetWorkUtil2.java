package com.yqb.networkcheck.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by QJZ on 2017/8/8.
 */

public class NetWorkUtil2 {

    private static final String TAG = "NetworkDiagnosis";

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

    public static String pingIP(String address) {
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

    public static String installBusyBox(Context context, String appPath) {
        String result = "";
        FileUtil.copyFile(context, appPath, "busybox");
        String cmdBusyBox = "." + appPath + "/busybox";
        List<String> resultBusyBox = FileUtil.exe(cmdBusyBox);
        for (String line : resultBusyBox) {
            result += line + "\n";
        }
        return result;
    }

    public static String tracerouteIP(Context context, String address, String appPath) {
        String result = "";
        FileUtil.copyFile(context, appPath, "traceroute");
        String cmdTraceRoute = "." + appPath + "/traceroute " + address;
        List<String> resultTraceRoute = FileUtil.exe(cmdTraceRoute);
        for (String line : resultTraceRoute) {
            result += line + "\n";
        }
        Log.d("traceroute", result);
        return result;
    }

    public static String tcpIP(String address, int port, int times) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < times; i++) {
            sb.append("第" + (i + 1) + "次" + tcpIP(address, port) + "\n");
        }
        return sb.toString();
    }

    public static String tcpIP(String address, int port) {
        StringBuffer result = new StringBuffer();
        long startTime = System.currentTimeMillis();
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), 3000);
            long endTime = System.currentTimeMillis();
            result.append("连接成功： time=" + (endTime - startTime) + "ms");
        } catch (ConnectException e) {
            long endTime = System.currentTimeMillis();
            result.append("连接失败: time=" + (endTime - startTime) + "ms");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public static String sslConnectIP(String address, int port) {
        final String[] result = new String[1];
        result[0] = "";
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = null;
        try {
            sslSocket = (SSLSocket) sslSocketFactory.createSocket();
            sslSocket.connect(new InetSocketAddress(address, port), 3000);
            sslSocket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
                @Override
                public void handshakeCompleted(HandshakeCompletedEvent arg0) {
                    try {
                        result[0] += Arrays.toString(arg0.getPeerCertificates());
                        result[0] += arg0.getCipherSuite();
                        result[0] += Arrays.toString(arg0.getPeerCertificateChain());

                    } catch (SSLPeerUnverifiedException e) {
                        e.printStackTrace();
                    }
                }
            });

            sslSocket.startHandshake();

        } catch (IOException e) {
            try {
                sslSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        return result[0];
    }

}
