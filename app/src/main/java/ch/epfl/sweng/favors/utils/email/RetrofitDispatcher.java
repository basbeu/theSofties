package ch.epfl.sweng.favors.utils.Retrofit;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.epfl.sweng.favors.utils.ExecutionMode;
import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * Allows for access to the retrofitClient to send and receive emails
 */
abstract class RetrofitDispatcher {

    private static RetrofitDispatcher mInstance;

    static synchronized RetrofitDispatcher getInstance() {
        if (mInstance == null) {
            if(ExecutionMode.getInstance().isTest()) {
                mInstance = FakeRetrofitClient.getInstance();
            }
            else{
                mInstance = RetrofitClient.getInstance();
            }
        }
        return mInstance;
    }

    abstract retrofit2.Retrofit getClient();

    abstract Api getApi() ;

    abstract Callback<ResponseBody> getCallback(@NonNull Context context, @NonNull String successMsg, @NonNull String failureMsg);
}
