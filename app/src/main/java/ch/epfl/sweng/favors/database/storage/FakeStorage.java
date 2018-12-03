package ch.epfl.sweng.favors.database.storage;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * This class simulates a FirebaseStorage for testing purposes
 */
public class FakeStorage extends FirebaseStorageDispatcher{

    private static FakeStorage storage = null;
    protected static Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ALPHA_8);

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
    public String uploadImage(StorageReference storageReference, Context context, Uri selectedImage, StorageCategories category) {

        if(category == null ||selectedImage == null) return null;

        if(ExecutionMode.getInstance().isInvalidAuthTest()){
            Toast.makeText(context, "Failed to upload image, try again later", Toast.LENGTH_SHORT).show();
            return "invalidRef";
        }else{
            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
            return "validRef";
        }
    }

    @Override
    public void displayImage(ObservableField<String> pictureRef, ImageView imageView, StorageCategories category) {
    }

    @Override
    public Task<Void> deleteImageFromStorage(ObservableField<String> pictureRef, StorageCategories category) {
        return Tasks.forResult(null);
    }

    @Override
    public Bitmap getPictureFromDevice(int requestCode, Intent data, Context context, ImageView view) {

        if(requestCode == GET_FROM_GALLERY || requestCode == GET_FROM_CAMERA){
            return bitmap;
        }
        return null;
    }

    @Override
    public void checkCameraPermission(Fragment fragment) {
        takePictureFromCamera(fragment);
    }

    @Override
    public void takePictureFromCamera(Fragment fragment) {
    }

    @Override
    public void takePictureFromGallery(Fragment fragment) {
    }

}
