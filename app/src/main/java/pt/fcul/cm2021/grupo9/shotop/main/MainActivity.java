package pt.fcul.cm2021.grupo9.shotop.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import pt.fcul.cm2021.grupo9.shotop.ListSpotsFragment;
import pt.fcul.cm2021.grupo9.shotop.camera.CameraFragment;
import pt.fcul.cm2021.grupo9.shotop.login.LoginFragment;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.MetaDataFragment;
import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.StartAddFragment;


public class MainActivity extends AppCompatActivity  {

    public static final String TAG_MAPS = "MAPS";
    public static final String TAG_LOGIN = "LOGIN";
    public static final String TAG_REGISTO = "REGISTO";

    private FirebaseAuth mAuth;
    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 121);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        onItemSelectedListener();

        if(savedInstanceState == null) {
            // Add the fragment to the 'frameFragment' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameFragment, new LoginFragment())
                    .commit();
        }

    }

    private void onItemSelectedListener() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if(id == R.id.logout) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                LoginManager.getInstance().logOut();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment,new LoginFragment())
                        .commit();
            }

            if(id == R.id.add) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new StartAddFragment())
                        .commit();
            }

            if(id == R.id.community) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new ListSpotsFragment())
                        .commit();
            }

            if(id == R.id.CameraOverlay) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment,new CameraFragment())
                        .commit();
            }

            if(id == R.id.map) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment,new MapaFragment())
                        .commit();
            }

            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameFragment);
        if(fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 98;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_STORAGE:{

            }

        }
    }


}