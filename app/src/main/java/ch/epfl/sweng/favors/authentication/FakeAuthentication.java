package ch.epfl.sweng.favors.authentication;


public class FakeAuthentication extends Authentication {

    private static FakeAuthentication auth;

    private FakeAuthentication(){

    }

    public static FakeAuthentication getInstance(){
        if(auth == null){
           auth = new FakeAuthentication();
        }

        return auth;
    }
}
