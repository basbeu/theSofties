package ch.epfl.sweng.favors.database;

import android.provider.ContactsContract;

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

import ch.epfl.sweng.favors.database.Interest;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        });

        interest.set(Interest.StringFields.title, FAKE_TITLE);
    }
}
