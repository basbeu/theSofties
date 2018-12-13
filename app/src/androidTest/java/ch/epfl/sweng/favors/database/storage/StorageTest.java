package ch.epfl.sweng.favors.database.storage;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;

import android.widget.ImageView;


import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


import ch.epfl.sweng.favors.favors.FavorCreateFragment;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
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

        String refStorage = Storage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), StorageCategories.FAVOR);
        //assertEquals("test", refStorage);
        String f1 = FakeStorage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), StorageCategories.PROFILE);
        //assertEquals("validRef", f1);
        ExecutionMode.getInstance().setInvalidAuthTest(true);
        String f2 = FakeStorage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), StorageCategories.PROFILE);
        assertEquals("invalidRef", f2);
//        String f3 = FakeStorage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), "test");
//        assertEquals(null, f3);


        ExecutionMode.getInstance().setInvalidAuthTest(false);
        Looper.myLooper().quitSafely();
    }

    @Test
    public void listenersBehaveAsExpected() throws InterruptedException {
        if(Looper.myLooper() == null){
            Looper.prepare();
        }

        mFragmentTestRule.launchActivity(null);
        Thread.sleep(500);

        Storage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), Uri.parse("fakeUri"), StorageCategories.FAVOR);
        Storage.getInstance().displayImage(new ObservableField<String>("test"), view, StorageCategories.FAVOR);
        Storage.successListener.onSuccess(taskSnapshot);
        Storage.failureListener.onFailure(new Exception());
        Storage.progressListener.onProgress(taskSnapshot);
        Storage.byteSuccessListener.onSuccess(b);
        Looper.myLooper().quitSafely();
    }

    @Test
    public void nullUriReturnsNullRef() {
        String refStorage = Storage.getInstance().uploadImage(storageReference, mFragmentTestRule.getActivity(), null, StorageCategories.PROFILE);
        assertNull(refStorage);
    }


    @Test
    public void imageCanBeDisplayed(){
        Storage.getInstance().displayImage(new ObservableField<String>("test"), view, StorageCategories.FAVOR);
    }

    @Test
    public void getReferenceReturnsCorretly(){
        StorageReference r = Storage.getInstance().getReference();
        assertEquals(storageReference, r);
    }

    @Test
    public void deleteReturnsNullWithWrongCategory(){
        Task<Void> result = Storage.getInstance().deleteImageFromStorage(new ObservableField<String>("test"), null);
        assertNull( result);
    }

    @Test
    public void deleteReturnsCorrectTask() throws InterruptedException {
        if(Looper.myLooper() == null){
            Looper.prepare();
        }
        mFragmentTestRule.launchActivity(null);
        Thread.sleep(500);

        Task<Void> result = Storage.getInstance().deleteImageFromStorage(new ObservableField<String>("test"), StorageCategories.FAVOR);
        assertEquals(deletTask, result);

        Looper.myLooper().quitSafely();
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
        Bitmap b1 = FirebaseStorageDispatcher.getInstance().getPictureFromDevice(Storage.GET_FROM_GALLERY, null, mFragmentTestRule.getActivity(), null);
        Bitmap b2 = FirebaseStorageDispatcher.getInstance().getPictureFromDevice(67, null, mFragmentTestRule.getActivity(), null);
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
        Bitmap b1 = Storage.getInstance().getPictureFromDevice(Storage.GET_FROM_GALLERY,  data, context, view);
        assertEquals(null, b1);
        Bitmap b2 = Storage.getInstance().getPictureFromDevice(Storage.GET_FROM_CAMERA, data, mFragmentTestRule.getActivity(), view);
        assertEquals(FakeStorage.bitmap, b2);

        Bitmap b3 = Storage.getInstance().getPictureFromDevice(3, data, mFragmentTestRule.getActivity(), view);
        assertEquals(null, b3);

        Looper.myLooper().quitSafely();
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
