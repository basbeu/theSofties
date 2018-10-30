package ch.epfl.sweng.favors.database;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserRequestTest {

    @Test
    public void getsSpecificFromList() {
        assertEquals(UserRequest.getList(User.StringFields.firstName, "Bastien", null, null),
                Request.db.getList(User.class, "users", User.StringFields.firstName, "Bastien", null, null));
    }
}
