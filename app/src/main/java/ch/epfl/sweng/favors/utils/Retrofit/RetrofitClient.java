package ch.epfl.sweng.favors.utils.Retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.net.HttpURLConnection.HTTP_OK;

public class RetrofitClient extends RetrofitDispatcher{


    private static final String BASE_URL = "https://api.mailgun.net/v3/favors.services/";

    private static final String API_USERNAME = "api";

    private static final String API_PASSWORD = "API_KEY";

    private static final String AUTH = "Basic " + Base64.encodeToString((API_USERNAME+":"+API_PASSWORD).getBytes(), Base64.NO_WRAP);

    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                //Adding basic auth
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", AUTH)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Retrofit getClient() {
        return retrofit;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }

    @NonNull
    public Callback<ResponseBody> getCallback(@NonNull Context context, @NonNull String successMsg, @NonNull String failureMsg) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() == HTTP_OK) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        Toast.makeText(context, successMsg, Toast.LENGTH_LONG).show();
                    } catch (JSONException | IOException e) {
                        Toast.makeText(context, failureMsg, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(context, failureMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, failureMsg, Toast.LENGTH_LONG).show();
            }
        };
    }
}