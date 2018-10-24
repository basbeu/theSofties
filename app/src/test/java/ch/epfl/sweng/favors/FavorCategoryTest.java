package ch.epfl.sweng.favors;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class FavorCategoryTest {


   private String[] favorCategories = {"tutoring", "writing", "designing", "coding",
            "event preparation", "bring groceries", "cooking", "delivery", "driver",
            "place to sleep", "moving", "reparations", "installation", "photography",
            "entertainer", "magician", "clown", "birthday party"
    };


    @Test
    public void favor_category_test_are_accessible() {
        String[] favorCategories = {"tutoring", "writing", "designing", "coding",
                "event preparation", "bring groceries", "cooking", "delivery", "driver",
                "place to sleep", "moving", "reparations", "installation", "photography",
                "entertainer", "magician", "clown", "birthday party"
        };


        ArrayList<String> st = new ArrayList<>();


        for (String fc : FavorCategories.favorCategories) {
            assertEquals(true, isContainedInFavorCategories(fc));
        }
    }

    private boolean isContainedInFavorCategories(String s){
        for (String st : favorCategories) {
            if (st.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
