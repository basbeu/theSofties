package ch.epfl.sweng.favors.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import ch.epfl.sweng.favors.BuildConfig;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // get version number from Build config
        String versionName = BuildConfig.VERSION_NAME;
        // debug log
        Log.d("version number", versionName);

        // get text view and set the version number in the splash screen
        TextView textView = findViewById(R.id.versionNumber);
        textView.setText(versionName);
        Database.getInstance().updateFromDb(User.getMain());
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), FavorsMain.class);
            startActivity(intent);
            finish();
        }, 2000);

    }


}
