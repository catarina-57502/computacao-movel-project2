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
import android.widget.SearchView;
import android.widget.TextView;

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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.MySpots.SpotInfoFragment;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.listeners.OnLocationChangedListener;
import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;


public class MapaFragment extends Fragment implements OnLocationChangedListener {

    ArrayList<Marker> myMarkers = new ArrayList<Marker>();
    MapView mMapView;
    SearchView searchView;
    TextView textview;
    private GoogleMap googleMap;
    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    public static List<Spot> allSpots = new ArrayList<>();
    static public LatLng lastLocation = new LatLng(38.757161150235916, -9.155208738614144);
    public static int count = 0;
    private static int locChang = 0;

    public ArrayList<Marker> markers = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("PotentialBehaviorOverride")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);

        FusedLocation.start(getContext());
        FusedLocation.addListener(this);
        getAllSpotsDB();

        FusedLocation.addListener(this);

        BottomNavigationView b = getActivity().findViewById(R.id.bottom_navigation_view);
        b.setVisibility(View.VISIBLE);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(lastLocation , 15) );
                        return false;
                    }
                });
                googleMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
                googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(lastLocation , 15) );
                if (googleMap != null) {
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            GeoPoint locCliked = new GeoPoint(marker.getPosition().latitude, marker.getPosition().longitude);
                            Spot spotClicked = null;
                            for (Spot s : allSpots) {
                                if (s.getLoc().equals(locCliked)) {
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
                markersOnMap();
            }
        });


        textview = rootView.findViewById(R.id.textView4);
        searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               if (searchView.getQuery().length() == 0) {
                    showAllMarkers();
                }
                return true;
            }

            public void callSearch(String query) {
                int resultingSpots = 0; //numero total de spots match
                for (Spot s : allSpots) {
                    int foundCaracteristica = 0; //para ver caso pesquisou nas caracteristicas desse spot, se encontrou ou nao
                    if(distanceKm(lastLocation.latitude,lastLocation.longitude, s.getLoc().getLatitude(),s.getLoc().getLongitude())<=5){ //raio 5 km
                        for(String c : s.getCaracteristicas()){
                            if (query.toUpperCase().compareTo(c.toUpperCase()) == 0) {
                                foundCaracteristica++;
                                resultingSpots++;
                                showMarker(s); //para o caso quando é feito uma pesquisa sem resultados (mapa sem markers) e depois é feita outra logo a seguir (sem apagar totalmente a searchview) mas que ja tem results
                                break;
                            }
                        }
                        if(foundCaracteristica==0){ //nenhuma das caracteristicas do spot corresponde a query,esconder
                            hideMarker(s);
                        }
                    }else{ //spot esta a mais de 5 km do curr locatin,esconder
                        hideMarker(s);
                    }
                }
                if(resultingSpots!=0){ //foi enonctrado spots, alterar zoom
                    googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(lastLocation , 12) );
                }else{
                    textview.setText("Num raio de 5km, não foram encontradas fotos com a tag \"" +query.toString() + "\"");
                    textview.setVisibility(View.VISIBLE);

                    textview.postDelayed(new Runnable() {
                        public void run() {
                            textview.setVisibility(View.INVISIBLE);
                        }
                    }, 2500);

                }
            }
        });

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


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onLocationChanged(LocationResult locationResult) {
        Location l = locationResult.getLastLocation();
        LatLng atualLocation = new LatLng(l.getLatitude(), l.getLongitude());
        // Caso sai da fcul
        if (l.getLatitude() != 38.757161150235916 && l.getLongitude() != -9.155208738614144 && locChang == 0) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atualLocation, 15));
            lastLocation = atualLocation;
            locChang++;
        }

        // Caso andei

        if (truncate(atualLocation.latitude,4) != truncate(lastLocation.latitude,4) && truncate(atualLocation.longitude,4) != truncate(lastLocation.longitude,4)) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atualLocation, 15));
            lastLocation = atualLocation;
            locChang++;
        }


    }

    double truncate(double number, int precision)
    {
        double prec = Math.pow(10, precision);
        int integerPart = (int) number;
        double fractionalPart = number - integerPart;
        fractionalPart *= prec;
        int fractPart = (int) fractionalPart;
        fractionalPart = (double) (integerPart) + (double) (fractPart)/prec;
        return fractionalPart;
    }

    public void getAllSpotsDB() {
        allSpots = new ArrayList<>();
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
                                String idUser = (String) document.getData().get("idUser");
                                boolean desafio = (boolean) document.getData().get("desafio");

                                Spot sp = new Spot(
                                        id,nome,loc,imagem,caracteristicas,idUser,
                                        desafio, imageHeight,imageWidth,model,dateTime,
                                        orientation,fNumber,exposureTime,focalLength,
                                        flash,iSOSpeedRatings,whiteBalanceMode,apertureValue,
                                        shutterSpeedValue,detectedFileTypeName,fileSize,brightnessValue,
                                        exposureBiasValue,maxApertureValue,digitalZoomRatio,contrast,saturation,sharpness
                                );
                                allSpots.add(sp);
                            }
                            markersOnMap();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void markersOnMap() {

        for(Spot s : allSpots){
            MarkerOptions markerOptions = new MarkerOptions();
            byte[] bytes = Base64.getDecoder().decode(s.getImagem());
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
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
                    position(new LatLng(lat, lng)).
                    anchor(iconGen.getAnchorU(), iconGen.getAnchorV());

            if (googleMap != null) {
                Marker newMarker = googleMap.addMarker(markerOptions);
                myMarkers.add(newMarker);
            }
        }


    }



    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private int distanceKm(double userLat, double userLng,
                                            double markerLat, double markerLng) {

        double latDistance = Math.toRadians(userLat - markerLat);
        double lngDistance = Math.toRadians(userLng - markerLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(markerLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }

    private void hideMarker(Spot s) {
        for (Marker m : myMarkers) {
            if (m.getPosition().latitude == s.getLoc().getLatitude() && m.getPosition().longitude == s.getLoc().getLongitude()) {
                m.setVisible(false);
            }

        }
    }

    private void showAllMarkers(){
        for (Spot s : allSpots) {
            for (Marker m : myMarkers) {
                if (s.getLoc().getLatitude() == m.getPosition().latitude && s.getLoc().getLongitude() == m.getPosition().longitude) {
                    if (!m.isVisible()) {
                        m.setVisible(true);
                    }
                }
            }
        }
    }

    private void showMarker(Spot s) {
        for (Marker m : myMarkers) {
            if (m.getPosition().latitude == s.getLoc().getLatitude() && m.getPosition().longitude == s.getLoc().getLongitude()) {
                if(!m.isVisible()){
                    m.setVisible(true);
                }

            }
        }
    }

}