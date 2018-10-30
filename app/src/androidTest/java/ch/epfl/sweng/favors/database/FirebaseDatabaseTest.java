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

import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FirebaseDatabaseTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private FirebaseFirestore fakeDb;
    @Mock
    private CollectionReference fakeCollection;
    @Mock
    private DocumentReference fakeDoc;
    @Mock
    private DocumentSnapshot fakeDocSnap;

    private Task<DocumentSnapshot> fakeTask;
    private Task<Void> fakeSetTask;
    private HashMap<String, Object> data;
    private String FAKE_DOC_ID = "klsafdjdlksdf";
    private String NEW_TITLE = "New favor title";


    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
        data = new HashMap<>();
        fakeTask = Tasks.forResult(fakeDocSnap);
        fakeSetTask = Tasks.forResult(null);
        when(fakeDb.collection("favors")).thenReturn(fakeCollection);
        when(fakeCollection.document(FAKE_DOC_ID)).thenReturn(fakeDoc);
        when(fakeDoc.get()).thenReturn(fakeTask);
        when(fakeDoc.set(any())).thenReturn(fakeSetTask);
        when(fakeDocSnap.getData()).thenReturn(data);
        FirebaseDatabase.setFirebaseTest(fakeDb);

    }

    @Test
    public void updateFromAndOnDbTest() {
        Favor favor = new Favor(FAKE_DOC_ID);
        FirebaseDatabase.getInstance().updateFromDb(favor).addOnCompleteListener(t -> {
            favor.set(Favor.StringFields.title, NEW_TITLE);
            FirebaseDatabase.getInstance().updateOnDb(favor);
            assertEquals("New favor title", favor.get(Favor.StringFields.title));
        });
    }
}
