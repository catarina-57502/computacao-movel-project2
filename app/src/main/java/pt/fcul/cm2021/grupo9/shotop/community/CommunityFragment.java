package pt.fcul.cm2021.grupo9.shotop.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterUser;
import pt.fcul.cm2021.grupo9.shotop.databinding.FragmentCommunityBinding;
import pt.fcul.cm2021.grupo9.shotop.entidades.User;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;

public class CommunityFragment extends Fragment  {

    private FragmentCommunityBinding binding;
    private AdapterUser adapterUser;

    private User currentUser;
    private final CollectionReference user = MainActivity.db.collection("User");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        binding = FragmentCommunityBinding.inflate(getLayoutInflater());
        binding.rvCommunity.setLayoutManager(new LinearLayoutManager(requireActivity()));

        DocumentReference docRef = user.document(mAuth.getCurrentUser().getUid());

        docRef.get().addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("SHOTOP", "DocumentSnapshot data: " + document.getData());
                    currentUser = document.toObject(User.class);

                    adapterUser = new AdapterUser(requireActivity());
                    adapterUser.setUsers(currentUser.getAmigos());

                    binding.rvCommunity.setAdapter(adapterUser);

                } else {
                    Log.d("SHOTOP", "No such document");
                }
            } else {
                Log.d("SHOTOP", "get failed with ", task.getException());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = binding.getRoot();

        //SearchView searchView = view.findViewById(R.id.svCommunity);
        //searchView.setSubmitButtonEnabled(true);
        //searchView.setOnQueryTextListener(this);

        Button addFriend = view.findViewById(R.id.btn_add_friend);
        addFriend.setOnClickListener(af -> openDialog());

        //Button showSpot = view.findViewById(R.id.btn_show_spot);
        //showSpot.setOnClickListener(ss -> showSpot());
        return view;
    }

    public void openDialog() {
        AddFriendDialogFragment dialog = new AddFriendDialogFragment();
        dialog.show(getChildFragmentManager(), "Add Friend Dialog");
    }

    /*
    public void showSpot() {

    }
    */

    /*
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if(query != null)
            // Empty body

        return true;
    }
    */
}
