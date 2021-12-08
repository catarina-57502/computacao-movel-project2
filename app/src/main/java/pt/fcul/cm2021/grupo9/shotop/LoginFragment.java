package pt.fcul.cm2021.grupo9.shotop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginFragment extends Fragment {

    public static final String TAG_MAPS = "MAPS";

    private FragmentTransaction fragmentTransaction;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(requireContext());

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        BottomNavigationView b = requireActivity().findViewById(R.id.bottom_navigation_view);
        b.setVisibility(View.GONE);

        ImageView login = v.findViewById(R.id.btn_facebook);
        login.setOnClickListener(view -> {

            LoginManager.getInstance().logInWithReadPermissions(requireActivity(),
                    Arrays.asList("email", "public_profile"));

            LoginManager.getInstance().registerCallback(
                    mCallbackManager, new FacebookCallback<LoginResult>() {

                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            handleFacebookAccessToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {}

                        @Override
                        public void onError(FacebookException error) {}
                    });
        });

        TextView tv = v.findViewById(R.id.register_link_text);
        tv.setOnClickListener(view -> {
            RegisterFragment rf =  new RegisterFragment();

            fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace((R.id.frameFragment), rf, MainActivity.TAG_REGISTO);
            fragmentTransaction.commit();
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(requireActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null)
        {
            Fragment fragment = getParentFragmentManager().findFragmentByTag(TAG_MAPS);
            if (fragment == null) {
                fragment = new MapaFragment();
            }
            fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameFragment, fragment, TAG_MAPS);
            fragmentTransaction.commit();
        }
        else
        {
            Toast.makeText(requireActivity(), "Please sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }
}