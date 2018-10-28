package ch.epfl.sweng.favors.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import ch.epfl.sweng.favors.R;

public class ConfirmationSent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_sent);
        Button gotIt = findViewById(R.id.gotItButton);
        gotIt.setOnClickListener(v-> goMain());
        Button ResendConf = findViewById(R.id.resendConfirmationMailButton);
        ResendConf.setOnClickListener(v->sendConfirmationMail());
    }

    private void goMain(){
        Authentication.getInstance().signOut();
        Intent intent = new Intent(this, AuthenticationProcess.class);
        intent.putExtra(AuthenticationProcess.AUTHENTIFICATION_ACTION, AuthenticationProcess.Action.Login);
        startActivity(intent);
    }

    private void sendConfirmationMail(){
        Authentication.getInstance().sendEmailVerification().addOnSuccessListener(v-> Toast.makeText(this, "Email confirmation sent successfully", Toast.LENGTH_LONG).show())
                .addOnFailureListener(v-> Toast.makeText(this, "Unable to send email", Toast.LENGTH_LONG).show());
    }

}