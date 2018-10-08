package ch.epfl.sweng.favours;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final int MAXPASSWORDLEN = 20;
    private static final int MINPASSWORDLEN = 8;

    public static boolean containsChar(String s){
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsDigit(String s){
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Function to check if the email is in a valid format
     * Inspired from: https://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
     *
     * @param email The email to check
     * @return True if the email is in a valid format
     */
    public static Boolean isEmailValid(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Function to check if a password has a valid format
     * Inspired from : https://stackoverflow.com/questions/24857893/password-requirements-program
     *
     * @param password The password to check
     * @return True if the password fits with requirements
     */
    public static Boolean passwordFitsRequirements(String password){

        if (password == null || password.length() < MINPASSWORDLEN || password.length() > MAXPASSWORDLEN) {
            return false;
        }

        return containsChar(password) && containsDigit(password);
    }

    public static void displayToastOnTaskCompletion(Task task, Context context,String msgSuccess, String msgFailure){
        if (task.isSuccessful()) {
            Toast.makeText(context,msgSuccess, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,msgFailure,Toast.LENGTH_SHORT).show();
        }
    }

    public static void logout(Context context){
        Toast.makeText(context, R.string.seeyou, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FavoursMain.class);
        intent.putExtra(FavoursMain.LOGGED_OUT, FavoursMain.Status.Disconnect);
        RuntimeEnvironment.getInstance().isConnected.set(false);
        FirebaseAuth.getInstance().signOut();
        context.startActivity(intent);
    }
}
