package ch.epfl.sweng.favors.notifications;

import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.notifications.NotificationsFragment;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class NotificationListTest {

    @Rule
    public FragmentTestRule<NotificationsFragment> mFragmentTestRule = new FragmentTestRule<NotificationsFragment>(NotificationsFragment.class);


    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void startFragment() throws InterruptedException {
        mFragmentTestRule.launchActivity(null);
        Thread.sleep(2000);
    }

    @Test
    public void displayNotification() throws InterruptedException {
        mFragmentTestRule.launchActivity(null);
        Thread.sleep(2000);
        mFragmentTestRule.getActivity().runOnUiThread(() -> {
                    mFragmentTestRule.getFragment().notificationsList.add("Super Notification");
                    mFragmentTestRule.getFragment().listAdapter.notifyDataSetChanged();
                });

        Thread.sleep(2000);

        onView(ViewMatchers.withText("Super Notification")).check(matches(isDisplayed()));
    }

    @After
    public void tearDown(){
        FakeDatabase.getInstance().removeExtraFromDB();
    }
}
