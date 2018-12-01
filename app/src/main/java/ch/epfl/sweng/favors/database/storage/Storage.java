package ch.epfl.sweng.favors.database.storage;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * Handle the real storage related to FirebaseStorage
 */
public class Storage extends FirebaseStorageDispatcher{

    private static FirebaseStorage firebaseStorage = null;
    private static  Storage storage = null;
    protected static final int MAX_BYTE_SIZE = 2160*2160;
    private static Context context_ext;
    private static ProgressDialog progressDialog;
    private static ImageView view;
    protected static Bitmap bmp;

    //The listener are declared protected so they can be used in the tests with Mockito

    protected static OnSuccessListener<UploadTask.TaskSnapshot> successListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            progressDialog.dismiss();
            Toast.makeText(context_ext, "Uploaded", Toast.LENGTH_SHORT).show();
        }
    };

    protected static OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            progressDialog.dismiss();
            Toast.makeText(context_ext, "Failed to upload image, try again later", Toast.LENGTH_SHORT).show();
        }
    };


    protected static OnProgressListener<UploadTask.TaskSnapshot> progressListener = new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                    .getTotalByteCount());
            progressDialog.setMessage("Uploaded "+(int)progress+"%");
        }
    };

    protected static OnSuccessListener<byte[]> byteSuccessListener = new OnSuccessListener<byte[]>() {
        @Override
        public void onSuccess(byte[] bytes) {
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            view.setImageBitmap(bmp);
        }};

    private Storage(){
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public static FirebaseStorageDispatcher getInstance(){
        if(storage == null){
            storage = new Storage();
        }

        return storage;
    }

    @Override
    public StorageReference getReference() {
        return firebaseStorage.getReference();
    }

    public static void setStorageTest(FirebaseStorage storage){
        firebaseStorage = storage;
    }

    public String uploadImage(StorageReference storageReference, Context context, Uri selectedImage, String category) {



        if(Storage.checkStoragePath(category) && selectedImage != null)
        {
            String path = category.toLowerCase()+"/";
            context_ext = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String storageRef = ExecutionMode.getInstance().isTest() ? "test" : UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(path + storageRef);
            ref.putFile(selectedImage).addOnSuccessListener(successListener).addOnFailureListener(failureListener).addOnProgressListener(progressListener);

            return storageRef;
        }

        return null;
    }

    @Override
    public void displayImage(ObservableField<String> pictureRef, ImageView imageView, String category) {

        if(Storage.checkStoragePath(category) && pictureRef != null && pictureRef.get() != null){
            String path = category.toLowerCase()+"/";
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(path+ pictureRef.get());
            view = imageView;
            ref.getBytes(MAX_BYTE_SIZE).addOnSuccessListener(byteSuccessListener);

        }
    }

    @Override
    public Task<Void> deleteImageFromStorage(ObservableField<String> pictureRef, String category) {

        if(Storage.checkStoragePath(category) && pictureRef != null && pictureRef.get() != null){
            String path = category.toLowerCase()+"/";
            StorageReference ref = getReference().child(path+pictureRef.get());
            return ref.delete();
        }
        return null;
    }

    @Override
    public Bitmap getPictureFromDevice(int requestCode, int resultCode, Intent data, Context context, ImageView view) {
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                view.setImageBitmap(bitmap);
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
