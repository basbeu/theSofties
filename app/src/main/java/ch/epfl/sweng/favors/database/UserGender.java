package ch.epfl.sweng.favors.database;

import javax.annotation.Nonnull;

public enum UserGender {
    M ,F, DEFAULT;

    static public UserGender getGenderFromUser(User user){
        if(user != null) {
            String gender = user.get(User.StringFields.sex);
            gender = gender.trim().substring(0, 1);
            if (gender.toUpperCase().equals("M"))
                return M;
            else if (gender.toUpperCase().equals("F"))
                return F;
        }
            return DEFAULT;
    }

    static public void setGender(@Nonnull User user,@Nonnull UserGender gender){
        user.set(User.StringFields.sex,gender.toString().toUpperCase());
    }
}
