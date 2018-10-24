package ch.epfl.sweng.favors;

import ch.epfl.sweng.favors.BuildConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ch.epfl.sweng.favors.R;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // get version number from Build config
        String versionName = BuildConfig.VERSION_NAME;
        // debug log
        Log.d("version number", versionName);

        // get text view and set the version number in the splash screebn
        TextView textView = findViewById(R.id.versionNumber);
        textView.setText(versionName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), FavorsMain.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }


}
