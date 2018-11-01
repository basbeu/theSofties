package ch.epfl.sweng.favors.database;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.favors.utils.ExecutionMode;

import static org.junit.Assert.assertEquals;

public class FavorEntityTest {


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

        f.set(Favor.StringFields.ownerID, FAKE_OWNER_ID);
        f.set(Favor.StringFields.description, FAKE_DESCRIPTION);
        f.set(Favor.StringFields.title, FAKE_TITLE);
        f.set(Favor.StringFields.locationCity, FAKE_LOCATION_CITY);
        f.set(Favor.BooleanFields.isOpen, FAKE_IS_OPEN);
        f.set(Favor.ObjectFields.location, FAKE_LOCATION_OBJECT);

        FakeDatabase.getInstance().updateOnDb(f);
    }

    @Test
    public void getTitleTest(){
        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->assertEquals(FAKE_TITLE, f.get(Favor.StringFields.title)));
    }

    @Test
    public void getOwnerIdTest(){
        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->assertEquals(FAKE_OWNER_ID, f.get(Favor.StringFields.ownerID)));
    }

    @Test
    public void getDescriptionTest(){;

        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->assertEquals(FAKE_DESCRIPTION, f.get(Favor.StringFields.description)));
    }

    @Test
    public void getLocationCityTest(){
        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->assertEquals(FAKE_LOCATION_CITY, f.get(Favor.StringFields.locationCity)));
    }

    @Test
    public void getObjectLocationTest(){
        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->assertEquals(FAKE_LOCATION_OBJECT, f.get(Favor.ObjectFields.location)));
    }

    @Test
    public void getIsOpenTest(){
        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->assertEquals(FAKE_IS_OPEN, f.get(Favor.BooleanFields.isOpen)));
    }

    @Test
    public void setIsOpenTest(){
        Boolean newIsOpen = false;

        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->{
            f.set(Favor.BooleanFields.isOpen,newIsOpen);
            Database.getInstance().updateOnDb(f);
            assertEquals(newIsOpen, f.get(Favor.BooleanFields.isOpen));
            //Set original data for other tests
            f.set(Favor.BooleanFields.isOpen, FAKE_IS_OPEN);
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

        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->{
            f.set(Favor.ObjectFields.location,newObject);
            Database.getInstance().updateOnDb(f);

            assertEquals(newObject, f.get(Favor.ObjectFields.location));
            //Set original data for other tests
            f.set(Favor.ObjectFields.location, FAKE_LOCATION_OBJECT);
        });


    }

    @Test
    public void reset(){
        Favor favor = new Favor(FAKE_DOC_ID);
        favor.reset();
    }

    @Test
    public void getObservableIsOpenTest(){

        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->assertEquals(FAKE_IS_OPEN, f.getObservableObject(Favor.BooleanFields.isOpen).get()));
    }


    @Test
    public void getObservableLocationObject(){
        Database.getInstance().updateFromDb(f).addOnCompleteListener(t->assertEquals(FAKE_LOCATION_OBJECT, f.getObservableObject(Favor.ObjectFields.location).get()));
    }
}
