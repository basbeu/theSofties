package ch.epfl.sweng.favors.database;

import android.support.test.runner.AndroidJUnit4;

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

import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

;

public class UserEntityTest {

    private User u;

    private final String FAKE_UID = "sklfklalsdj";
    private final String FAKE_EMAIL = "thisisatestemail@email.com";
    private final String FAKE_FIRST_NAME = "toto";
    private final String FAKE_LAST_NAME = "foo";
    private final String FAKE_SEX = "M";
    private final Integer FAKE_TIMESTAMP = 343354;

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();

        u = new User(FAKE_UID);
        u.set(User.StringFields.firstName, FAKE_FIRST_NAME);
        u.set(User.StringFields.lastName, FAKE_LAST_NAME);
        u.set(User.StringFields.email, FAKE_EMAIL);
        u.set(User.StringFields.city, FAKE_EMAIL);
        u.set(User.IntegerFields.creationTimeStamp, FAKE_TIMESTAMP);
        User.UserGender.setGender(u, User.UserGender.M);

        FakeDatabase.getInstance().updateOnDb(u);
    }

    @Test
    public void getFirstNameTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_FIRST_NAME, u.get(User.StringFields.firstName)));
    }

    @Test
    public void getLastNameTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_LAST_NAME, u.get(User.StringFields.lastName)));
    }

    @Test
    public void getEmailTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_EMAIL, u.get(User.StringFields.email)));
    }

    @Test
    public void getSexTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_SEX, u.get(User.StringFields.sex)));
    }

    @Test
    public void getGenderTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(User.UserGender.M,User.UserGender.getGenderFromUser(u)));
    }

    @Test
    public void getTimestampTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_TIMESTAMP, u.get(User.IntegerFields.creationTimeStamp)));
    }

    @Test
    public void setFirstNameTest(){
        String newFirstName = "tata";

        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->{
            u.set(User.StringFields.firstName, newFirstName);
            FakeDatabase.getInstance().updateOnDb(u);
            assertEquals(newFirstName, u.get(User.StringFields.firstName));
        });

        //Reset basic user info for other tests
        u.set(User.StringFields.firstName, FAKE_FIRST_NAME);
        FakeDatabase.getInstance().updateOnDb(u);
    }

    @Test
    public void setLastNameTest(){
        String newLastName = "foofoo";

        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->{
            u.set(User.StringFields.lastName, newLastName);
            FakeDatabase.getInstance().updateOnDb(u);
            assertEquals(newLastName, u.get(User.StringFields.lastName));
        });

        //Reset basic user info for other tests
        u.set(User.StringFields.lastName, FAKE_LAST_NAME);
        FakeDatabase.getInstance().updateOnDb(u);
    }

    @Test
    public void setEmailTest(){
        String newEmail = "email@new.com";

        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->{
            u.set(User.StringFields.email, newEmail);
            FakeDatabase.getInstance().updateOnDb(u);
            assertEquals(newEmail, u.get(User.StringFields.email));
        });

        //Reset basic user info for other tests
        u.set(User.StringFields.email, FAKE_EMAIL);
        FakeDatabase.getInstance().updateOnDb(u);
    }

    @Test
    public void setGenderTest(){

        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->{
            User.UserGender.setGender(u, User.UserGender.F);
            assertEquals(User.UserGender.F,User.UserGender.getGenderFromUser(u));
        });

        //Reset basic user info for other tests
        User.UserGender.setGender(u, User.UserGender.M);
        FakeDatabase.getInstance().updateOnDb(u);
    }

    @Test
    public void setTimestampTest(){
        Integer newTimestamp = 788484;

        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->{
            u.set(User.IntegerFields.creationTimeStamp, newTimestamp);
            FakeDatabase.getInstance().updateOnDb(u);
            assertEquals(newTimestamp, u.get(User.IntegerFields.creationTimeStamp));
        });

        //Reset basic user info for other tests
        u.set(User.IntegerFields.creationTimeStamp, FAKE_TIMESTAMP);
        FakeDatabase.getInstance().updateOnDb(u);

    }

    @Test
    public void getObservableFirstNameTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_FIRST_NAME, u.getObservableObject(User.StringFields.firstName).get()));
    }

    @Test
    public void getObservableLastNameTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_LAST_NAME, u.getObservableObject(User.StringFields.lastName).get()));
    }

    @Test
    public void getObservableEmailTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_EMAIL, u.getObservableObject(User.StringFields.email).get()));
    }

    @Test
    public void getObservableGenderStringTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t-> assertEquals(User.UserGender.getGenderFromUser(u).toString(),User.UserGender.getObservableGenderString(u).get()));
    }

    @Test
    public void getObservableTimestampTest(){
        FakeDatabase.getInstance().updateFromDb(u).addOnCompleteListener(t->assertEquals(FAKE_TIMESTAMP, u.getObservableObject(User.IntegerFields.creationTimeStamp).get()));
    }

}
