package ch.epfl.sweng.favors.database.storage;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
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
    public abstract String uploadImage(StorageReference storageReference, Context context, Uri selectedImage, StorageCategories category);

    /**
     * Download the picture corresponding to pictureRef from firestore and displays it on imageView
     * @param pictureRef the reference of the picture to be downloaded
     * @param imageView the Imageview where the picture should be displayed
     * @param category the category of the picture to be displayed (favor or profile)
     */
    public abstract void displayImage(ObservableField<String> pictureRef, ImageView imageView, StorageCategories category);

    /**
     * Delete an image on firestore
     * @param pictureRef the reference of the image to be deleted
     * @param category the category of the picture to be deleted (favor or profile)
     */
    public abstract Task<Void> deleteImageFromStorage(ObservableField<String> pictureRef, StorageCategories category);

    /**
     * Extracts an image from data and displays it
     * Inspired from : https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
     * and : https://androidkennel.org/android-camera-access-tutorial/
     * @param requestCode 66 if it's an image from the gallery, 99 if from the camera
     * @param data the intent containing the image
     * @param context the context from which the method has been called
     * @param view the view on which the image should be displayed
     * @return the image Bitmap or null if the requestCode was not valid
     */
    public abstract Bitmap getPictureFromDevice(int requestCode, Intent data, Context context, ImageView view);

    /**
     * Check if the device has already granted permission for camera and storage
     * Ask for permission if not already granted, open the camera by calling takePictureFromCamera otherwise
     * @param fragment the fragment that should ask for the permission
     */
    public abstract void checkCameraPermission(Fragment fragment);

    /**
     * Open the camera of the device to take a picture
     * Once it's done, onActivityResult from fragment will be called
     * @param fragment the fragment that should call startActivityForResult
     */
    public abstract void takePictureFromCamera(Fragment fragment);

    /**
     * Open the gallery of the device to select a picture
     * Once it's done, onActivityResult from fragment will be called
     * @param fragment the fragment that should call startActivityForResult
     */
    public abstract void takePictureFromGallery(Fragment fragment);
}
