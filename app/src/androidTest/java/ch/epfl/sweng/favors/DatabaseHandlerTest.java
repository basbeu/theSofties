package ch.epfl.sweng.favors;

import android.databinding.ObservableField;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import ch.epfl.sweng.favors.database.DatabaseHandler;
import ch.epfl.sweng.favors.database.DatabaseStringField;
import ch.epfl.sweng.favors.database.User;

@RunWith(AndroidJUnit4.class)
public class DatabaseHandlerTest {

    private enum StringFields implements DatabaseStringField {firstName, lastName, email, sex, pseudo, city}

    @Test
    public void resetMap_clearsMap(){
        Map<DatabaseStringField, ObservableField<String>> stringData =
                new HashMap<DatabaseStringField, ObservableField<String>>(){
                    {
                        for(DatabaseStringField field : StringFields.values()){
                            this.put(field, new ObservableField<String>("TEST"));
                        }
                    }
                };
        assertThat(stringData.get(StringFields.firstName).get(), is("TEST"));
        assertThat(stringData.get(StringFields.lastName).get(), is("TEST"));
        assertThat(stringData.get(StringFields.email).get(), is("TEST"));
        assertThat(stringData.get(StringFields.sex).get(), is("TEST"));
        assertThat(stringData.get(StringFields.pseudo).get(), is("TEST"));
        assertThat(stringData.get(StringFields.city).get(), is("TEST"));

        try {
            DatabaseHandler db = new User();
            Method privateResetMap = DatabaseHandler.class.getDeclaredMethod("resetMap", Map.class, Object.class);
            privateResetMap.setAccessible(true);
            privateResetMap.invoke(db,stringData, "");
            assertThat(stringData.get(StringFields.firstName).get(), is( ""));
            assertThat(stringData.get(StringFields.lastName).get(), is(""));
            assertThat(stringData.get(StringFields.email).get(), is(""));
            assertThat(stringData.get(StringFields.sex).get(), is(""));
            assertThat(stringData.get(StringFields.pseudo).get(), is(""));
            assertThat(stringData.get(StringFields.city).get(), is(""));
        } catch (Exception e) {
            assertThat("", is(e.toString()));
        }
    }
}
