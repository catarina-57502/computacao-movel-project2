package pt.fcul.cm2021.grupo9.shotop.camera;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.StartAddFragment;

public class CameraFragment extends Fragment {

    private CameraPreview mPreview;
    static int orientation;
    static Spot spotOriginal;
    static int width = 0;
    static int height = 0;
    static Camera c = null;;




    public CameraFragment(){
        spotOriginal = new Spot("Teste",new GeoPoint(MapaFragment.lastLocation.latitude,MapaFragment.lastLocation.longitude));

        int width = 1280;
        int height = 960;
        spotOriginal.setImageWidth(Integer.toString(width));
        spotOriginal.setImageHeight(Integer.toString(height));

        spotOriginal.setOrientation("Horizontal");
        //spotOriginal.setOrientation("Vertical");
        resolucao();
    }

    public void resolucao(){
        try{
            width = Integer.parseInt(spotOriginal.getImageWidth());
            height = Integer.parseInt(spotOriginal.getImageHeight());
            System.out.println(height);
            System.out.println(width);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        //Confirmar foto original
        if(spotOriginal.getOrientation() == "Horizontal"){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;

        }


        FrameLayout preview = (FrameLayout) v.findViewById(R.id.camera_preview);





        orientation = getActivity().getResources().getConfiguration().orientation;
        Camera c = getCameraInstance();

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.GONE);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(getContext(), c);


        preview.addView(mPreview);
        addView();


        return v;
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){

        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            c.stopPreview();
            c.setDisplayOrientation(90);
            Camera.Parameters parameters = c.getParameters();




            parameters.setPictureSize(width,height);

            if (spotOriginal.getOrientation() == "Horizontal") {
                c.setDisplayOrientation(270);
            } else {

                c.setDisplayOrientation(0);
            }

            c.setParameters(parameters);
            c.startPreview();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }

    private void addView() {

        LayoutInflater controlInflater = LayoutInflater.from(getContext());
        View viewControl = controlInflater.inflate(R.layout.overlay, null);
        ImageView img = viewControl.findViewById(R.id.imageView1);
        img.setImageResource(R.drawable.teste);
        Button btn = (Button) viewControl.findViewById(R.id.buttonPhoto);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.takePicture(null, null, mPicture);
            }
        });
        ViewGroup.LayoutParams layoutParamsControl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getActivity().addContentView(viewControl, layoutParamsControl);

    }

    int MEDIA_TYPE_IMAGE = 1;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = null;
            try {
                pictureFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
        }
    };

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



}