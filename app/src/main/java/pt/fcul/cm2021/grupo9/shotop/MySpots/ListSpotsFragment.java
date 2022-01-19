package pt.fcul.cm2021.grupo9.shotop.MySpots;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Base64;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterListCheckBox;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterSpot;
import pt.fcul.cm2021.grupo9.shotop.desafio.SpotInfoCriarDesafioFragment;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;


public class ListSpotsFragment extends Fragment {

    View v;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_list_spots, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            getMySpotsDB();
        }

        return v;
    }

    public void getMySpotsDB(){

        MainActivity.db.collection("Spot")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Spot> mySpots = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String nome = (String) document.getData().get("nome");
                                String imagem = (String) document.getData().get("imagem");
                                String apertureValue = (String) document.getData().get("apertureValue");
                                String brightnessValue = (String) document.getData().get("brightnessValue");
                                String contrast = (String) document.getData().get("contrast");
                                String dateTime = (String) document.getData().get("dateTime");
                                String detectedFileTypeName = (String) document.getData().get("detectedFileTypeName");
                                String digitalZoomRatio = (String) document.getData().get("digitalZoomRatio");
                                String exposureBiasValue = (String) document.getData().get("exposureBiasValue");
                                String exposureTime = (String) document.getData().get("exposureTime");
                                String fNumber = (String) document.getData().get("fNumber");
                                String fileSize = (String) document.getData().get("fileSize");
                                String flash = (String) document.getData().get("flash");
                                String focalLength = (String) document.getData().get("focalLength");
                                String iSOSpeedRatings = (String) document.getData().get("iSOSpeedRatings");
                                String imageHeight = (String) document.getData().get("imageHeight");
                                String imageWidth = (String) document.getData().get("imageWidth");
                                GeoPoint loc = (GeoPoint) document.getData().get("loc");
                                String maxApertureValue = (String) document.getData().get("maxApertureValue");
                                String model = (String) document.getData().get("model");
                                String orientation = (String) document.getData().get("orientation");
                                String saturation = (String) document.getData().get("saturation");
                                String sharpness = (String) document.getData().get("sharpness");
                                String shutterSpeedValue = (String) document.getData().get("shutterSpeedValue");
                                String whiteBalanceMode = (String) document.getData().get("whiteBalanceMode");
                                ArrayList<String> caracteristicas = (ArrayList<String>) document.getData().get("caracteristicas");
                                String idUser = (String) document.getData().get("idUser");
                                boolean desafio = (boolean) document.getData().get("desafio");

                                Spot sp = new Spot(
                                        id,nome,loc,imagem,caracteristicas,idUser,
                                        desafio, imageHeight,imageWidth,model,dateTime,
                                        orientation,fNumber,exposureTime,focalLength,
                                        flash,iSOSpeedRatings,whiteBalanceMode,apertureValue,
                                        shutterSpeedValue,detectedFileTypeName,fileSize,brightnessValue,
                                        exposureBiasValue,maxApertureValue,digitalZoomRatio,contrast,saturation,sharpness
                                );

                                if(sp.getIdUser()!=null && sp.getIdUser().equals(firebaseUser.getUid())){
                                    mySpots.add(sp);
                                }

                            }
                            ListView listv = v.findViewById(R.id.listViewSpots);
                            AdapterSpot adapter = new AdapterSpot(mySpots,getContext());
                            listv.setAdapter(adapter);
                            listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int
                                        position, long id) {

                                    Spot spotClicked = (Spot) listv.getItemAtPosition(position);

                                    for(Spot s: mySpots){
                                        if(s.getId().equals(spotClicked.getId())){
                                            getParentFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.frameFragment, new SpotInfoCriarDesafioFragment(spotClicked))
                                                    .commit();
                                        }
                                    }

                                }
                            });
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }});

    }

}