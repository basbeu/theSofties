package ch.epfl.sweng.favors.database;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RequestTest {
    class AnyRequest extends Request {}
    @Test
    public void RequestIsAbstract() {
        assertEquals(AnyRequest.db, Request.db);
    }

    @Before
    public void setup(){

    }

    @Test
    public void tryRequests(){

    }
}