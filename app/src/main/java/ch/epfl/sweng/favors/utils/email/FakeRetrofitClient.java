package ch.epfl.sweng.favors.utils.email;

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

class FakeRetrofitClient extends RetrofitDispatcher {
    private static FakeRetrofitClient mInstance;

    public static synchronized FakeRetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new FakeRetrofitClient();
        }
        return mInstance;
    }

    @Override
    Retrofit getClient() {
        return null;
    }

    @Override
    RetrofitApi getApi() {
        return (from, to, subject, text) -> new Call<ResponseBody>() {
            @Override
            public Response<ResponseBody> execute() throws IOException { return null; }

            @Override
            public void enqueue(Callback<ResponseBody> callback) { callback.onFailure(null,null); }

            @Override
            public boolean isExecuted() { return true; }

            @Override
            public void cancel() { }

            @Override
            public boolean isCanceled() {return false;}

            @Override
            public Call<ResponseBody> clone() {return null; }

            @Override
            public Request request() { return null;}
        };
    }


    /*
    This method allows for the testing of the send email. The result depends on the execution mode that is called
     */
    @Override
    Callback<ResponseBody> getCallback(@NonNull Context context, @NonNull String successMsg, @NonNull String failureMsg) {
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



