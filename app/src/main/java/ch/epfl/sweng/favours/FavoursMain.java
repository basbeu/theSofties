package ch.epfl.sweng.favours;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "TheSoftiesApp";

    private RuntimeEnvironment runtimeEnvironment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runtimeEnvironment = new RuntimeEnvironment();



        setContentView(R.layout.activity_main);
    }
}
