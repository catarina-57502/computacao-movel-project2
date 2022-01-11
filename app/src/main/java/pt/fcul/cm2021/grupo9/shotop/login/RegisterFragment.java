package pt.fcul.cm2021.grupo9.shotop.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.GONE);

        Button register = view.findViewById(R.id.buttonRegistar);
        register.setOnClickListener(l -> {

            EditText etEmail = view.findViewById(R.id.et_email);
            EditText etPassword = view.findViewById(R.id.et_password);
            EditText etConfirmPassword = view.findViewById(R.id.et_password2);

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if(email.isEmpty() || password.isEmpty())
                Toast.makeText(requireActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
            else if(!password.equals(confirmPassword))
                Toast.makeText(requireActivity(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
            else
                createAccount(email, password);
        });

        return view;
    }

    private void createAccount(String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthUserCollisionException e) {
                            Toast.makeText(requireActivity(), "Email already registered!", Toast.LENGTH_SHORT).show();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(requireActivity(), "Password is weak (less than 6 characters)!", Toast.LENGTH_SHORT).show();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(requireActivity(), "Email invalid!", Toast.LENGTH_SHORT).show();
                        } catch(Exception e) {
                            Toast.makeText(requireActivity(), "Unexpected error!", Toast.LENGTH_SHORT).show();
                        }
                        // If sign in fails, display a message to the user.
                        updateUI(null);
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {

        if(user != null)
        {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameFragment, new MapaFragment())
                    .commit();
        }
    }
}