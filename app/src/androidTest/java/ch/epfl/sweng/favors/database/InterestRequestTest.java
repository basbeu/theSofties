package ch.epfl.sweng.favors.database;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InterestRequestTest {
    @Test
    public void getsAllFromList() {
        assertEquals(InterestRequest.all(null, null),
                Request.db.getAll(Interest.class, "interests", null, null));
    }

    @Test
    public void getsSpecificFromList() {
        assertEquals(InterestRequest.getList(Favor.StringFields.title, "test", null, null),
                Request.db.getList(Interest.class, "interests", Interest.StringFields.title, "Food", null, null));
    }
}
