package ch.epfl.sweng.favors.database;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ObservableListTest {

    @Test
    public void observableListTest() {
        Favor testFavor0 = new Favor();
        Favor testFavor1 = new Favor();
        Favor testFavor2 = new Favor();
        ObservableArrayList<Favor> testList = new ObservableArrayList<>();

        testList.add(testFavor0);
        testList.add(0,testFavor1);
        testList.remove(testFavor0);
        testList.clear();

        testList.add(testFavor0);
        testList.add(testFavor1);
        ObservableArrayList<Favor> testList2 = new ObservableArrayList<>();
        testList2.addAll(testList);

        testList2.remove(1);
        testList.remove(testFavor1);
        testList.removeAll(testList2);

        testList.add(testFavor2);
        testList.remove(testFavor2);
        assertEquals(0, testList.size());
        assertEquals(1, testList2.size());

    }
}
