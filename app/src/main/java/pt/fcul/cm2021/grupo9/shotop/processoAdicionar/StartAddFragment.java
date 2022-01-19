package pt.fcul.cm2021.grupo9.shotop.processoAdicionar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.listeners.OnLocationChangedListener;
import pt.fcul.cm2021.grupo9.shotop.location.FusedLocation;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;


public class StartAddFragment extends Fragment  {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    Button button;
    ImageView imageView;
    Bitmap bitmap;
    String timeStamp;
    Uri photoURI;
    MapView mMapView;
    private GoogleMap googleMap;
    String nomeFoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start_add, container, false);
        button = (Button) v.findViewById(R.id.photo);
        imageView = (ImageView) v.findViewById(R.id.photoview);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText etNomeFoto= v.findViewById(R.id.et_namePhoto);

                nomeFoto = etNomeFoto.getText().toString();

                if(nomeFoto.isEmpty()){
                    Toast.makeText(getActivity(), "Nome vazio!", Toast.LENGTH_SHORT).show();
                }else {
                    dispatchTakePictureIntent();
                }
            }
        });


        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

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
                LatLng atual;
                atual = MapaFragment.lastLocation;
                if(atual == null){
                    atual = new LatLng(38.756977088908094,  -9.155466230678432);
                }
                googleMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
                googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(atual , 15.0f) );
                googleMap.addMarker(new MarkerOptions()
                        .position(atual)
                        .title("ATUAL"));
            }
        });

        final View view = v.findViewById(R.id.circle_one);
        view.setBackground(getResources().getDrawable(R.drawable.circle));

        return v;
    }


    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(getContext(),
                    "pt.fcul.cm2021.grupo9.shotop.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }

    }

    public static String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Spot spot = new Spot(nomeFoto,new GeoPoint(MapaFragment.lastLocation.latitude,MapaFragment.lastLocation.longitude));
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameFragment, new CheckPhotoFragment(bitmap,currentPhotoPath,spot))
                    .commit();
        }

    }
}