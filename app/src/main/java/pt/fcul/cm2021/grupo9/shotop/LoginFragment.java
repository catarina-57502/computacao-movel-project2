package pt.fcul.cm2021.grupo9.shotop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class LoginFragment extends Fragment {

    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentManager fragmentManager = getFragmentManager();


        View v = inflater.inflate(R.layout.fragment_login, container, false);
        BottomNavigationView b = getActivity().findViewById(R.id.bottom_navigatin_view);
        b.setVisibility( View.GONE);

        TextView tv = (TextView) v.findViewById(R.id.register_link_text);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment rf =  new RegisterFragment();

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace((R.id.frameFragment), rf, MainActivity.TAG_REGISTO);
                fragmentTransaction.commit();
            }

        });


        return v;
    }


}