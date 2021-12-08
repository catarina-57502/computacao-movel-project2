package pt.fcul.cm2021.grupo9.shotop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_MAPS = "MAPS";
    public static final String TAG_LOGIN = "LOGIN";
    public static final String TAG_REGISTO = "REGISTO";

    private FirebaseAuth mAuth;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize the bottom navigation view
        //create bottom navigation view object

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //#TODO podem alterar para o vosso fragmento ser o primeiro a abrir
        doFragmentTransaction(TAG_LOGIN);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(navListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment = getSupportFragmentManager().findFragmentByTag(TAG_LOGIN);
        if(fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Handle item selection
                    int itemId = item.getItemId();
                    if (itemId == R.id.logout) {
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        LoginManager.getInstance().logOut();
                        doFragmentTransaction(TAG_LOGIN);
                    }
                    return true;
                }
            };

    private void doFragmentTransaction(String tag) {
        fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new LoginFragment();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment, fragment, tag);
        fragmentTransaction.commit();
    }
}