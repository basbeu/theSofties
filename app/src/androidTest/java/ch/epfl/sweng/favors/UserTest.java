package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.favors.database.User;

import static org.junit.Assert.assertEquals;;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private FirebaseAuth fakeAuth;
    @Mock private FirebaseFirestore fakeDb;
    @Mock private CollectionReference fakeCollection;
    @Mock private DocumentReference fakeDoc;
    @Mock private DocumentSnapshot fakeDocSnap;

    private Task<DocumentSnapshot> fakeTask;
    private HashMap<String,Object> data;
    private User user;

    private final String FAKE_UID = "sklfklalsdj";
    private final String FAKE_EMAIL = "thisisatestemail@email.com";
    private final String FAKE_FIRST_NAME = "toto";
    private final String FAKE_LAST_NAME = "foo";
    private final String FAKE_SEX = "M";

    @Before
    public void Before(){
        fakeTask = Tasks.forResult(fakeDocSnap);
        data = new HashMap<>();
        data.put(User.StringFields.firstName.toString(),FAKE_FIRST_NAME);
        data.put(User.StringFields.lastName.toString(),FAKE_LAST_NAME);
        data.put(User.StringFields.email.toString(), FAKE_EMAIL);
        data.put(User.StringFields.sex.toString(), FAKE_SEX);
        when(fakeAuth.getUid()).thenReturn(FAKE_UID);
        when(fakeDb.collection("users")).thenReturn(fakeCollection);
        when(fakeCollection.document(FAKE_UID)).thenReturn(fakeDoc);
        when(fakeDoc.get()).thenReturn(fakeTask);
        when(fakeDocSnap.getData()).thenReturn(data);

        user = new User(fakeAuth, fakeDb);
    }


    @Test
    public void getFirstNameTest(){
        user.updateFromDb().addOnCompleteListener(t->assertEquals(FAKE_FIRST_NAME, user.get(User.StringFields.firstName)));
    }

    @Test
    public void getLastNameTest(){
        user.updateFromDb().addOnCompleteListener(t->assertEquals(FAKE_LAST_NAME, user.get(User.StringFields.lastName)));
    }

    @Test
    public void getEmailTest(){
        user.updateFromDb().addOnCompleteListener(t->assertEquals(FAKE_EMAIL, user.get(User.StringFields.email)));
    }

    @Test
    public void getSexTest(){
        user.updateFromDb().addOnCompleteListener(t->assertEquals(FAKE_SEX, user.get(User.StringFields.sex)));
    }


}
