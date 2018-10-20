package ch.epfl.sweng.favors;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.database.User;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private FirebaseAuth fakeAuth;

    private final String FAKE_EMAIL = "thisisatestemail@email.com";
    private final String FAKE_FIRST_NAME = "toto";

    @Before
    public void Before(){

    }

    @Test
    public void getNameTest(){
        User user = new User();
        assertEquals(user.get(User.StringFields.firstName), FAKE_FIRST_NAME);
    }
}
