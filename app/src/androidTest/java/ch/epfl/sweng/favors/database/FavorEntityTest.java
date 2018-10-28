package ch.epfl.sweng.favors.database;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.Interest;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FavorEntityTest {/*


    private Favor f;
    private String FAKE_DOC_ID  = "klsafdjdlksdf";
    private String FAKE_TITLE = "Fake title";
    private String FAKE_OWNER_ID = "fklskkdja";
    private String FAKE_DESCRIPTION = "This is a fake description";
    private String FAKE_LOCATION_CITY = "Lausanne";
    private Boolean FAKE_IS_OPEN = true;
    private Object FAKE_LOCATION_OBJECT = new Object();

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
        f = new Favor(FAKE_DOC_ID);
    }

    @Test
    public void getTitleTest(){
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->assertEquals(FAKE_TITLE, favor.get(Favor.StringFields.title)));
    }

    @Test
    public void getOwnerIdTest(){
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->assertEquals(FAKE_OWNER_ID, favor.get(Favor.StringFields.ownerID)));
    }

    @Test
    public void getDescriptionTest(){;
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->assertEquals(FAKE_DESCRIPTION, favor.get(Favor.StringFields.description)));
    }

    @Test
    public void getLocationCityTest(){
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->assertEquals(FAKE_LOCATION_CITY, favor.get(Favor.StringFields.locationCity)));
    }

    @Test
    public void getObjectLocationTest(){
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->assertEquals(FAKE_LOCATION_OBJECT, favor.get(Favor.ObjectFields.location)));
    }

    @Test
    public void getIsOpenTest(){
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->assertEquals(FAKE_IS_OPEN, favor.get(Favor.BooleanFields.isOpen)));
    }

    @Test
    public void setIsOpenTest(){
        Boolean newIsOpen = false;
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->{
            favor.set(Favor.BooleanFields.isOpen,newIsOpen);
            Database.getInstance().updateOnDb(favor);
            assertEquals(newIsOpen, favor.get(Favor.BooleanFields.isOpen));
        });
    }

    @Test
    public void setIsOpenDocIdNullTest(){
        Boolean newIsOpen = false;
        Favor favor = new Favor(null);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->{
            favor.set(Favor.BooleanFields.isOpen,newIsOpen);
            Database.getInstance().updateOnDb(favor);
            assertEquals(newIsOpen, favor.get(Favor.BooleanFields.isOpen));
        });
    }

    @Test
    public void setObjectLocationTest(){
        Object newObject = new Object();
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->{
            favor.set(Favor.ObjectFields.location,newObject);
            Database.getInstance().updateOnDb(favor);
            assertEquals(newObject, favor.get(Favor.ObjectFields.location));
        });
    }

    @Test
    public void reset(){
        Favor favor = new Favor(FAKE_DOC_ID);
        favor.reset();
    }

    @Test
    public void getObservableIsOpenTest(){
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->assertEquals(FAKE_IS_OPEN, favor.getObservableObject(Favor.BooleanFields.isOpen).get()));
    }


    @Test
    public void getFailed(){
        HashMap<String,Object> empty = new HashMap();
        Favor f = new Favor(FAKE_DOC_ID);
        f.set(FAKE_DOC_ID, data);
        f.get(Favor.StringFields.title);
        f.get(Favor.ObjectFields.location);
        f.get(Favor.BooleanFields.isOpen);
        f.get(Favor.IntegerFields.creationTimestamp);
    }

    @Test
    public void getObservableLocationObject(){
        Favor favor = new Favor(FAKE_DOC_ID);
        Database.getInstance().updateFromDb(favor).addOnCompleteListener(t->assertEquals(FAKE_LOCATION_OBJECT, favor.getObservableObject(Favor.ObjectFields.location).get()));
    }*/
}
