package ch.epfl.sweng.favors.database;

import org.junit.Before;
import org.junit.Test;

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
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void userRequests(){
        // Try to get all men users
        ObservableArrayList<User> someUsersList = new ObservableArrayList<>();
        UserRequest.getList(someUsersList, User.StringFields.sex, "M",  null, null);

        User user1 = new User();
        UserRequest.getWithEmail(user1, "jean.marchand@thesoftie.com");
        User user2 = new User();
        UserRequest.getWithId(user2, "U2");

        try {
            sleep(1000);
        } catch (InterruptedException e) {

        }
        assertEquals(user1.get(User.StringFields.firstName), "Jean");
        assertEquals(user1.get(User.StringFields.lastName), "Marchand");
        assertEquals(user1.get(User.StringFields.city), "Sydney, AU");

        assertEquals(user2.get(User.StringFields.firstName), "Jeanne");
        assertEquals(user2.get(User.StringFields.lastName),"Trousse");
        assertEquals(user2.get(User.StringFields.city), "New York, US");

        assertEquals(2, someUsersList.size());
    }

    @Test
    public void favorsRequests(){
        ObservableArrayList<Favor> someFavorsList =  new ObservableArrayList<>();
        FavorRequest.getList(someFavorsList, Favor.StringFields.ownerID, "U1",  null, null);
        ObservableArrayList<Favor> allFavorsList =  new ObservableArrayList<>();
        FavorRequest.all(allFavorsList, null, null);
        try {
            sleep(1000);
        } catch (InterruptedException e) {

        }
        assertEquals(4, someFavorsList.size());
        assertEquals(10, allFavorsList.size());
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
        assertEquals(0, someInterestsList.size());
        assertEquals(5, allInterestsList.size());
    }
}