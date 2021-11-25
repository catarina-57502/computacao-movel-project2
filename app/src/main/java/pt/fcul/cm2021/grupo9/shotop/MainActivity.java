package pt.fcul.cm2021.grupo9.shotop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_MAPS = "MAPS";
    public static final String TAG_LOGIN = "LOGIN";
    public static final String TAG_REGISTO = "REGISTO";

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize the bottom navigation view
        //create bottom navigation view object

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //#TODO podem alterar para o vosso fragmento ser o primeiro a abrir
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_LOGIN);
        if (fragment == null) {
            fragment = new LoginFragment();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment, fragment, TAG_LOGIN);
        fragmentTransaction.commit();


    }


}