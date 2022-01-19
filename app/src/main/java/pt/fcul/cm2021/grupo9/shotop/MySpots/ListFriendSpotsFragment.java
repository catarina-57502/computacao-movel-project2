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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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


                        spotCollection.whereEqualTo("idUser", userID).get().addOnCompleteListener(
                                t -> {
                                    if(t.isSuccessful()) {
                                        if(t.getResult().isEmpty())
                                            System.out.println("Vazio");

                                        List<DocumentSnapshot> documents = t.getResult().getDocuments();
                                        spotList = new ArrayList<>();

                                        for (DocumentSnapshot dc : documents) {
                                            String id = dc.getId();
                                            String nome = (String) dc.getData().get("nome");
                                            String imagem = (String) dc.getData().get("imagem");
                                            String apertureValue = (String) dc.getData().get("apertureValue");
                                            String brightnessValue = (String) dc.getData().get("brightnessValue");
                                            String contrast = (String) dc.getData().get("contrast");
                                            String dateTime = (String) dc.getData().get("dateTime");
                                            String detectedFileTypeName = (String) dc.getData().get("detectedFileTypeName");
                                            String digitalZoomRatio = (String) dc.getData().get("digitalZoomRatio");
                                            String exposureBiasValue = (String) dc.getData().get("exposureBiasValue");
                                            String exposureTime = (String) dc.getData().get("exposureTime");
                                            String fNumber = (String) dc.getData().get("fNumber");
                                            String fileSize = (String) dc.getData().get("fileSize");
                                            String flash = (String) dc.getData().get("flash");
                                            String focalLength = (String) dc.getData().get("focalLength");
                                            String iSOSpeedRatings = (String) dc.getData().get("iSOSpeedRatings");
                                            String imageHeight = (String) dc.getData().get("imageHeight");
                                            String imageWidth = (String) dc.getData().get("imageWidth");
                                            GeoPoint loc = (GeoPoint) dc.getData().get("loc");
                                            String maxApertureValue = (String) dc.getData().get("maxApertureValue");
                                            String model = (String) dc.getData().get("model");
                                            String orientation = (String) dc.getData().get("orientation");
                                            String saturation = (String) dc.getData().get("saturation");
                                            String sharpness = (String) dc.getData().get("sharpness");
                                            String shutterSpeedValue = (String) dc.getData().get("shutterSpeedValue");
                                            String whiteBalanceMode = (String) dc.getData().get("whiteBalanceMode");
                                            ArrayList<String> caracteristicas = (ArrayList<String>) dc.getData().get("caracteristicas");
                                            String idUser = (String) dc.getData().get("idUser");
                                            boolean desafio = (boolean) dc.getData().get("desafio");

                                            Spot sp = new Spot(
                                                    id,nome,loc,imagem,caracteristicas,idUser,
                                                    desafio, imageHeight,imageWidth,model,dateTime,
                                                    orientation,fNumber,exposureTime,focalLength,
                                                    flash,iSOSpeedRatings,whiteBalanceMode,apertureValue,
                                                    shutterSpeedValue,detectedFileTypeName,fileSize,brightnessValue,
                                                    exposureBiasValue,maxApertureValue,digitalZoomRatio,contrast,saturation,sharpness
                                            );
                                            spotList.add(sp);
                                        }



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
