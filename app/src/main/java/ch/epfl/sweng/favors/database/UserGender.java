package ch.epfl.sweng.favors.database;

import android.databinding.ObservableField;
import android.util.Log;

import javax.annotation.Nonnull;

/**
 * This class should be used when accessing user sexField
 */
public enum UserGender {
    M ,F, DEFAULT;

    private static final String TAG = "USER_GENDER";

    /**
     * Provides users gender. If user gender cannot be found it will return the DEFAULT value. If the gender is determined this method will return  M or F.
     * @param user that needs to be accessed to determine sex
     * @return M or F depending on users sex. Can return DEFAULT if
     */
    static public UserGender getGenderFromUser(User user){
        if(user != null) {
            String gender = user.get(User.StringFieldProtected.sex);
            gender = gender.trim().substring(0, 1);
            if (gender.toUpperCase().equals("M"))
                return M;
            else if (gender.toUpperCase().equals("F"))
                return F;
        }
        Log.e(TAG,"Failed to parse the gender returned by the database");
        return DEFAULT;
    }

    /**
     * Sets the gender of the user
     * @param user who sex needs to be modified
     * @param gender to set the users gender to
     */
    static public void setGender(@Nonnull User user,@Nonnull UserGender gender){
        user.set(User.StringFieldProtected.sex,gender.toString().toUpperCase());
    }

    static public ObservableField<String> getObservableGenderSString(@Nonnull User user){
        return user.getObservableStringObject(User.StringFieldProtected.sex);
    }
}
