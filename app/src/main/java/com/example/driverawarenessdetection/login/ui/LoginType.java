package com.example.driverawarenessdetection.login.ui;

public class LoginType {
    private static String LOGIN_TYPE = "user";

    private LoginType() {
    }

    public static String getLoginType() {
        return LOGIN_TYPE;
    }

    public static void setLoginType(String loginType) {
        LOGIN_TYPE = loginType;
    }
}
