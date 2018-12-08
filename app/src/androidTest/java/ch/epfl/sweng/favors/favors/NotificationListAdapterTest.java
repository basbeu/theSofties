package ch.epfl.sweng.favors.favors;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.favors.notifications.NotificationListAdapter;

import static org.junit.Assert.assertEquals;

public class NotificationListAdapterTest {

    public NotificationListAdapter adapter;

    @Before
    public void Before(){
        ArrayList<String> notifications = new ArrayList<>();
        notifications.add("This is the first notification");
        adapter = new NotificationListAdapter(notifications);
    }

    @Test
    public void returnsCorrectListSize() {
        assertEquals(1, adapter.getItemCount());
    }

}
