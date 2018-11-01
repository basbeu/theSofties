package ch.epfl.sweng.favors.database;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class ClassRequestTest {
    @Test
    public void getsAllFromList() {
        //favors
        assertEquals(FavorRequest.all(null, null),
                Request.db.getAll(Favor.class, "favors", null, null));
        //categories
        assertEquals(InterestRequest.all(null, null),
                Request.db.getAll(Interest.class, "interests", null, null));
    }
    @Test
    public void getsSpecificFromList() {
        //favors
        assertEquals(FavorRequest.getList(Favor.StringFields.title, "test", null, null),
                Request.db.getList(Favor.class, "favors", Favor.StringFields.title, "test", null, null));
        //categories
        assertEquals(InterestRequest.getList(Favor.StringFields.title, "test", null, null),
                Request.db.getList(Interest.class, "interests", Interest.StringFields.title, "Food", null, null));
        //users
        assertEquals(UserRequest.getList(User.StringFields.firstName, "Bastien", null, null),
                Request.db.getList(User.class, "users", User.StringFields.firstName, "Bastien", null, null));
    }
}