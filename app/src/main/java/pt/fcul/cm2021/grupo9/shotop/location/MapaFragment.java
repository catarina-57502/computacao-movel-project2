package pt.fcul.cm2021.grupo9.shotop.location;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.MySpots.SpotInfoFragment;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.listeners.OnLocationChangedListener;
import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;


public class MapaFragment extends Fragment implements OnLocationChangedListener {

    MapView mMapView;
    private GoogleMap googleMap;
    FusedLocation fl;
    public static List<Spot> allSpots = new ArrayList<>();

    static public LatLng lastLocation;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fl = new FusedLocation(getContext());
        FusedLocation.addListener(this);
        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);

        BottomNavigationView b = getActivity().findViewById(R.id.bottom_navigation_view);
        b.setVisibility( View.VISIBLE);


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getAllSpotsDB();

        return rootView;
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("LOCALIZAÇÃO")
                        .setMessage("LOCALIZAÇÃO")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onLocationChanged(LocationResult locationResult) {

        MarkerOptions markerOptions = new MarkerOptions();
        Location l = locationResult.getLastLocation();
        LatLng atual = new LatLng(l.getLatitude(), l.getLongitude());
        lastLocation = atual;

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
                googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(atual , 15.0f) );
            }
        });


        if (allSpots.size() != 0) {

            for (Spot s : allSpots) {

                byte[] bytes = Base64.getDecoder().decode(s.getImagem());
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap bitmap = getResizedBitmap(bm, 200);

                ImageView mImageView = new ImageView(getApplicationContext());
                IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
                mIconGenerator.setContentView(mImageView);
                mImageView.setImageBitmap(bitmap);
                Bitmap iconBitmap = mIconGenerator.makeIcon();
                IconGenerator iconGen = new IconGenerator(getApplicationContext());

                double lat = s.getLoc().getLatitude();
                double lng = s.getLoc().getLongitude();

                markerOptions.
                        icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)).
                        position(new LatLng(lat,lng)).
                        anchor(iconGen.getAnchorU(), iconGen.getAnchorV());

                    if (googleMap != null) {
                        googleMap.addMarker(markerOptions);
                    }
            }


            assert googleMap != null;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atual, 15.0f));



            assert googleMap != null;
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    GeoPoint locCliked = new GeoPoint(marker.getPosition().latitude, marker.getPosition().longitude);
                    Spot spotClicked = null;
                    for(Spot s: allSpots){
                        if(s.getLoc().equals(locCliked)){
                            spotClicked = s;
                            break;
                        }
                    }
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameFragment, new SpotInfoFragment(spotClicked))
                            .commit();
                    return false;
                }
            });


        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void getAllSpotsDB () {
        MainActivity.db.collection("Spot")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String nome = (String) document.getData().get("nome");
                                String imagem = (String) document.getData().get("imagem");
                                String apertureValue = (String) document.getData().get("apertureValue");
                                String brightnessValue = (String) document.getData().get("brightnessValue");
                                String contrast = (String) document.getData().get("contrast");
                                String dateTime = (String) document.getData().get("dateTime");
                                String detectedFileTypeName = (String) document.getData().get("detectedFileTypeName");
                                String digitalZoomRatio = (String) document.getData().get("digitalZoomRatio");
                                String exposureBiasValue = (String) document.getData().get("exposureBiasValue");
                                String exposureTime = (String) document.getData().get("exposureTime");
                                String fNumber = (String) document.getData().get("fNumber");
                                String fileSize = (String) document.getData().get("fileSize");
                                String flash = (String) document.getData().get("flash");
                                String focalLength = (String) document.getData().get("focalLength");
                                String iSOSpeedRatings = (String) document.getData().get("iSOSpeedRatings");
                                String imageHeight = (String) document.getData().get("imageHeight");
                                String imageWidth = (String) document.getData().get("imageWidth");
                                GeoPoint loc = (GeoPoint) document.getData().get("loc");
                                String maxApertureValue = (String) document.getData().get("maxApertureValue");
                                String model = (String) document.getData().get("model");
                                String orientation = (String) document.getData().get("orientation");
                                String saturation = (String) document.getData().get("saturation");
                                String sharpness = (String) document.getData().get("sharpness");
                                String shutterSpeedValue = (String) document.getData().get("shutterSpeedValue");
                                String whiteBalanceMode = (String) document.getData().get("whiteBalanceMode");
                                ArrayList<String> caracteristicas = (ArrayList<String>) document.getData().get("caracteristicas");
                                Spot sp = new Spot(
                                        id, nome, loc, imagem, caracteristicas,
                                        imageHeight, imageWidth, model, dateTime,
                                        orientation, fNumber, exposureTime, focalLength,
                                        flash, iSOSpeedRatings, whiteBalanceMode, apertureValue,
                                        shutterSpeedValue, detectedFileTypeName, fileSize, brightnessValue,
                                        exposureBiasValue, maxApertureValue, digitalZoomRatio, contrast, saturation, sharpness
                                );
                                allSpots.add(sp);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}