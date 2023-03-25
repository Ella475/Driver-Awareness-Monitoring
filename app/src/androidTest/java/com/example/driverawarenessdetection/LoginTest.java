package com.example.driverawarenessdetection;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
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
    public void test1RegisterUser() throws InterruptedException {
        // Simulate user entering credentials
        onView(withId(R.id.username)).perform(typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(PASSWORD), closeSoftKeyboard());

        // Click login button
        onView(withId(R.id.login)).perform(click());

        // confirm registration dialog
        onView(withText("Confirm")).perform(click());

        SystemClock.sleep(1000);

        // Check if user is registered
        assertTrue(isUserRegistered());
    }

    private boolean isUserRegistered() {
        LoginRepository loginRepository = LoginRepository.getInstance(null);
        LoggedInUser user = loginRepository.getLoggedInUser();
        return user.getDisplayName().equals(USERNAME);
    }

    @Test
    public void test2LoginUser() {
        // Simulate user entering credentials
        onView(withId(R.id.username)).perform(typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(PASSWORD), closeSoftKeyboard());

        // Click login button
        onView(withId(R.id.login)).perform(click());

        SystemClock.sleep(1000);

        // Check if user is logged in
        assertTrue(isUserLoggedIn());
    }

    private boolean isUserLoggedIn() {
        LoginRepository loginRepository = LoginRepository.getInstance(null);
        LoggedInUser user = loginRepository.getLoggedInUser();
        return user.getDisplayName().equals(USERNAME);
    }
}