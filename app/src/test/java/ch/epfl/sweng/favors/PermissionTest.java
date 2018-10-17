package ch.epfl.sweng.favors;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PermissionTest {
    @Test
    public void permission_isCorrect() {
        assertEquals(FavorsMain.Permissions.values()[0].ordinal(), FavorsMain.Permissions.LOCATION_REQUEST.ordinal());
    }

}

