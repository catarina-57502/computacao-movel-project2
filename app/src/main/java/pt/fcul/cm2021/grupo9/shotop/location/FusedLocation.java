package pt.fcul.cm2021.grupo9.shotop.location;

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

import pt.fcul.cm2021.grupo9.shotop.listeners.OnLocationChangedListener;

public class FusedLocation extends LocationCallback {
    Context context;
    private String TAG = "FUSEDLOCATION";
    //TODO confirmar isto
    private long TIME_BETWEEN_UPDATES = 1 * 1000L;
    static int i = 0;
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
        notifyListeners(locationResult);
        i++;
        if(i < 5){
            locationRequest.setInterval(20 * 1000L);
        }
        super.onLocationResult(locationResult);
    }

}
