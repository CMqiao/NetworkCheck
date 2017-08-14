package com.yqb.networkcheck.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by QJZ on 2017/8/7.
 */

public class AddressCheck {

    public static boolean isIP(String address) {
        if (address.length() < 7 || address.length() > 15 || "".equals(address)) {
            return false;
        }
        String rexpString = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(rexpString);
        Matcher matcher = pattern.matcher(address);
        boolean isIP = matcher.find();
        return isIP;
    }

    public static boolean isDomain(String address) {
        if (address.length() < 7 || address.length() > 15 || "".equals(address)) {
            return false;
        }
        String rexpString = "^(([0-9a-z]+-[0-9a-z]+)+\\.|([0-9a-z]+)\\.)+[a-z]{2,8}$";
        Pattern pattern = Pattern.compile(rexpString);
        Matcher matcher = pattern.matcher(address);
        boolean isDomain = matcher.find();
        return isDomain;
    }

}
