package ch.epfl.sweng.favors.notifications;

import android.support.test.espresso.matcher.ViewMatchers;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.notifications.NotificationsFragment;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.mockito.Mockito.when;

public class NotificationListTest {
    private static final String MOCKED_NOTIF = "mocked notification";

    @Rule
    public FragmentTestRule<NotificationsFragment> mFragmentTestRule = new FragmentTestRule<NotificationsFragment>(NotificationsFragment.class);

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock private QuerySnapshot querySnapshot;
    @Mock private DocumentChange documentChange;
    @Mock private QueryDocumentSnapshot queryDocumentSnapshots;
    private Map<String,Object> mockedNotifications;

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();

        mockedNotifications = new HashMap<>();
        mockedNotifications.put("message", MOCKED_NOTIF);

        List<DocumentChange> list = new ArrayList<>();
        list.add(documentChange);

        when(querySnapshot.getDocumentChanges()).thenReturn(list);
        when(documentChange.getDocument()).thenReturn(queryDocumentSnapshots);
        when(queryDocumentSnapshots.getData()).thenReturn(mockedNotifications);
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

    @Test
    public void updateListTest() throws InterruptedException {
        mFragmentTestRule.launchActivity(null);
        Thread.sleep(2000);

        mFragmentTestRule.getActivity().runOnUiThread(() -> {
            mFragmentTestRule.getFragment().updateList(querySnapshot);
        });

        onView(ViewMatchers.withText(MOCKED_NOTIF)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown(){
        FakeDatabase.getInstance().removeExtraFromDB();
    }
}
