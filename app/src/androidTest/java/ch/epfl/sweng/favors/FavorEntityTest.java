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

import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FavorEntityTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private FirebaseFirestore fakeDb;
    @Mock private CollectionReference fakeCollection;
    @Mock private DocumentReference fakeDoc;
    @Mock private DocumentSnapshot fakeDocSnap;

    private Task<DocumentSnapshot> fakeTask;
    private Task<Void> fakeSetTask;
    private HashMap<String,Object> data;

    private String FAKE_DOC_ID  = "klsafdjdlksdf";
    private String FAKE_TITLE = "Fake title";
    private String FAKE_OWNER_ID = "fklskkdja";
    private String FAKE_DESCRIPTION = "This is a fake description";
    private String FAKE_LOCATION_CITY = "Lausanne";
    private Boolean FAKE_IS_OPEN = true;

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
        data = new HashMap<>();
        fakeTask = Tasks.forResult(fakeDocSnap);
        fakeSetTask = Tasks.forResult((Void)null);
        data.put(Favor.StringFields.title.toString(),FAKE_TITLE);
        data.put(Favor.StringFields.ownerID.toString(),FAKE_OWNER_ID);
        data.put(Favor.StringFields.description.toString(),FAKE_DESCRIPTION);
        data.put(Favor.StringFields.locationCity.toString(),FAKE_LOCATION_CITY);
        data.put(Favor.BooleanFields.isOpen.toString(), FAKE_IS_OPEN);
        when(fakeDb.collection("favors")).thenReturn(fakeCollection);
        when(fakeCollection.document(FAKE_DOC_ID)).thenReturn(fakeDoc);
        when(fakeDoc.get()).thenReturn(fakeTask);
        when(fakeDoc.set(any())).thenReturn(fakeSetTask);
        when(fakeDocSnap.getData()).thenReturn(data);
    }

    @Test
    public void getTitleTest(){
        Favor favor = new Favor(FAKE_DOC_ID,fakeDb);
        favor.updateFromDb().addOnCompleteListener(t->assertEquals(FAKE_TITLE, favor.get(Favor.StringFields.title)));
    }
}
