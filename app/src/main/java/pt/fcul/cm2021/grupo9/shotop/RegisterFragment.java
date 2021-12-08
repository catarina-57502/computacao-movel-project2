package pt.fcul.cm2021.grupo9.shotop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class RegisterFragment extends Fragment {

    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_register, container, false);
        FragmentManager fragmentManager = getFragmentManager();
        BottomNavigationView b = getActivity().findViewById(R.id.bottom_navigation_view);
        b.setVisibility( View.GONE);

        Button btn = (Button) v.findViewById(R.id.buttonRegistar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapaFragment mf =  new MapaFragment();

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace((R.id.frameFragment), mf, MainActivity.TAG_MAPS);
                fragmentTransaction.commit();
            }

        });

        return  v;
    }
}