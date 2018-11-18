package ch.epfl.sweng.favors.location;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SortLocationsTest {
    private UiDevice device;

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();

    }

    @Test
    public void setFavorsSorting() {
        GeoPoint point1 = new GeoPoint(46.51600,6.63282);
        GeoPoint point2 = new GeoPoint(48.85341,2.3488);
        Favor f1 = new Favor();
        Favor f2 = new Favor();
        f1.set(Favor.ObjectFields.location, point1);
        f2.set(Favor.ObjectFields.location, point2);
        SortLocations sl = new SortLocations(new GeoPoint(47.36667, 8.55));
        assertEquals(-315791, sl.compare(f1,f2));
    }
}
