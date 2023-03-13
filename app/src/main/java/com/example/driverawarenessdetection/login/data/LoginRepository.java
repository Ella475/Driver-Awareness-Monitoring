package com.example.driverawarenessdetection.login.data;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.driverawarenessdetection.MainActivity;
import com.example.driverawarenessdetection.R;
import com.example.driverawarenessdetection.SettingsActivity;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;
import com.example.driverawarenessdetection.settings.SettingsFragment;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private final LoginDataSource dataSource;
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public Result login(String username, String password) {
        Result result = dataSource.handleUser(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}