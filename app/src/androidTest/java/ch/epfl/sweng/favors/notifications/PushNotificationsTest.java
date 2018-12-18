package ch.epfl.sweng.favors.notifications;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.messaging.RemoteMessage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.authentication.ConfirmationSent;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static org.mockito.Mockito.when;


public class PushNotificationsTest {

    @Rule
    public FragmentTestRule<NotificationsFragment> mFragmentTestRule = new FragmentTestRule<NotificationsFragment>(NotificationsFragment.class);

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();

    }

    PushNotifications pushNotClass = new PushNotifications();
    RemoteMessage.Builder rmb = new RemoteMessage.Builder("mockDestination");

    @Test
    public void onNewTokenCall(){
        pushNotClass.onNewToken("newToken");
    }
    @Test
    public void sendNotification() throws InterruptedException {
        mFragmentTestRule.launchActivity(null);
        Thread.sleep(2000);
        pushNotClass.sendNotification("Fake notif", "fake notif", mFragmentTestRule.getFragment().getContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void sendNotificationNullContext(){
        pushNotClass.sendNotification("blalbla","blabla", null);
    }


}
