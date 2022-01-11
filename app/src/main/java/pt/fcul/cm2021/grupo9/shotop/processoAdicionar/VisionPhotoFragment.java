package pt.fcul.cm2021.grupo9.shotop.processoAdicionar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.graphics.Bitmap;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterListCheckBox;


public class VisionPhotoFragment extends Fragment {

    ArrayList<VisionResponse> listVR = new ArrayList<>();
    ListView lv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vision_photo, container, false);
        lv = v.findViewById(R.id.listView);
        AdapterListCheckBox adapter = new AdapterListCheckBox(listVR,getContext());
        lv.setAdapter(adapter);
        return v;
    }


    public void vision(){
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
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
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
            lv = getView().findViewById(R.id.listView);
            AdapterListCheckBox adapter = new AdapterListCheckBox(listVR,getContext());
            lv.setAdapter(adapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}