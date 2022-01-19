package pt.fcul.cm2021.grupo9.shotop.processoAdicionar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterListCheckBox;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;


public class VisionPhotoFragment extends Fragment {

    ArrayList<VisionResponse> listVR = new ArrayList<>();
    ListView listv;
    Bitmap bitmap;
    Spot spot;

    VisionPhotoFragment(Bitmap bm, Spot spot){
        this.bitmap = bm;
        this.spot = spot;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vision_photo, container, false);
        listv = v.findViewById(R.id.listView);
        AdapterListCheckBox adapter = new AdapterListCheckBox(listVR,getContext());
        listv.setAdapter(adapter);
        vision(listv);

        Button b = v.findViewById(R.id.submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null) {
                    String id = firebaseUser.getUid();
                    spot.setIdUser(id);
                }
                spot.setDesafio(false);
                submit();
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new MapaFragment())
                        .commit();
            }
        });

        final View view = v.findViewById(R.id.circle_three);
        view.setBackground(getResources().getDrawable(R.drawable.circle));

        return v;
    }


    public void vision(ListView lv){
        Thread t = new Thread(new Runnable() {
            public void run(){
                try {
                    Vision.Builder visionBuilder = new Vision.Builder(
                            new NetHttpTransport(),
                            new AndroidJsonFactory(),
                            null);

                    visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyAsSBLcKEQJvemQdocQ129gZcC0SM5MXyk"));

                    Vision vision = visionBuilder.build();


                    Feature desiredFeature = new Feature();
                    desiredFeature.setType("LABEL_DETECTION");



                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                    InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

                    byte[] photoData = IOUtils.toByteArray(inputStream);
                    inputStream.close();
                    Image inputImage = new Image();
                    inputImage.encodeContent(photoData);
                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputImage);
                    request.setFeatures(Arrays.asList(desiredFeature));

                    BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
                    batchRequest.setRequests(Arrays.asList(request));
                    BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();
                    List<FaceAnnotation> faces = batchResponse.getResponses().get(0).getFaceAnnotations();
                    String list = batchResponse.getResponses().get(0).get("labelAnnotations").toString();
                    JSONArray obj = new JSONArray(list);
                    listVR = new ArrayList<>();
                    for(int i = 0; i < obj.length(); i++){
                        JSONObject object = new JSONObject(obj.get(i).toString());
                        VisionResponse vr = new VisionResponse(object.get("description").toString(),
                                object.get("mid").toString(),
                                object.get("score").toString(),
                                object.get("topicality").toString());
                        listVR.add(vr);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
            ArrayList<String> listaCarac = new ArrayList<>();
            for(VisionResponse vr: listVR){
                listaCarac.add(vr.description);
            }
            spot.setCaracteristicas(listaCarac);
            AdapterListCheckBox adapter = new AdapterListCheckBox(listVR,getContext());
            lv.setAdapter(adapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void submit(){

        MainActivity.db.collection("Spot")
                .add(spot)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("SHOTOP", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("SHOTOP", "Error adding document", e);
                    }
                });
    }
}