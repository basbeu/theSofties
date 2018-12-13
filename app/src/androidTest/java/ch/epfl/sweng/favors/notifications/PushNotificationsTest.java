package ch.epfl.sweng.favors.notifications;

import android.content.Context;
import android.support.test.uiautomator.UiDevice;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PushNotificationsTest {

    private UiDevice device;

    @Mock
    Context context = mock(Context.class);

    @Before
    public void Before(){
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
    public void onMessageReceivedCall() {
//        User u = new User("U1");
//        u.set(User.StringFields.token_id, "1");
//        Database.getInstance().updateOnDb(u);
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        NotificationCompat.Builder ncb = mock(NotificationCompat.Builder.class);
//        //PowerMock.whenNew(NotificationCompat.Builder.class)
//        rmb.addData("title", "testTitle").addData("body", "testBody");
//        pushNotClass.onMessageReceived(rmb.build());
//    }
    }

}
