package ch.epfl.sweng.favors.database.storage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.FileNotFoundException;
import java.io.IOException;

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

public class StorageTest {

    @Rule public FragmentTestRule<FavorCreateFragment> mFragmentTestRule = new FragmentTestRule<FavorCreateFragment>(FavorCreateFragment.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private FirebaseStorage firebaseStorage;
    @Mock private StorageReference ref;
    @Mock private StorageReference storageReference;
    @Mock private UploadTask task;
    @Mock private StorageTask<UploadTask.TaskSnapshot> successTask;
    @Mock private StorageTask<UploadTask.TaskSnapshot> failureTask;
    @Mock private Task<Void> deletTask;
    @Mock private Task<byte[]> byteTask;
    @Mock private UploadTask.TaskSnapshot taskSnapshot;
    @Mock private ImageView view;
    @Mock private Intent data;
    @Mock private Context context;
    @Mock private ContentResolver contentResolver;
    @Mock private Fragment fakeFragment;

    private byte[] b = {};
    private Bundle bundle = new Bundle();


    @Before
    public void before(){
        ExecutionMode.getInstance().setTest(true);
        bundle.putParcelable("data", FakeStorage.bitmap);
        when(storageReference.child("favor/"+ "test")).thenReturn(ref);
        when(ref.putFile(Uri.parse("fakeUri"))).thenReturn(task);
        when(task.addOnSuccessListener(Storage.successListener)).thenReturn(successTask);
        when(successTask.addOnFailureListener(Storage.failureListener)).thenReturn(failureTask);
        when(failureTask.addOnProgressListener(Storage.progressListener)).thenReturn(null);
        when(ref.getBytes(Storage.MAX_BYTE_SIZE)).thenReturn(byteTask);
        when(byteTask.addOnSuccessListener(Storage.byteSuccessListener)).thenReturn(null);
        when(taskSnapshot.getBytesTransferred()).thenReturn((long)1);
        when(taskSnapshot.getTotalByteCount()).thenReturn((long)1);
        when(firebaseStorage.getReference()).thenReturn(storageReference);
        when(data.getData()).thenReturn(Uri.parse("fakeUri"));
        when(ref.delete()).thenReturn(deletTask);
        when(context.getContentResolver()).thenReturn(contentResolver);
        when(data.getExtras()).thenReturn(bundle);


        Storage.getInstance();
        Storage.setStorageTest(firebaseStorage);

    }

    @Test
    public void userCanUploadPicture(){
        if(Looper.myLooper() == null){
            Looper.prepare();
        }

        mFragmentTestRule.launchActivity(null);
        try {
            Thread.sleep(500);

        }catch (Exception e){

        }

        String refStorage = Storage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), "favor");
        assertEquals("test", refStorage);
        String f1 = FakeStorage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), "profile");
        assertEquals("validRef", f1);
        ExecutionMode.getInstance().setInvalidAuthTest(true);
        String f2 = FakeStorage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), "profile");
        assertEquals("invalidRef", f2);
        String f3 = FakeStorage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), "test");
        assertEquals(null, f3);


        ExecutionMode.getInstance().setInvalidAuthTest(false);
        Looper.myLooper().quitSafely();
    }

    @Test
    public void listenersBehaveAsExpected(){
        if(Looper.myLooper() == null){
            Looper.prepare();
        }

        mFragmentTestRule.launchActivity(null);
        try {
            Thread.sleep(500);

        }catch (Exception e){

        }
        Storage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), "favor");
        Storage.getInstance().displayImage(new ObservableField<String>("test"), view, "favor");
        Storage.successListener.onSuccess(taskSnapshot);
        Storage.failureListener.onFailure(new Exception());
        Storage.progressListener.onProgress(taskSnapshot);
        Storage.byteSuccessListener.onSuccess(b);
        Looper.myLooper().quitSafely();
    }

    @Test
    public void nullUriReturnsNullRef(){
        String refStorage = Storage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), null, "profile");
        assertEquals(null, refStorage);
    }

    @Test
    public void imageCanBeDisplayed(){
        Storage.getInstance().displayImage(new ObservableField<String>("test"), view, "test");
    }

    @Test
    public void getReferenceReturnsCorretly(){
        StorageReference r = Storage.getInstance().getReference();
        assertEquals(storageReference, r);
    }

    @Test
    public void deleteReturnsNullWithWrongCategory(){
        Task<Void> result = Storage.getInstance().deleteImageFromStorage(new ObservableField<String>("test"), null);
        assertEquals(null, result);
    }

    @Test
    public void deleteReturnsCorrectTask(){
        if(Looper.myLooper() == null){
            Looper.prepare();
        }
        mFragmentTestRule.launchActivity(null);
        try {
            Thread.sleep(500);

        }catch (Exception e){

        }
        Task<Void> result = Storage.getInstance().deleteImageFromStorage(new ObservableField<String>("test"), "favor");
        assertEquals(deletTask, result);

        Looper.myLooper().quitSafely();
    }

    @Test
    public void checkCategoryTest(){
        assertEquals(true, FirebaseStorageDispatcher.checkStoragePath("profile"));
        assertEquals(true, FirebaseStorageDispatcher.checkStoragePath("pRofIle"));
        assertEquals(true, FirebaseStorageDispatcher.checkStoragePath("favor"));
        assertEquals(true, FirebaseStorageDispatcher.checkStoragePath("FAVOR"));
        assertEquals(false, FirebaseStorageDispatcher.checkStoragePath("favors"));
        assertEquals(false, FirebaseStorageDispatcher.checkStoragePath("profiles"));
        assertEquals(false, FirebaseStorageDispatcher.checkStoragePath(null));
    }

    @Test
    public void getPictureFromDeviceFake(){
        if(Looper.myLooper() == null){
            Looper.prepare();
        }

        mFragmentTestRule.launchActivity(null);
        try {
            Thread.sleep(500);

        }catch (Exception e){

        }
        Bitmap b1 = FirebaseStorageDispatcher.getInstance().getPictureFromDevice(Storage.GET_FROM_GALLERY, -1, null, mFragmentTestRule.getActivity(), null);
        Bitmap b2 = FirebaseStorageDispatcher.getInstance().getPictureFromDevice(67, -1, null, mFragmentTestRule.getActivity(), null);
        assertEquals(FakeStorage.bitmap, b1);
        assertEquals(null, b2);

        Looper.myLooper().quitSafely();

    }

    @Test
    public void getPictureFromDeviceReal(){
        if(Looper.myLooper() == null){
            Looper.prepare();
        }

        mFragmentTestRule.launchActivity(null);
        try {
            Thread.sleep(500);

        }catch (Exception e){

        }
        Bitmap b1 = Storage.getInstance().getPictureFromDevice(Storage.GET_FROM_GALLERY, -1, data, context, view);
        assertEquals(null, b1);
        Bitmap b2 = Storage.getInstance().getPictureFromDevice(Storage.GET_FROM_CAMERA, -1, data, mFragmentTestRule.getActivity(), view);
        assertEquals(FakeStorage.bitmap, b2);

        Bitmap b3 = Storage.getInstance().getPictureFromDevice(3, -1, data, mFragmentTestRule.getActivity(), view);
        assertEquals(null, b3);

        Looper.myLooper().quitSafely();
    }

    @Ignore
    @Test
    public void takePictureFromCameraCanBeCalled(){
        if(Looper.myLooper() == null){
            Looper.prepare();
        }
        mFragmentTestRule.launchActivity(new Intent());
        try {
            Thread.sleep(500);

        }catch (Exception e){

        }
        Storage.getInstance().checkCameraPermission(fakeFragment);
        Looper.myLooper().quitSafely();
        //Storage.getInstance().takePictureFromCamera(fakeFragment);

    }

    @Test
    public void takePictureFromGalleryCanBeCalled(){
        Storage.getInstance().takePictureFromGallery(fakeFragment);
    }


    @After
    public void after(){
        ExecutionMode.getInstance().setTest(false);
    }

}
