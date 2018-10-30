package ch.epfl.sweng.favors.database;

import org.junit.Test;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

import static org.junit.Assert.assertEquals;

public class FavorRequestTest {

    @Test
    public void getsAllFromList() {
        assertEquals(FavorRequest.all(null, null),
                Request.db.getAll(Favor.class, "favors", null, null));
    }

    @Test
    public void getsSpecificFromList() {
        assertEquals(FavorRequest.getList(Favor.StringFields.title, "test", null, null),
                Request.db.getList(Favor.class, "favors", Favor.StringFields.title, "test", null, null));
    }
}
