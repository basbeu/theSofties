package ch.epfl.sweng.favors.database.storage;

import android.content.Context;
import android.net.Uri;

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

    public StorageReference getReference(){
        return storage.getReference();
    }

    public abstract String uploadImage(StorageReference storageReference, Context context, Uri selectedImage);
}
