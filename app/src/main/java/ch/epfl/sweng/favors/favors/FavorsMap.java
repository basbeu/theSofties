package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.FavorsMapBinding;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static ch.epfl.sweng.favors.utils.Utils.getIconNameFromCategory;

/**
 * Fragment that displays the list of favor and allows User to sort it and to search in it
 */
public class FavorsMap extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    private static final String TAG = "FAVORS_MAP";

    FavorsMapBinding binding;
    ObservableArrayList<Favor> favorList = new ObservableArrayList<>();

    private HashMap<String,Favor> favorsMap = new HashMap<>();

    private GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Favor favor = favorsMap.get(marker.getId());
            ViewModelProviders.of(FavorsMap.this.getActivity()).get(SharedViewFavor.class).select(favor);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mapView, new FavorDetailView()).addToBackStack(null).commit();

            return true;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.favors_map,container,false);
        binding.setElements(this);

        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.mapView.getMapAsync(this);

        return binding.getRoot();
    }

    private GoogleMap mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(markerClickListener);
        Log.d(TAG, "Map ready");

        FavorRequest.all(favorList, null,null);

        favorList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                //favorsMap = new HashMap<>();
                for(Favor favor:favorList){
                    GeoPoint point = (GeoPoint) favor.get(Favor.ObjectFields.location);
                    LatLng location = new LatLng(point.getLatitude(),point.getLongitude());

                    if(getContext() != null) {
                        Resources r = getResources();
                        int drawableId = r.getIdentifier(getIconNameFromCategory(favor.get(Favor.StringFields.category)), "drawable", "ch.epfl.sweng.favors");

                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(drawableId));

                        //TODO : try to do it in a more clean way
                        if (!ExecutionMode.getInstance().isTest()) {
                            //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carpooling);
                            //BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.carpooling));

//                        Resources r = getResources();
//                        int drawableId = r.getIdentifier(getIconNameFromCategory(favor.get(Favor.StringFields.category)), "drawable", "ch.epfl.sweng.favors");
//
//                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(drawableId));

                            Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(favor.get(Favor.StringFields.title)).icon(icon));
                            favorsMap.put(marker.getId(), favor);
                            Log.d(TAG, "new Marker : " + marker.getId());
                        }
                    }
                }
            }
        });
    }

    /**
     * Build a custom bitmap from a ressource id, found this solution in stackoverflow
     * https://stackoverflow.com/questions/14811579/how-to-create-a-custom-shaped-bitmap-marker-with-android-map-api-v2
     * @param resId
     * @return
     */
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = getLayoutInflater().inflate(R.layout.custom_marker_map, null);

        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);

        customMarkerView.draw(canvas);

        return returnedBitmap;
    }

}