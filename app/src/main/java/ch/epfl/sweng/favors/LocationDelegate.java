package ch.epfl.sweng.favors;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

public class LocationDelegate implements ActivityCompat.PermissionCompatDelegate {
    @Override
    public boolean requestPermissions(@NonNull Activity activity, @NonNull String[] strings, int i) {
        return true;
    }

    @Override
    public boolean onActivityResult(@NonNull Activity activity, int i, int i1, @Nullable Intent intent) {
        return true;
    }
}
