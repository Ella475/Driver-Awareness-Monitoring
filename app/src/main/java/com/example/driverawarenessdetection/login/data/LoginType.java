package com.example.driverawarenessdetection.login.data;

public class LoginType {
    private static String LOGIN_TYPE = "user";
    private static String LOGIN_ACTION = "login";

    private LoginType() {
    }

    public static String getLoginType() {
        return LOGIN_TYPE;
    }

    public static void setLoginType(String loginType) {
        LOGIN_TYPE = loginType;
    }

    public static String getLoginAction() {
        return LOGIN_ACTION;
    }

    public static void setLoginAction(String loginAction) {
        LOGIN_ACTION = loginAction;
    }
}
