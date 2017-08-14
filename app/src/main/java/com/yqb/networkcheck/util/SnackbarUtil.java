package com.yqb.networkcheck.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by QJZ on 2017/8/7.
 */

public class SnackbarUtil {
    public static void makeText(View rootView,String message){
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}
