package ch.epfl.sweng.favors;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), FavorsMain.class);
            startActivity(intent);
            finish();
        }, 2000);

    }


}
