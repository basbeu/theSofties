package ch.epfl.sweng.favors.database.storage;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.R;

import ch.epfl.sweng.favors.favors.FavorCreateFragment;
import ch.epfl.sweng.favors.favors.FavorDetailView;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class StorageTest {

    @Rule public FragmentTestRule<FavorCreateFragment> mFragmentTestRule = new FragmentTestRule<FavorCreateFragment>(FavorCreateFragment.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private FirebaseStorage firebaseStorage;
    @Mock private StorageReference ref;
    @Mock private StorageReference storageReference;
    @Mock private UploadTask task;
    @Mock private StorageTask<UploadTask.TaskSnapshot> successTask;
    @Mock private StorageTask<UploadTask.TaskSnapshot> failureTask;

    @Before
    public void before(){
        ExecutionMode.getInstance().setTest(true);
        when(storageReference.child("images/"+ "test")).thenReturn(ref);
        when(ref.putFile(Uri.parse("fakeUri"))).thenReturn(task);
        when(task.addOnSuccessListener(Storage.successListener)).thenReturn(successTask);
        when(successTask.addOnFailureListener(Storage.failureListener)).thenReturn(failureTask);
        when(failureTask.addOnProgressListener(Storage.progressListener)).thenReturn(null);

        Storage.getInstance();
        Storage.setStorageTest(firebaseStorage);

        if(Looper.myLooper() == null){
            Looper.prepare();
        }

    }


    @Test
    public void userCanUploadPicture(){
        mFragmentTestRule.launchActivity(null);
        try {
            Thread.sleep(500);

        }catch (Exception e){

        }

        String refStorage = Storage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"));
        assertEquals("test", refStorage);
        String f1 = FakeStorage.getInstance().uploadImage(storageReference, mFragmentTestRule.getFragment().getContext(), Uri.parse("fakeUri"));
        assertEquals("validRef", f1);
        ExecutionMode.getInstance().setInvalidAuthTest(true);
        String f2 = FakeStorage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"));
        assertEquals("invalidRef", f2);

        ExecutionMode.getInstance().setInvalidAuthTest(false);

    }

    @After
    public void after(){
        ExecutionMode.getInstance().setTest(false);
    }

}
