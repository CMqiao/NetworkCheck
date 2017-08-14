package com.yqb.networkcheck.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QJZ on 2017/8/9.
 */

public class FileUtil {

    public static void copyFile(Context context, String appPath, String fileName) {
        try {
            context.openFileInput(fileName);
        } catch (FileNotFoundException notfoundE) {
            try {
                copyFromAssets(context, fileName, fileName);
                String script = "chmod 700 " + appPath + "/" + fileName;
                exe(script);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void copyFromAssets(Context context, String source, String destination) throws IOException {
        InputStream is = context.getAssets().open(source);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        FileOutputStream output = context.openFileOutput(destination, Context.MODE_PRIVATE);
        output.write(buffer);
        output.close();
    }

    public static List<String> exe(String cmd) {
        Process process = null;
        List<String> list = new ArrayList<String>();
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmd);
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}
