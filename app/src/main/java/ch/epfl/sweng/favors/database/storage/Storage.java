package ch.epfl.sweng.favors.database.storage;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import ch.epfl.sweng.favors.utils.Utils;

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

    public String uploadImage(StorageReference storageReference, Context context, Uri selectedImage, StorageCategories category) {


        if(category != null && selectedImage != null)
        {
            String path = category.toString()+"/";
            context_ext = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String storageRef = getStorageRef();
            StorageReference ref = storageReference.child(path + storageRef);
            Uri compressedImage;
            compressedImage = getCompressedImage(context, selectedImage);
            if(compressedImage != null){
                ref.putFile(compressedImage).addOnSuccessListener(successListener).addOnFailureListener(failureListener).addOnProgressListener(progressListener);
                return storageRef;
            }

        }

        return null;
    }

    /**
     * Helper method to return the storage reference based on if it is a test or not
     * @return the storage ref
     */
    private String getStorageRef(){
        return ExecutionMode.getInstance().isTest() ? "test" : UUID.randomUUID().toString();
    }

    /**
     * Helper method to get the compressed Uri image based on if it is a test or not
     * @param context actual context
     * @param selectedImage Uri to be compressed
     * @return compressed Uri
     */
    private Uri getCompressedImage(Context context, Uri selectedImage){
        return ExecutionMode.getInstance().isTest() ? selectedImage : Utils.compressImageUri(context, selectedImage);
    }

    @Override
    public void displayImage(ObservableField<String> pictureRef, ImageView imageView, StorageCategories category) {

        if(category != null && pictureRef != null && pictureRef.get() != null){
            String path = category.toString()+"/";
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(path+ pictureRef.get());
            view = imageView;
            ref.getBytes(MAX_BYTE_SIZE).addOnSuccessListener(byteSuccessListener);

        }
    }

    @Override
    public Task<Void> deleteImageFromStorage(ObservableField<String> pictureRef, StorageCategories category) {

        if(category != null && pictureRef != null && pictureRef.get() != null){
            String path = category.toString()+"/";
            StorageReference ref = getReference().child(path+pictureRef.get());
            return ref.delete();
        }
        return null;
    }

    @Override
    public Bitmap getPictureFromDevice(int requestCode, Intent data, Context context, ImageView view) {
        if (requestCode == GET_FROM_GALLERY) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                view.setImageBitmap(bitmap);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == GET_FROM_CAMERA) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            view.setImageBitmap(bitmap);
            return bitmap;
        }

        return null;
    }

    @Override
    public void checkCameraPermission(Fragment fragment) {
        if (permissionGranted(fragment, Manifest.permission.CAMERA)
                || permissionGranted(fragment, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            fragment.requestPermissions(new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION);
        }
        else takePictureFromCamera(fragment);
    }

    /**
     * Will check if a specific permission is gratend to the fragment
     * @param fragment current fragmetn to request permission in
     * @param permission Manifest constant that you wich to check permissions for.
     * @return true if permission is granted.
     */
    private boolean permissionGranted(Fragment fragment, String permission) {
        return ContextCompat.checkSelfPermission(fragment.getActivity(), permission) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void takePictureFromCamera(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(intent, FirebaseStorageDispatcher.GET_FROM_CAMERA);
        }
    }

    @Override
    public void takePictureFromGallery(Fragment fragment) {
        fragment.startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

}
