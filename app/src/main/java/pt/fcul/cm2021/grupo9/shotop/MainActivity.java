package pt.fcul.cm2021.grupo9.shotop;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity  {

    public static final String TAG_MAPS = "MAPS";
    public static final String TAG_LOGIN = "LOGIN";
    public static final String TAG_REGISTO = "REGISTO";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
                        .replace(R.id.frameFragment, new LoginFragment())
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
}