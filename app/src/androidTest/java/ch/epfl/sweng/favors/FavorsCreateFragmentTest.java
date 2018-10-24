package ch.epfl.sweng.favors;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.epfl.sweng.favors.database.Favor;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;


@RestrictTo(RestrictTo.Scope.TESTS)
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
}

@RunWith(AndroidJUnit4.class)
public class FavorsCreateFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> mActivityTestRule = new ActivityTestRule<>(SingleFragmentActivity.class);

    @Mock Favor favor;

    @Before
    public void Before(){

    }

    @Test
    public void fragment_can_be_instantiated() {
        onView(withId(R.id.createFavorTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void isStringValidTest(){

        assertEquals(FavorCreateFragment.isStringValid("blablabla"), true);

    }

    @Test
    public void allFavorsValidisFalse(){
        assertEquals(((FavorCreateFragment) mActivityTestRule.getActivity().getFragment()).allFavorFieldsValid(), false);
    }

    @Test
    public void createFavorIfValidTest(){
        //mActivityTestRule.getFragment().createFavorIfValid(favor);
    }



    
}
