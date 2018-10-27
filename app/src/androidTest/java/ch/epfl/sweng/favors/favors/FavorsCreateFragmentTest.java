package ch.epfl.sweng.favors.favors;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.epfl.sweng.favors.utils.FragmentTestRule;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.anything;


/*@RestrictTo(RestrictTo.Scope.TESTS)
class SingleFragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content = new FrameLayout(this);
        content.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        content.setId(R.id.fragment_container);
        setContentView(content);
    }
    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, "TEST")
                .commit();
    }
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }
    public Fragment getFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }
}*/

@RunWith(AndroidJUnit4.class)
public class FavorsCreateFragmentTest {

    @Rule
    public FragmentTestRule<FavorCreateFragment> mFragmentTestRule = new FragmentTestRule<>(FavorCreateFragment.class);

    @Mock Favor favor;
    //Fragment createFrag = new FavorCreateFragment();

    @Before
    public void Before(){
        //
    }

    @Test
    public void fragment_can_be_instantiated() {
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.createFavorTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void isStringValidTest(){

        assertEquals(FavorCreateFragment.isStringValid("blablabla"), true);

    }

    @Test
    public void changesTitle() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.titleFavor)).perform(typeText("title")).check(matches(withText("title")));
    }

    @Test
    public void changesDescription() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.descriptionFavor)).perform(typeText("description")).check(matches(withText("description")));
    }

    @Test
    public void changesLocation() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.locationFavor)).perform(typeText("location")).check(matches(withText("location")));
    }

    @Test
    public void interestSpinnerTest(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.categoryFavor)).perform(click());
    }


    /*@Test
    public void allFavorsValidisFalse(){

        mFragmentTestRule.getFragment().allFavorFieldsValid();
    }*/

    /*@Test
    public void createFavorIfValidTest(){
        //mActivityTestRule.getFragment().createFavorIfValid(favor);
    }*/


}
