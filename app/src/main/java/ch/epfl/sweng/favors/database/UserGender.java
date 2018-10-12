package ch.epfl.sweng.favors.database;

public enum UserGender {
    M ,F, DEFAULT;

    static public UserGender getGenderFromString(String gender){
        gender = gender.trim().substring(0, 1);
        if(gender.toLowerCase().equals("m"))
            return M;
        else if(gender.toLowerCase().equals("f"))
            return F;
        else
            return DEFAULT;
    }

}
