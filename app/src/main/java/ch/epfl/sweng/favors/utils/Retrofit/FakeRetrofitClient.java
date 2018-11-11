package ch.epfl.sweng.favors.utils.Retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.IOException;

import ch.epfl.sweng.favors.utils.ExecutionMode;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FakeRetrofitClient extends RetrofitDispatcher {
    private static FakeRetrofitClient mInstance;

    public static synchronized FakeRetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new FakeRetrofitClient();
        }
        return mInstance;
    }

    @Override
    public Retrofit getClient() {
        return null;
    }

    @Override
    public Api getApi() {
        return new Api() {
            @Override
            public Call<ResponseBody> sendEmail(String from, String to, String subject, String text) {
                return null;
            }
        };
    }

    @Override
    public Callback<ResponseBody> getCallback(@NonNull Context context, @NonNull String successMsg, @NonNull String failureMsg) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(ExecutionMode.getInstance().isInvalidAuthTest()){
                    Toast.makeText(context, failureMsg, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, successMsg, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onResponse(call,null);
            }
        };
    }


}



