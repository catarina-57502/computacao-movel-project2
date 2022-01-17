package pt.fcul.cm2021.grupo9.shotop.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterUser;
import pt.fcul.cm2021.grupo9.shotop.databinding.FragmentCommunityBinding;
import pt.fcul.cm2021.grupo9.shotop.entidades.User;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;

public class CommunityFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentCommunityBinding binding;
    private AdapterUser adapterUser;

    private List<User> users;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentCommunityBinding.inflate(getLayoutInflater());
        binding.rvCommunity.setLayoutManager(new LinearLayoutManager(requireActivity()));

        adapterUser = new AdapterUser();

        binding.rvCommunity.setAdapter(adapterUser);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = binding.getRoot();

        SearchView searchView = view.findViewById(R.id.svCommunity);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if(query != null)
            searchUsersDB(query);

        return true;
    }

    private void searchUsersDB(String query) {
        MainActivity.db.collection("User")
                .whereEqualTo("email", query)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String nome = (String) document.getData().get("nome");
                            String email = (String) document.getData().get("email");
                            User user = new User(id, nome, email);
                            users.add(user);
                            Log.d("SHOTOP", document.getId() + " => " + document.getData());
                        }

                        adapterUser.setUsers(users);
                    } else {
                        Log.d("SHOTOP", "Error getting documents: ", task.getException());
                    }
                });
    }
}
