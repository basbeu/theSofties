package ch.epfl.sweng.favors.database.storage;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class Storage extends FirebaseStorageDispatcher{

    private static FirebaseStorage firebaseStorage = null;
    private static  Storage storage = null;

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

    public String uploadImage(StorageReference storageReference, Context context, Uri selectedImage) {

        if(selectedImage != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String storageRef = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ storageRef);
            ref.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed to upload image, try again later", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

            return storageRef;
        }

        return null;
    }
}