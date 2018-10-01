package ch.epfl.sweng.favours;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RuntimeEnvironment {

    private static RuntimeEnvironment runtimeEnvironment;

    static RuntimeEnvironment getInstance(){
        return runtimeEnvironment;
    }
    public RuntimeEnvironment(){
        this.runtimeEnvironment = this;
    }

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    public ObservableBoolean isConnected = new ObservableBoolean((currentUser != null));



}
