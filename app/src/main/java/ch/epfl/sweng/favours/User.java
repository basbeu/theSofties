package ch.epfl.sweng.favours;

public class User {
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
