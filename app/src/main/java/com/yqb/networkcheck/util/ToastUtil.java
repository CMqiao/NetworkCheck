package com.yqb.networkcheck.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by QJZ on 2017/8/8.
 */

public class ToastUtil {
    public static void toast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
