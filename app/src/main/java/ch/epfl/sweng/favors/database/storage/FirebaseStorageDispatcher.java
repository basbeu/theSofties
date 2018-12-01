package ch.epfl.sweng.favors.database.storage;

import android.content.Context;
import android.databinding.ObservableField;
import android.net.Uri;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * A dispatcher for the FirebaseStorage depending on test mode or not
 */
public abstract class FirebaseStorageDispatcher {

    private static FirebaseStorageDispatcher storage = null;

    public static FirebaseStorageDispatcher getInstance() {
        if(storage != null){
            return storage;
        }
        if(ExecutionMode.getInstance().isTest()){
            storage = FakeStorage.getInstance();
        }else{
            storage = Storage.getInstance();
        }
        return storage;
    }

    public abstract StorageReference getReference();

    /**
     * Upload an image on firestore
     * Inspired from this tutorial : https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
     * @param storageReference the storage reference of the Firestoreplot
     * @param context actual context
     * @param selectedImage Uri corresponding to the picture to be uploaded
     * @param category the category of the picture (favor or profile)
     * @return null if selectedImage == null, the storage reference of the picture otherwise
     */
    public abstract String uploadImage(StorageReference storageReference, Context context, Uri selectedImage, String category);

    /**
     * Download the picture corresponding to pictureRef from firestore and displays it on imageView
     * @param pictureRef the reference of the picture to be downloaded
     * @param imageView the Imageview where the picture should be displayed
     * @param category the category of the picture to be displayed (favor or profile)
     */
    public abstract void displayImage(ObservableField<String> pictureRef, ImageView imageView, String category);

    /**
     * Delete an image on firestore
     * @param pictureRef the reference of the image to be deleted
     * @param category the category of the picture to be deleted (favor or profile)
     */
    public abstract void deleteImageFromStorage(ObservableField<String> pictureRef, String category);

    public static boolean checkStoragePath(String path){
        if(!path.equals("favor/") && !path.equals("profile/")){
            return false;
        }

        return true;
    }
}
