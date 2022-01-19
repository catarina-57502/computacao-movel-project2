package pt.fcul.cm2021.grupo9.shotop.desafio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ListView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
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
import com.google.firebase.firestore.GeoPoint;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterListCheckBox;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.StartAddFragment;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.VisionResponse;

public class SpotTools {

    // Vision
    static Bitmap bitmap;
    // Vision uso
    static ArrayList<VisionResponse> listVR = new ArrayList<>();


    //spots para popular
    static Spot spot;

    //Metadados
    static String currentPhotoPath;



    public static Spot runAll(String currentPhotoPath){
        spot = new Spot("Partipação", new GeoPoint(0,0));

        SpotTools.currentPhotoPath = currentPhotoPath;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        int scaleFactor = 1;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);


        getDados();
        vision();
        return spot;
    }

    public static void vision(){
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static void getDados(){
        if(currentPhotoPath != null){
            String selectedImagePath = currentPhotoPath;;
            try {
                File jpegFile = new File(selectedImagePath);
                Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        checkCategoria(tag.getTagName(),tag.getDescription());
                    }
                }
            } catch (ImageProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkCategoria(String ctg, String value){
        int number = 1;
        int number2 = 0;
        switch (ctg){
            case "Exif Image Height":
                spot.setImageHeight(value);
                break;
            case "Exif Image Width":
                spot.setImageWidth(value);
                break;
            case "Model":
                spot.setModel(value);
                break;
            case "Date/Time":
                spot.setDateTime(value);
                break;
            case "Orientation":
                spot.setOrientation(value);
                break;
            case "F-Number":
                spot.setfNumber(value);
                break;
            case "Exposure Time":
                spot.setExposureTime(value);
                break;
            case "Focal Length":
                spot.setFocalLength(value);
                break;
            case "Flash":
                spot.setFlash(value);
                break;
            case "ISO Speed Ratings":
                spot.setiSOSpeedRatings(value);
                break;
            case "White Balance Mode":
                spot.setWhiteBalanceMode(value);
                break;
            case "Aperture Value":
                spot.setApertureValue(value);
                break;
            case "Shutter Speed Value":
                spot.setShutterSpeedValue(value);
                break;
            case "Detected File Type Name":
                spot.setDetectedFileTypeName(value);
                break;
            case "File Size":
                spot.setFileSize(value);
                break;
            case "Brightness Value":
                spot.setBrightnessValue(value);
                break;
            case "Exposure Bias Value":
                spot.setExposureBiasValue(value);
                break;
            case "Max Aperture Value":
                spot.setMaxApertureValue(value);
                break;
            case "Digital Zoom Ratio":
                spot.setDigitalZoomRatio(value);
                break;
            case "Contrast":
                spot.setContrast(value);
                break;
            case "Saturation":
                spot.setSaturation(value);
                break;
            case "Sharpness":
                spot.setSharpness(value);
                break;
        }
    }
}
