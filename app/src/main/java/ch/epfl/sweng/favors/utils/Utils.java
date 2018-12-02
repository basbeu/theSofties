package ch.epfl.sweng.favors.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.internal_db.LocalPreferences;
import ch.epfl.sweng.favors.main.FavorsMain;



public final class Utils {
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

    public static void logout(Context context, Authentication auth){
        Toast.makeText(context, R.string.seeyou, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FavorsMain.class);
        auth.signOut();
        context.startActivity(intent);
        User.resetMain();
        LocalPreferences.closeInstance();
    }

    public static String getYear(Date date) {
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        int year  = localDate.getYear();
//        int month = localDate.getMonthValue();
//        int day   = localDate.getDayOfMonth();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; //Add one to month {0 - 11}
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.toString(year);
    }

    public static String getMonth(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1; //Add one to month {0 - 11}
        return Integer.toString(month);
    }

    public static String getDay(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.toString(day);
    }

    public static String getFullDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return df.format(date);
    }

    final static long DAYS = 432000000; // mili
    final static long DAY = 86400000;

    public static String getFavorDate(Date date) {
        Date today = new Date();
        long difference = getDifference(date, today);
        String dateString = "";
        if(date.before(today)) {
            dateString = "expired";
        } else if (getFullDate(date).equals(getFullDate(today))) {
            dateString =  "today";
        } else if (difference < DAY) {
            dateString = difference/DAY+1 + " day";
        } else if (difference < DAYS) {
            dateString = difference/DAY+1 + " days";
        } else {
            SimpleDateFormat df = new SimpleDateFormat("d.MMM", Locale.getDefault());
            dateString = df.format(date);
        }
        return dateString;
    }

    /**
     * get Date from Datepicker
     * @param day
     * @param month
     * @param year
     * @return
     */
    public static Date getDateFromDatePicker(int day, int month, int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public static String getFavorDateAlternative(Date date) {
        Date today = new Date();
        long difference = getDifference(date, today);
        if(date.before(today)) {
            return "done";
        } else if (getFullDate(date).equals(getFullDate(today))) {
            return "hurry";
        } else if (difference < DAYS) {
            return difference/DAY + " days";
        }
        SimpleDateFormat df = new SimpleDateFormat("d.M.yy", Locale.getDefault());
        return df.format(date);
    }

    public static long getDifference(Date d1, Date d2) {
        long difference = d1.getTime()-d2.getTime();
        return difference;
    }

    public static String getIconPathFromCategory(String category){
        return "android.resource://ch.epfl.sweng.favors/drawable/"+getIconNameFromCategory(category);
    }

    public static String getIconNameFromCategory(String category){
        return category.toLowerCase().replaceAll("\\s","");
    }



}
