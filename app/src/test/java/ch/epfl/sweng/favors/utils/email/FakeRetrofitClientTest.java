package ch.epfl.sweng.favors.utils.email;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import ch.epfl.sweng.favors.utils.ExecutionMode;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.junit.Assert.assertEquals;

public class FakeRetrofitClientTest {

    @Before
    public void before(){
        ExecutionMode.getInstance().setTest(true);
    }

    @Test
    public void getApiWorks() throws IOException{
        RetrofitDispatcher.getInstance().getApi().sendEmail("from", "to", "subject", "test").cancel();
        Call<ResponseBody> cloned = RetrofitDispatcher.getInstance().getApi().sendEmail("from", "to", "subject", "test").clone();
        assertEquals(null, cloned);
        Response<ResponseBody> executed = RetrofitDispatcher.getInstance().getApi().sendEmail("from", "to", "subject", "test").execute();
        assertEquals(null, executed);
        boolean isExecuted = RetrofitDispatcher.getInstance().getApi().sendEmail("from", "to", "subject", "test").isExecuted();
        assertEquals(true, isExecuted);
        Request request = RetrofitDispatcher.getInstance().getApi().sendEmail("from", "to", "subject", "test").request();
        assertEquals(null, request);
        boolean canceled = RetrofitDispatcher.getInstance().getApi().sendEmail("from", "to", "subject", "test").isCanceled();
        assertEquals(false, canceled);
    }

    @Test
    public void getClientReturnsNull(){
        Retrofit client = RetrofitDispatcher.getInstance().getClient();
        assertEquals(null, client);
    }

    @After
    public void after(){
        ExecutionMode.getInstance().setTest(false);
    }
}
