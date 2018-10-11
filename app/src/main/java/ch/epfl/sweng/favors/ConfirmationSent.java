package ch.epfl.sweng.favors;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.favors.R;

public class ConfirmationSent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_sent);
        Button gotIt = findViewById(R.id.gotItButton);
        gotIt.setOnClickListener(v-> goMain());
        Button ResendConf = findViewById(R.id.resendConfirmationMailButton);
        ResendConf.setOnClickListener(v -> sendConfirmationMail(FirebaseAuth.getInstance().getCurrentUser()));

    }

    private void goMain(){
        RuntimeEnvironment.getInstance().isConnected.set(false);
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, AuthentificationProcess.class);
        intent.putExtra(FavorsMain.AUTHENTIFICATION_ACTION, FavorsMain.Status.Login);
        startActivity(intent);
    }

    private void sendConfirmationMail(final FirebaseUser user){
        if(user != null){
            user.sendEmailVerification().addOnSuccessListener(v-> Toast.makeText(this, "Email confirmation sent successfully", Toast.LENGTH_SHORT).show());
        }
        else{
            Toast.makeText(this, "Unable to send email", Toast.LENGTH_SHORT).show();
        }
    }

}
