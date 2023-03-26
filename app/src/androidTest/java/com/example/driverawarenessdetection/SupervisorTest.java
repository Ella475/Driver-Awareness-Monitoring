package com.example.driverawarenessdetection;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;


import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.driverawarenessdetection.client.SupervisorSenderReceiver;
import com.example.driverawarenessdetection.login.data.LoginRepository;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;
import com.example.driverawarenessdetection.login.ui.LoginType;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the following user stories:
 * As a user (supervisor) I would like to enter a different part of the application, which is different from the user part,
 * so that I could get just the functionality I need from the app.
 * (meaning different flow of the app, like: different login/ registration, different main page..)
 * As a user (supervisor) I would like to be able to aad a new driver to supervise, so that I could monitor his/her driving.
 * As a user (supervisor) I would like to be able to choose a specific driver I am supervising to observe his statistics,
 * so that I could monitor his/her  different driving.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupervisorTest {
    private static final String random_number = String.valueOf((int) (Math.random() * 1000000));
    private static final String USERNAME = "test_user" + random_number;
    private static final String PASSWORD = "test_password";
    private static final String random_user = "test" + (int) (Math.random() * 1000000);


    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        LoginType.setLoginType("supervisor");
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() {
        mActivityRule.finishActivity();
    }

    @Test
    public void test0RegisterUser() {
        LoginType.setLoginType("user");
        mActivityRule.launchActivity(new Intent());
        Utils.enterCredentialsAndConfirm(random_user, PASSWORD);
        SystemClock.sleep(2000);
        assertTrue(Utils.isUserLoggedIn(random_user));
        Utils.logoutUser();
    }

    @Test
    public void test1RegisterSupervisor() {
        Utils.enterCredentialsAndConfirm(USERNAME, PASSWORD);

        SystemClock.sleep(2000);

        // Check if user is registered
        assertTrue(Utils.isUserLoggedIn(USERNAME));
        Utils.logoutUser();
    }

    @Test
    public void test2RegisterUsedUsernameOrWrongPassword() {
        String random_password = (int) (Math.random() * 1000000) + "a";
        Utils.enterCredentials(USERNAME, random_password);
        // Check if user is logged in
        assertFalse(Utils.isUserLoggedIn(USERNAME));
        Utils.logoutUser();
    }

    @Test
    public void test3LoginSupervisor() {
        Utils.enterCredentials(USERNAME, PASSWORD);

        // Check if user is logged in
        assertTrue(Utils.isUserLoggedIn(USERNAME));
        Utils.logoutUser();
    }

    @Test
    public void test4AddNewDriverToSupervisor() {
        Utils.enterCredentials(USERNAME, PASSWORD);
        SystemClock.sleep(2000);

        // new_user_edit_text
        onView(withId(R.id.new_user_edit_text)).perform(typeText(random_user), closeSoftKeyboard());
        SystemClock.sleep(1000);

        // Click add new driver button
        onView(withId(R.id.add_new_user_button)).perform(click());

        SystemClock.sleep(2000);

        onView(withId(R.id.user_spinner)).perform(click());
        onView(withText(random_user)).check(matches(isDisplayed()));
    }

    @Test
    public void test5AddNotExistDriverToSupervisor() {
        Utils.enterCredentials(USERNAME, PASSWORD);
        SystemClock.sleep(2000);

        // new_user_edit_text
        onView(withId(R.id.new_user_edit_text)).perform(typeText("not_exist_user"), closeSoftKeyboard());
        SystemClock.sleep(1000);

        // Click add new driver button
        onView(withId(R.id.add_new_user_button)).perform(click());

        onView(withId(R.id.user_spinner)).perform(click());
        onView(withText("not_exist_user")).check(doesNotExist());
    }


    @Test
    public void test6ChooseDriverToViewData() {
        Utils.enterCredentials(USERNAME, PASSWORD);
        onView(withId(R.id.new_user_edit_text)).perform(typeText(random_user), closeSoftKeyboard());
        SystemClock.sleep(1000);

        // choose "ella" from the user_spinner dropdown
        onView(withId(R.id.user_spinner)).perform(click());

        onData(allOf(is(instanceOf(String.class)), is(random_user))).perform(click());
        onView(withId(R.id.confirm_button)).perform(click());
        // make sure that the statistics are displayed
        onView(withId(R.id.line_chart_graph)).check(matches(isDisplayed()));
    }
}