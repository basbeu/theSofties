package ch.epfl.sweng.favors.database.storage;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * A dispatcher for the FirebaseStorage depending on test mode or not
 */
public abstract class FirebaseStorageDispatcher {

    private static FirebaseStorageDispatcher storage = null;
    public final static int GET_FROM_GALLERY = 66;
    public final static int GET_FROM_CAMERA = 99;
    public final static int STORAGE_PERMISSION = 0;

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
    public abstract Task<Void> deleteImageFromStorage(ObservableField<String> pictureRef, String category);

    public static boolean checkStoragePath(String category){
        if(category == null){
            return false;
        }
        String cat = category.toLowerCase();
        if(!cat.equals("favor") && !cat.equals("profile")){
            return false;
        }

        return true;
    }

    public abstract Bitmap getPictureFromDevice(int requestCode, int resultCode, Intent data, Context context, ImageView view);
    public abstract void checkCameraPermission(Fragment fragment);
    public abstract void takePictureFromCamera(Fragment fragment);
}
