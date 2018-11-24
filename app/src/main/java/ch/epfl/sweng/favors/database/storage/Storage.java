package ch.epfl.sweng.favors.database.storage;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import ch.epfl.sweng.favors.utils.ExecutionMode;


public class Storage extends FirebaseStorageDispatcher{

    private static FirebaseStorage firebaseStorage = null;
    private static  Storage storage = null;
    protected static final int MAX_BYTE_SIZE = 2160*2160;
    private static Context context_ext;
    private static ProgressDialog progressDialog;
    private static ImageView view;

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
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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

    public String uploadImage(StorageReference storageReference, Context context, Uri selectedImage) {

        if(selectedImage != null)
        {
            context_ext = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String storageRef = ExecutionMode.getInstance().isTest() ? "test" : UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ storageRef);
            ref.putFile(selectedImage).addOnSuccessListener(successListener).addOnFailureListener(failureListener).addOnProgressListener(progressListener);

            return storageRef;
        }

        return null;
    }

    @Override
    public void displayImage(ObservableField<String> pictureRef, ImageView imageView) {
        if(pictureRef != null && pictureRef.get() != null){
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/"+ pictureRef.get());
            view = imageView;
            ref.getBytes(MAX_BYTE_SIZE).addOnSuccessListener(byteSuccessListener);

        }
    }

}
