package com.example.driverawarenessdetection;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.driverawarenessdetection.login.data.LoginDataSource;
import com.example.driverawarenessdetection.login.data.LoginRepository;
import com.example.driverawarenessdetection.login.data.LoggedInUser;

public class Utils {

    public static void enterCredentials(String username, String password) {
        // Simulate user entering credentials
        onView(withId(R.id.username)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
    }

    public static void enterCredentialsAndConfirm(String username, String password) {
        enterCredentials(username, password);
        onView(withText("Confirm")).perform(click());
    }

    public static boolean isUserLoggedIn(String username) {
        LoginRepository loginRepository = LoginRepository.getInstance(null);
        LoggedInUser user = loginRepository.getLoggedInUser();
        return loginRepository.isLoggedIn() && user.getDisplayName().equals(username);
    }

    public static void logoutUser() {
        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
        loginRepository.logout();
    }
}
