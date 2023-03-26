package com.example.driverawarenessdetection;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.os.SystemClock;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.driverawarenessdetection.login.data.LoginDataSource;
import com.example.driverawarenessdetection.login.data.LoginRepository;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;
import com.example.driverawarenessdetection.login.ui.LoginType;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Tests for the following user stories:
 * As a user (driver) I want to be able to register to the app, so that I can use the app.
 * As a user (driver) I want to be able to sign into the app
 * so that my personalized data can be saved between different usage of the app.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest {
    private static final String random_number = String.valueOf((int) (Math.random() * 1000000));
    private static final String USERNAME = "test_user" + random_number;
    private static final String PASSWORD = "test_password";


    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        LoginType.setLoginType("user");
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void test1RegisterUser() {
        Utils.enterCredentialsAndConfirm(USERNAME, PASSWORD);

        SystemClock.sleep(2000);

        // Check if user is registered
        assertTrue(Utils.isUserLoggedIn(USERNAME));
        Utils.logoutUser();
    }

    @Test
    public void test2LoginUser() {
        Utils.enterCredentials(USERNAME, PASSWORD);

        SystemClock.sleep(2000);

        // Check if user is logged in
        assertTrue(Utils.isUserLoggedIn(USERNAME));
        Utils.logoutUser();
    }

    @Test
    public void test3RegisterUsedUsernameOrWrongPassword() {
        Utils.enterCredentialsAndConfirm(USERNAME, PASSWORD);

        SystemClock.sleep(2000);

        // Check if user is registered
        assertFalse(Utils.isUserLoggedIn(USERNAME));
        Utils.logoutUser();
    }
}