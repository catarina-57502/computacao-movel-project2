package pt.fcul.cm2021.grupo9.shotop.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.entidades.User;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final int RC_SIGN_IN_GOOGLE = 1;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    private final CollectionReference user = MainActivity.db.collection("User");

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        System.out.println(mGoogleSignInClient.getApiKey());
        // [END config_signin]

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(requireContext());

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.GONE);

        Button login = view.findViewById(R.id.btn_login);
        login.setOnClickListener(this);

        Button loginFacebook = view.findViewById(R.id.facebook_login_button);
        loginFacebook.setOnClickListener(this);

        Button loginGoogle = view.findViewById(R.id.google_login_button);
        loginGoogle.setOnClickListener(this);

        TextView register = view.findViewById(R.id.register_link_text);
        register.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        firebaseUser = mAuth.getCurrentUser();

        updateUI();
    }

    private void loginWithEmailAndPassword() {

        EditText etEmail = requireView().findViewById(R.id.et_email_login);
        EditText etPassword = requireView().findViewById(R.id.et_password_login);

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            firebaseUser = mAuth.getCurrentUser();

                            submit();
                        } else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidUserException e) {
                                Toast.makeText(requireActivity(), "User not registered!", Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(requireActivity(), "Invalid credentials!", Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Toast.makeText(requireActivity(), "Unexpected error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // If sign in fails, display a message to the user.
                        updateUI();
                    });
        }
        else
            Toast.makeText(requireActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
    }

    private void loginWithFacebook() {

        LoginManager.getInstance().logInWithReadPermissions(requireActivity(),
                Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(
                mCallbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // Empty body
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // Empty body
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if(task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        firebaseUser = mAuth.getCurrentUser();

                        submit();
                    }
                    // If sign in fails, display a message to the user.
                    updateUI();
                });
    }

    private void loginWithGoogle() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        System.out.println("login2");
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        System.out.println("login3333");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if(task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        firebaseUser = mAuth.getCurrentUser();

                        submit();
                    }
                    if(!task.isSuccessful()){
                        System.out.println(task.getException());
                    }
                    System.out.println("login");
                    System.out.println(task.getException());
                    // If sign in fails, display a message to the user.
                    updateUI();
                });
    }

    private void doRegister() {

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frameFragment, new RegisterFragment(), MainActivity.TAG_REGISTO)
                .commit();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_login)
            loginWithEmailAndPassword();
        else if(v.getId() == R.id.facebook_login_button)
            loginWithFacebook();
        else if(v.getId() == R.id.google_login_button)
            loginWithGoogle();
        else
            doRegister();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
               e.printStackTrace();
            }
        }
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void submit() {

        DocumentReference docRef = user.document(firebaseUser.getUid());

        docRef.get().addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("SHOTOP", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("SHOTOP", "No such document");
                    User newUser = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());
                    user.document(firebaseUser.getUid()).set(newUser);
                }
            } else {
                Log.d("SHOTOP", "get failed with ", task.getException());
            }
        });
    }

    private void updateUI() {

        if(firebaseUser != null)
        {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameFragment, new MapaFragment())
                    .commit();
        }
    }
}