package ch.epfl.sweng.favors;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class InterestEntityTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private FirebaseFirestore fakeDb;
    @Mock private CollectionReference fakeCollection;
    @Mock private DocumentReference fakeDoc;
    @Mock private DocumentSnapshot fakeDocSnap;

    private Task<DocumentSnapshot> fakeTask;
    private Task<Void> fakeSetTask;
    private HashMap<String,Object> data;

    private String FAKE_DOC_ID  = "klsafdjdlksdf";
    private String FAKE_TITLE = "Fake title";
    private String FAKE_DESCRIPTION = "This is a fake description";
    private Object FAKE_LINKEDINTEREST_OBJECT = new Object();

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
        data = new HashMap<>();
        fakeTask = Tasks.forResult(fakeDocSnap);
        fakeSetTask = Tasks.forResult((Void)null);
        data.put(Interest.StringFields.title.toString(),FAKE_TITLE);
        data.put(Interest.StringFields.description.toString(),FAKE_DESCRIPTION);
        data.put(Interest.ObjectFields.linkedInterests.toString(),FAKE_LINKEDINTEREST_OBJECT);
        when(fakeDb.collection("interests")).thenReturn(fakeCollection);
        when(fakeCollection.document(FAKE_DOC_ID)).thenReturn(fakeDoc);
        when(fakeCollection.add(any(Map.class))).thenReturn(Tasks.forResult(fakeDoc));
        when(fakeDoc.get()).thenReturn(fakeTask);
        when(fakeDoc.set(any())).thenReturn(fakeSetTask);
        when(fakeDocSnap.getData()).thenReturn(data);
    }

    @Test
    public void getTitleTest(){
        Interest i = new Interest (FAKE_DOC_ID,fakeDb);
        i.updateFromDb().addOnCompleteListener(t->assertEquals(FAKE_TITLE, i.get(Interest.StringFields.title)));
    }

    @Test
    public void getTitleFailedTest(){
        when(fakeDoc.get()).thenReturn(Tasks.forCanceled());
        Interest i = new Interest (FAKE_DOC_ID,fakeDb);
    }

    @Test
    public void getLinkedInterestTest(){
        Interest i = new Interest (FAKE_DOC_ID,fakeDb);
        i.updateFromDb().addOnCompleteListener(t->assertEquals(FAKE_LINKEDINTEREST_OBJECT, i.get(Interest.ObjectFields.linkedInterests)));
    }

    @Test
    public void setDocId(){
        Interest i = new Interest (FAKE_DOC_ID,fakeDb);
        i.set(FAKE_DOC_ID, data);
    }


    @Test
    public void setTitleDocIDTest(){
        String newTitle = "tata";
        Interest i = new Interest (null,fakeDb);

        i.updateFromDb().addOnCompleteListener(t->{
            i.set(Interest.StringFields.title, newTitle);
            i.updateOnDb();
            assertEquals(newTitle, i.get(Interest.StringFields.title));
        });
    }

    @Test
    public void setTitleTest(){
        String newTitle = "tata";
        Interest i = new Interest (FAKE_DOC_ID,fakeDb);

        i.updateFromDb().addOnCompleteListener(t->{
            i.set(Interest.StringFields.title, newTitle);
            i.updateOnDb();
            assertEquals(newTitle, i.get(Interest.StringFields.title));
        });
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorTestPurpose(){
        ExecutionMode.getInstance().setTest(false);
        Interest i = new Interest (FAKE_DOC_ID,fakeDb);
    }
}
