package ch.epfl.sweng.favours;

public class User {

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String SEX = "sex";
    public static final String BASED_LOCATION = "basedLocation";
    public static final String USERS = "users";

    private String firstName;
    private String lastName;
    private String email;
    private String sex;
    private String basedLocation;

    public User(){}


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }

    public String getBasedLocation() {
        return basedLocation;
    }

    public String[] userInfoList(){
        return new String[]{"First Name", firstName, "Last Name", lastName, "Email", email, "Sex", sex, "Ciry", basedLocation};
    }

}
