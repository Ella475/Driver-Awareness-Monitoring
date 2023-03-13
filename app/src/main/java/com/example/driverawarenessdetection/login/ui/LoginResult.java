package com.example.driverawarenessdetection.login.ui;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private String success;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        assert success != null;
        this.success = success.getDisplayName();
    }

    @Nullable
    public String getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }
}