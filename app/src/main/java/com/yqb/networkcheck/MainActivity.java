package com.yqb.networkcheck;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yqb.networkcheck.util.NetWorkUtil;
import com.yqb.networkcheck.util.ToastUtil;
import com.yqb.networkcheck.vo.AddressCheck;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputAddressEditText;
    private Button checkButton;
    private ScrollView scrollView;
    private TextView resultTextView;
    private String address;
    private View rootView;
    private Handler handler;

    private String appPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        init();
    }

    private void findViews() {
        rootView = (View) findViewById(R.id.rl_root_main);
        inputAddressEditText = (EditText) findViewById(R.id.et_input_address);
        checkButton = (Button) findViewById(R.id.btn_ok);
        scrollView = (ScrollView) findViewById(R.id.sv_scroll_check_result);
        resultTextView = (TextView) findViewById(R.id.tv_check_result);
    }

    private void init() {

        appPath = getApplicationContext().getFilesDir().getAbsolutePath();

        checkButton.setOnClickListener(this);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.arg1) {
                    case 1:
                        Bundle data = msg.getData();
                        resultTextView.append(Html.fromHtml("<font color='#FF0000'>解析出的ip为：</font>"));
                        resultTextView.append(data.getString("ip"));
                        pingIP(data.getString("ip"));
                        break;
                    case 2:
                        Bundle data1 = msg.getData();
                        resultTextView.append(Html.fromHtml("<font color='#FF0000'>ping的结果：</font></br>"));
                        resultTextView.append("\n" + data1.getString("result"));
                        installBusyBox(data1.getString("ip"));
                        break;
                    case 3:
                        Bundle data2 = msg.getData();
                        resultTextView.append(Html.fromHtml("<font color='#FF0000'>安装结果：</font></br>"));
                        resultTextView.append("\n" + data2.getString("result"));
                        tracerouteIP(data2.getString("ip"));
                        break;
                    case 4:
                        Bundle data3 = msg.getData();
                        resultTextView.append(Html.fromHtml("<font color='#FF0000'>traceroute的结果：</font></br>"));
                        resultTextView.append("\n" + data3.getString("result"));
                        tcpIP(data3.getString("ip"), 80);
                        break;
                    case 5:
                        Bundle data4 = msg.getData();
                        resultTextView.append(Html.fromHtml("<font color='#FF0000'>TCP连接结果：</font></br>"));
                        resultTextView.append("\n" + data4.getString("result"));
                        sslIP(data4.getString("ip"), 443);
                        break;
                    case 6:
                        Bundle data5 = msg.getData();
                        resultTextView.append(Html.fromHtml("<font color='#FF0000'>SSL连接结果：</font></br>"));
                        resultTextView.append("\n" + data5.getString("result"));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        address = inputAddressEditText.getText().toString().trim();

        if (address.equals("")) {
            //地址为空
            ToastUtil.toast(this, getResources().getString(R.string.remind_address_no_null));
        } else if (AddressCheck.isIP(address)) {
            //地址为IP
            pingIP(address);
        } /*else if (AddressCheck.isDomain(address)) {
            //地址为域名
            checkDomain(address);
        } */else {
            checkDomain(address);
            //ToastUtil.toast(this, getResources().getString(R.string.remind_input_right_address));
        }
    }

    private void checkDomain(final String address) {
        resultTextView.setText(Html.fromHtml("<font color='#0000FF'>正在解析域名: </font>"));
        resultTextView.append(address + " ........\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = NetWorkUtil.parseDNS(address);
                Message message = new Message();
                message.arg1 = 1;
                Bundle data = new Bundle();
                data.putString("ip", ip);
                data.putString("domain", address);
                message.setData(data);
                handler.sendMessage(message);
            }
        }).start();
    }

    private void pingIP(final String ipAddress) {
        resultTextView.append("\n\n");
        resultTextView.append(Html.fromHtml("<font color='#0000FF'>正在ping: </font>"));
        resultTextView.append(ipAddress + "..........\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetWorkUtil.pingIP(ipAddress);
                Message message = new Message();
                message.arg1 = 2;
                Bundle data = new Bundle();
                data.putString("result", result);
                data.putString("ip", ipAddress);
                message.setData(data);
                handler.sendMessage(message);
            }
        }).start();
    }

    private void installBusyBox(final String address) {
        resultTextView.append("\n");
        resultTextView.append(Html.fromHtml("<font color='#0000FF'>正在安装BusyBox..........</font>"));
        resultTextView.append("\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetWorkUtil.installBusyBox(MainActivity.this, appPath);
                Message message = new Message();
                message.arg1 = 3;
                Bundle data = new Bundle();
                data.putString("result", result);
                data.putString("ip", address);
                message.setData(data);
                handler.sendMessage(message);
            }
        }).start();
    }

    private void tracerouteIP(final String address) {
        resultTextView.append("\n");
        resultTextView.append(Html.fromHtml("<font color='#0000FF'>正在traceroute: </font>"));
        resultTextView.append(address + "..........\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetWorkUtil.tracerouteIP(MainActivity.this, address, appPath);
                Message message = new Message();
                message.arg1 = 4;
                Bundle data = new Bundle();
                data.putString("result", result);
                data.putString("ip", address);
                message.setData(data);
                handler.sendMessage(message);
            }
        }).start();
    }

    private void tcpIP(final String address, final int port){
        resultTextView.append("\n");
        resultTextView.append(Html.fromHtml("<font color='#0000FF'>正在进行TCP连接: </font>"));
        resultTextView.append(address + "..........\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetWorkUtil.tcpIP(address, port, 4);
                Message message = new Message();
                message.arg1 = 5;
                Bundle data = new Bundle();
                data.putString("result", result);
                data.putString("ip", address);
                message.setData(data);
                handler.sendMessage(message);
            }
        }).start();
    }

    private void sslIP(final String address, final int port){
        resultTextView.append("\n");
        resultTextView.append(Html.fromHtml("<font color='#0000FF'>正在进行SSL连接: </font>"));
        resultTextView.append(address + "..........\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetWorkUtil.sslConnectIP(address, port);
                Message message = new Message();
                message.arg1 = 6;
                Bundle data = new Bundle();
                data.putString("result", result);
                data.putString("ip", address);
                message.setData(data);
                handler.sendMessage(message);
            }
        }).start();
    }

}
