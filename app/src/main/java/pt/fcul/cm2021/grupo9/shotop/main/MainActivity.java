package pt.fcul.cm2021.grupo9.shotop.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;


import pt.fcul.cm2021.grupo9.shotop.community.AddFriendDialogFragment;
import pt.fcul.cm2021.grupo9.shotop.MySpots.ListSpotsFragment;
import pt.fcul.cm2021.grupo9.shotop.community.CommunityFragment;
import pt.fcul.cm2021.grupo9.shotop.entidades.User;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.MySpots.ListSpotsFragment;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterSpot;
import pt.fcul.cm2021.grupo9.shotop.camera.CameraFragment;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.listeners.OnLocationChangedListener;
import pt.fcul.cm2021.grupo9.shotop.location.FusedLocation;

import pt.fcul.cm2021.grupo9.shotop.login.LoginFragment;
import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.StartAddFragment;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.VisionPhotoFragment;


public class MainActivity extends AppCompatActivity implements AddFriendDialogFragment.AddFriendDialogFragmentListener  {


    public static final String TAG_MAPS = "MAPS";
    public static final String TAG_LOGIN = "LOGIN";
    public static final String TAG_REGISTO = "REGISTO";

    private FirebaseAuth mAuth;
    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<Spot> spots = new ArrayList<Spot>();
    public static ArrayList<User> users = new ArrayList<>();


    public static Spot spotOriginal;
    public static Spot spotParticipacao;



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

        if (savedInstanceState == null) {
            // Add the fragment to the 'frameFragment' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameFragment, new LoginFragment())
                    .commit();
        }
        getAllSpotsDB();
        getAllUsersDB();

    }

    private void onItemSelectedListener() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.logout) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                LoginManager.getInstance().logOut();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new LoginFragment())
                        .commit();
            }

            if (id == R.id.add) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new StartAddFragment())
                        .commit();
            }


            if(id == R.id.community) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new CommunityFragment())
                        .commit();

           }

            if (id == R.id.CameraOverlay) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new ListSpotsFragment())
                        .commit();
            }

            if (id == R.id.map) {
                MapaFragment.count = 0;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new MapaFragment())
                        .commit();
            }

            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameFragment);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
            System.out.println("fragmentttttttttttt");
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 121;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                } else {

                }
                return;
            }
        }
    }


    @Override
    public void addFriend(String friend) {

        Query query = db.collection("User").whereEqualTo("email", friend);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean friendExists = !task.getResult().isEmpty();

                if(friendExists) {

                    DocumentSnapshot fDocument = task.getResult().getDocuments().get(0);
                    User friendUser = fDocument.toObject(User.class);

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    DocumentReference docRef = db.collection("User").document(firebaseUser.getUid());

                    docRef.update("amigos", FieldValue.arrayUnion(friendUser));
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameFragment, new CommunityFragment())
                            .commit();

                }
                else
                    Toast.makeText(this, "User doesn't exist!", Toast.LENGTH_SHORT).show();

            }  else {
                Log.d("SHOTOP", "Error getting documents: ", task.getException());
            }
        });
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
                                spots.add(sp);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    public void getAllUsersDB () {
        users = new ArrayList<>();
        MainActivity.db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String nome = (String) document.getData().get("nome");
                                String email = (String) document.getData().get("email");
                                User user = new User(id,nome,email);
                                users.add(user);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static User getUserFromID(String id) {
        for (User us : users) {
            if (us.getIdNoBD().equals(id)) {
                return us;
            }
        }
        return new User("id","NA","NA");
    }
}




