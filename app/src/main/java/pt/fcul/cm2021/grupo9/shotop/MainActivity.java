package pt.fcul.cm2021.grupo9.shotop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_MAPS = "MAPS";

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //#TODO podem alterar para o vosso fragmento ser o primeiro a abrir
        Fragment fragmentMaps = getSupportFragmentManager().findFragmentByTag(TAG_MAPS);
        if (fragmentMaps == null) {
            fragmentMaps = new MapaFragment();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment, fragmentMaps, TAG_MAPS);
        fragmentTransaction.commit();

    }
}