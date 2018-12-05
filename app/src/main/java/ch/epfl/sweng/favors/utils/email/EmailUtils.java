package ch.epfl.sweng.favors.utils.email;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.epfl.sweng.favors.database.ApiKeys;
import ch.epfl.sweng.favors.database.Database;

/**
 * Provides the utilities that allow for the sending of emails.
 */
public class EmailUtils {
    private static String TAG = "EMAIL_UTILS";
    /**
     * Sends an email to `to` originating from `from`.
     *
     * Testing details implementation:
     * If ExecutionMode.getInstance().setInvalidAuthTest(true) then the email will fail to be send and toast for a failed message will be shown
     *
     * @param email
     * @param context - the current context that is using this method
     * @param successMsg - text that will be displayed as a toast if the email is successfully send
     * @param failureMsg - text that will be displayed as a toast if the email fails to be send
     */
    public static void sendEmail(Email email, @NonNull Context context, @NonNull String successMsg, @NonNull String failureMsg){

        //Ensure that API Keys are up to date before calling the API
        ApiKeys key = ApiKeys.getInstance();
        Database.getInstance().updateFromDb(key).addOnCompleteListener(t->{

            RetrofitDispatcher.getInstance()
                    .getApi()
                    .sendEmail(email.getFrom(), email.getTo(), email.getSubject(), email.getMessage())
                    .enqueue(RetrofitDispatcher.getInstance().getCallback(context, successMsg, failureMsg));
        });

    }
}