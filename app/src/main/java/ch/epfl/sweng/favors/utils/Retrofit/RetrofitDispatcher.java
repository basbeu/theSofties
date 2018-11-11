package ch.epfl.sweng.favors.utils.Retrofit;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.epfl.sweng.favors.utils.ExecutionMode;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public abstract class RetrofitDispatcher {

    private static RetrofitDispatcher mInstance;

    public static synchronized RetrofitDispatcher getInstance() {
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

    public abstract retrofit2.Retrofit getClient();

    public abstract Api getApi() ;

    public abstract Callback<ResponseBody> getCallback(@NonNull Context context, @NonNull String successMsg, @NonNull String failureMsg);
}
