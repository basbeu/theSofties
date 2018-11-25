package ch.epfl.sweng.favors.database.storage;

import android.content.Context;
import android.databinding.ObservableField;
import android.net.Uri;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.favors.utils.ExecutionMode;

public abstract class FirebaseStorageDispatcher {

    private static FirebaseStorageDispatcher storage = null;

    public static FirebaseStorageDispatcher getInstance() {
        if(storage == null){
            if(ExecutionMode.getInstance().isTest()){
                storage = FakeStorage.getInstance();
            }else{
                storage = Storage.getInstance();
            }
        }
        return storage;
    }

    public abstract StorageReference getReference();

    /**
     *
     * @param storageReference
     * @param context
     * @param selectedImage
     * @return
     */
    public abstract String uploadImage(StorageReference storageReference, Context context, Uri selectedImage);

    /**
     *
     * @param pictureRef
     * @param imageView
     */
    public abstract void displayImage(ObservableField<String> pictureRef, ImageView imageView);
}
