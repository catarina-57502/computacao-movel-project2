package pt.fcul.cm2021.grupo9.shotop.MySpots;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterFriendSpots;
import pt.fcul.cm2021.grupo9.shotop.databinding.FragmentListFriendSpotsBinding;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.entidades.User;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;

public class ListFriendSpotsFragment extends Fragment {

    private User user;
    private FragmentListFriendSpotsBinding binding;
    private AdapterFriendSpots adapterFriendSpots;
    private String userID;
    private List<Spot> spotList;

    private final CollectionReference userCollection = MainActivity.db.collection("User");
    private final CollectionReference spotCollection = MainActivity.db.collection("Spot");

    public ListFriendSpotsFragment(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentListFriendSpotsBinding.inflate(getLayoutInflater());
        binding.rvFriendSpots.setLayoutManager(new LinearLayoutManager(requireActivity()));

        userCollection.whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        userID = document.getId();
                        System.out.println(userID);

                        spotCollection.whereEqualTo("idUser", userID).get().addOnCompleteListener(
                                t -> {
                                    if(t.isSuccessful()) {
                                        if(t.getResult().isEmpty())
                                            System.out.println("Vazio");

                                        List<DocumentSnapshot> documents = t.getResult().getDocuments();
                                        spotList = new ArrayList<>();

                                        documents.forEach(doc ->
                                                spotList.add(
                                                        new Spot((String) doc.getData().get("nome"),
                                                                 (String) doc.getData().get("imagem"))));

                                        adapterFriendSpots = new AdapterFriendSpots(requireActivity());
                                        adapterFriendSpots.setSpots(spotList);

                                        binding.rvFriendSpots.setAdapter(adapterFriendSpots);
                                    } else {
                                        Log.d("SHOTOP", "Error getting documents: ", t.getException());
                                    }
                                }
                        );

                    } else {
                        Log.d("SHOTOP", "Error getting documents: ", task.getException());
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }
}
