package ch.epfl.sweng.favors.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.ConfirmationSent;
import ch.epfl.sweng.favors.database.internal_db.InternalSqliteDb;

import static junit.framework.TestCase.assertEquals;


@RunWith(AndroidJUnit4.class)
public class UtilsTestImages {


    @Rule public ActivityTestRule<ConfirmationSent> mActivityTestRule = new ActivityTestRule<>(ConfirmationSent.class, true, false);

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock private Context context;

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
    }

    @Test
    public void getImageUriTest(){
        Utils.getImageUri(context, Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8));
    }


    @Test
    public void compressImageUriTest() throws InterruptedException {

        if(Looper.myLooper() == null){
            Looper.prepare();
        }

        mActivityTestRule.launchActivity(null);
        InternalSqliteDb.openDb(mActivityTestRule.getActivity().getApplicationContext());

        Thread.sleep(500);

        Uri path = Uri.parse("android.resource://ch.epfl.sweng.favors.utils/" + R.drawable.no_image);
        Uri result = Utils.compressImageUri(mActivityTestRule.getActivity().getApplicationContext(), path);
        assertEquals(result, path);
        Looper.myLooper().quitSafely();
    }

    @Test
    public void compressImageUriReturnsNullOnNullUri() throws InterruptedException {
        if(Looper.myLooper() == null){
            Looper.prepare();
        }

        mActivityTestRule.launchActivity(null);
        InternalSqliteDb.openDb(mActivityTestRule.getActivity().getApplicationContext());

        Thread.sleep(500);

        Uri result = Utils.compressImageUri(mActivityTestRule.getActivity().getApplicationContext(), null);
        assertEquals(null, result);
        Looper.myLooper().quitSafely();
    }
}
