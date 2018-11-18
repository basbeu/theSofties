package ch.epfl.sweng.favors.database;

import android.databinding.ObservableList;
import android.os.Looper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.favors.utils.ExecutionMode;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class RequestTest {
    class AnyRequest extends Request {}
    @Test
    public void RequestIsAbstract() {
        assertEquals(AnyRequest.db, Request.db);
    }

    @Before
    public void setup(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().cleanUp();
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void userRequests(){
        // Try to get all men users
        ObservableArrayList<User> someUsersList = new ObservableArrayList<>();
        UserRequest.getList(someUsersList, User.StringFields.sex, "M",  null, null);

        User user1 = new User();
        UserRequest.getWithEmail(user1, "harvey.dent@gotham.com");
        User user2 = new User();
        UserRequest.getWithId(user2, "U1");

        try {
            sleep(1000);
        } catch (InterruptedException e) {

        }
        assertEquals(user1.get(User.StringFields.firstName), "Harvey");
        assertEquals(user1.get(User.StringFields.lastName), "Dent");
        assertEquals(user1.get(User.StringFields.city), "Arkham Asylum");

        assertEquals(user2.get(User.StringFields.firstName), "Toto");
        assertEquals(user2.get(User.StringFields.lastName),"Lolo");
        assertEquals(user2.get(User.StringFields.city), "Tombouctou");

        assertEquals(someUsersList.size(), 3);
    }

    @Test
    public void favorsRequests(){
        ObservableArrayList<Favor> someFavorsList =  new ObservableArrayList<>();
        FavorRequest.getList(someFavorsList, Favor.StringFields.ownerID, "U3",  null, null);
        ObservableArrayList<Favor> allFavorsList =  new ObservableArrayList<>();
        FavorRequest.all(allFavorsList, null, null);
        try {
            sleep(1000);
        } catch (InterruptedException e) {

        }
        assertEquals(someFavorsList.size(), 9);
        assertEquals(allFavorsList.size(), 10);
    }

    @Test
    public void interestsRequests(){
        ObservableArrayList<Interest> someInterestsList =  new ObservableArrayList<>();
        InterestRequest.getList(someInterestsList, Interest.StringFields.title, "SWISS AVIATION",  null, null);
        ObservableArrayList<Interest> allInterestsList =  new ObservableArrayList<>();
        InterestRequest.all(allInterestsList,  null, null);
        try {
            sleep(1000);
        } catch (InterruptedException e) {

        }
        assertEquals(someInterestsList.size(), 1);
        assertEquals(allInterestsList.size(), 5);
    }
}