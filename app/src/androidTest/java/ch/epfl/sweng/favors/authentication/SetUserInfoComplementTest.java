package ch.epfl.sweng.favors.authentication;

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.internal_db.InternalSqliteDb;
import ch.epfl.sweng.favors.main.FavorsMain;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;
import static junit.framework.TestCase.assertEquals;
/**
 * Used as container to test fragments in isolation with Espresso
 */
@RunWith(AndroidJUnit4.class)
public class SetUserInfoComplementTest {
    @Rule public IntentsTestRule<SetUserInfo> intentsTestRule = new IntentsTestRule<SetUserInfo>(SetUserInfo.class, true,  false);
    private final String FAKEFIRSTNAME = "Bastien";
    private final String FAKELASTNAME = "Beuchat";
    private final String FAKECITY = "Lausanne";
    private User u;
    @Before
    public void Before(){
        Database.cleanUpAll();
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();

        Intent intent = new Intent();
        intent.putExtra(FavorsMain.TEST_MODE, "true");
        intentsTestRule.launchActivity(intent);
        onView(ViewMatchers.withId(R.id.userFirstNameEdit)).perform(scrollTo(),replaceText(FAKEFIRSTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.userLastNameEdit)).perform(scrollTo(),replaceText(FAKELASTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.userCityEdit)).perform(scrollTo(),replaceText(FAKECITY)).perform(closeSoftKeyboard());
        onView(withId(R.id.profGenderMEdit)).perform(scrollTo(),click());
        onView(withId(R.id.profGenderFEdit)).perform(scrollTo(),click());
        u = new User(Authentication.getInstance().getUid());

        InternalSqliteDb.openDb(intentsTestRule.getActivity().getApplicationContext());
    }
    @Test
    public void testBackButton(){
        Database.getInstance().updateFromDb(u);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
        }
        Espresso.pressBackUnconditionally();
        intended(hasComponent(new ComponentName(getTargetContext(), FavorsMain.class)));
    }

    @After
    public void After(){
        Database.cleanUpAll();
    }
}