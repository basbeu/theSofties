package ch.epfl.sweng.favors.database;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.favors.utils.ExecutionMode;

import static org.junit.Assert.assertEquals;

public class InterestEntityTest {

    private String FAKE_DOC_ID  = "klsafdjdlksdf";
    private String FAKE_TITLE = "Fake title";
    private String FAKE_DESCRIPTION = "This is a fake description";
    private Object FAKE_LINKEDINTEREST_OBJECT = new Object();

    private Interest interest;

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
        interest = new Interest(FAKE_DOC_ID);
        interest.set(Interest.StringFields.title, FAKE_TITLE);
        interest.set(Interest.StringFields.description, FAKE_DESCRIPTION);
        interest.set(Interest.ObjectFields.linkedInterests,FAKE_LINKEDINTEREST_OBJECT);
        FakeDatabase.getInstance().updateOnDb(interest);
    }

    @Test
    public void getTitleTest(){
        Database.getInstance().updateFromDb(interest).addOnCompleteListener(t->assertEquals(FAKE_TITLE, interest.get(Interest.StringFields.title)));
    }

    @Test
    public void getLinkedInterestTest(){
        Database.getInstance().updateFromDb(interest).addOnCompleteListener(t->assertEquals(FAKE_LINKEDINTEREST_OBJECT, interest.get(Interest.ObjectFields.linkedInterests)));
    }

    @Test
    public void setDocId(){
        Interest i = new Interest (FAKE_DOC_ID);
        i.set(FAKE_DOC_ID, interest.getEncapsulatedObjectOfMaps());
    }

    @Test
    public void setTitleTest(){
        String newTitle = "tata";

        Database.getInstance().updateFromDb(interest).addOnCompleteListener(t->{
            interest.set(Interest.StringFields.title, newTitle);
            Database.getInstance().updateOnDb(interest);
            assertEquals(newTitle, interest.get(Interest.StringFields.title));
            interest.set(Interest.StringFields.title, FAKE_TITLE);
        });


    }
}
