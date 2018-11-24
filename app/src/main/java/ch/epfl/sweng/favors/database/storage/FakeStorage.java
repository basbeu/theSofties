package ch.epfl.sweng.favors.database.storage;

import android.content.Context;
import android.databinding.ObservableField;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.favors.utils.ExecutionMode;

public class FakeStorage extends FirebaseStorageDispatcher{

    private static FakeStorage storage = null;

    public static FirebaseStorageDispatcher getInstance(){
        if(storage == null){
            storage = new FakeStorage();
        }
        return storage;
    }

    @Override
    public StorageReference getReference() {
        return null;
    }

    @Override
    public String uploadImage(StorageReference storageReference, Context context, Uri selectedImage) {
        if(ExecutionMode.getInstance().isInvalidAuthTest()){
            Toast.makeText(context, "Failed to upload image, try again later", Toast.LENGTH_SHORT).show();
            return "invalidRef";
        }else{
            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
            return "validRef";
        }
    }

    @Override
    public void displayImage(ObservableField<String> pictureRef, ImageView imageView) {
        //TODO display fake image
    }
}
