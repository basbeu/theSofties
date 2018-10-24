package ch.epfl.sweng.favors;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseUser;
import ch.epfl.sweng.favors.database.User;

public class ConfirmationSent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_sent);
        Button gotIt = findViewById(R.id.gotItButton);
        gotIt.setOnClickListener(v-> goMain());
        Button ResendConf = findViewById(R.id.resendConfirmationMailButton);
        ResendConf.setOnClickListener(v -> sendConfirmationMail(User.getMain().getInstance().getCurrentUser()));

    }

    private void goMain(){
        User.getMain().getInstance().signOut();
        Intent intent = new Intent(this, AuthentificationProcess.class);
        intent.putExtra(AuthentificationProcess.AUTHENTIFICATION_ACTION, AuthentificationProcess.Action.Login);
        startActivity(intent);
    }

    private void sendConfirmationMail(final FirebaseUser user){
        if(user != null){
            user.sendEmailVerification().addOnSuccessListener(v-> Toast.makeText(this, "Email confirmation sent successfully", Toast.LENGTH_LONG).show())
                    .addOnFailureListener(v-> Toast.makeText(this, "Unable to send email", Toast.LENGTH_LONG).show());
        }
        else{
            Toast.makeText(this, "Unable to send email", Toast.LENGTH_SHORT).show();
        }
    }

}