package pt.fcul.cm2021.grupo9.shotop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;

import androidx.core.app.ActivityCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;

public class FusedLocation extends LocationCallback {
    Context context;
    private String TAG = "FUSEDLOCATION";
    private long TIME_BETWEEN_UPDATES = 20 * 1000L;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient client;

    FusedLocation(Context context) {
        this.context = context;
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(TIME_BETWEEN_UPDATES);
        client = new FusedLocationProviderClient(context);

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();

        LocationServices.getSettingsClient(context).checkLocationSettings(locationSettingsRequest);
    }

    public static ArrayList<OnLocationChangedListener> listener = new ArrayList<>();
    public static FusedLocation instance = null;


    public static void addListener(OnLocationChangedListener listenerAdd) {
        listener.add(listenerAdd);
    }


    public static void notifyListeners(LocationResult locationResult) {
        for (OnLocationChangedListener i : listener){
            i.onLocationChanged(locationResult);
        }
    }

    public static void start(Context context) {
        if (instance == null) {
            instance = new FusedLocation(context);
            instance.startLocationUpdates();
        } else {
            instance.startLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            return;
        }else{
            client.requestLocationUpdates(locationRequest,this, Looper.myLooper());
        }
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        System.out.println("onLocationResult");
        notifyListeners(locationResult);
        super.onLocationResult(locationResult);
    }

}
