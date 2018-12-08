package ch.epfl.sweng.favors.favors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.notifications.NotificationsFragment;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

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

    @After
    public void tearDown(){
        FakeDatabase.getInstance().removeExtraFromDB();
    }
}
