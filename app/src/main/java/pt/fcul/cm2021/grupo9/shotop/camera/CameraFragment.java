package pt.fcul.cm2021.grupo9.shotop.camera;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
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
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.comparator.ParticipateChallengeFragment;
import pt.fcul.cm2021.grupo9.shotop.desafio.DesafioCamerasFragment;
import pt.fcul.cm2021.grupo9.shotop.desafio.SpotTools;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.StartAddFragment;

public class CameraFragment extends Fragment {

    private CameraPreview mPreview;
    static int orientation;
    static Spot spotOriginal;
    static int width = 0;
    static int height = 0;
    static Camera c = null;;

    public CameraFragment(){

    }

    public CameraFragment(Spot spot){
        MainActivity.spotOriginal = spot;

        spotOriginal = spot;



        String img = spot.getImagem();
        byte[] bytes = Base64.getDecoder().decode(img);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        spotOriginal.setImageHeight("1920");
        spotOriginal.setImageWidth("1080");

        resolucao();
    }

    public void resolucao(){
        try{
            width = Integer.parseInt(spotOriginal.getImageWidth());
            height = Integer.parseInt(spotOriginal.getImageHeight());
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        //Confirmar foto original

        if(spotOriginal.getOrientation().equals("Horizontal")){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();


        FrameLayout preview = (FrameLayout) v.findViewById(R.id.camera_preview);

        Camera c = getCameraInstance();

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.GONE);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(getContext(), c);


        preview.addView(mPreview);
        addView();


        return v;
    }

    private static final String CAMERA_PARAM_ORIENTATION = "orientation";
    private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
    private static final String CAMERA_PARAM_PORTRAIT = "portrait";


    @Override
    public void onDestroy() {
        super.onDestroy();
        c.stopPreview();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        c.stopPreview();
    }

    public static Camera getCameraInstance(){

        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            Camera.Parameters parameters = c.getParameters();
            c.stopPreview();
            Camera.Parameters param = c.getParameters();
            Log.i("camera", "parameters: " + param.flatten());

            if (spotOriginal.getOrientation().equals("Horizontal")) {
                c.setDisplayOrientation(360);
                parameters.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_LANDSCAPE);
            } else {
               c.setDisplayOrientation(90);

                parameters.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_PORTRAIT );

            }
            c.setParameters(parameters);
            c.startPreview();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }


    View viewControl;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addView() {


        LayoutInflater controlInflater = LayoutInflater.from(getContext());
        viewControl = controlInflater.inflate(R.layout.overlay, null);


        ImageView img = viewControl.findViewById(R.id.imageView1);

        byte[] bytes = Base64.getDecoder().decode(spotOriginal.getImagem());
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        img.setImageBitmap(bitmap);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void removeView() {
        viewControl.setVisibility(View.GONE);
    }

    int MEDIA_TYPE_IMAGE = 1;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            removeView();
            File pictureFile = null;
            try {
                pictureFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                if (spotOriginal.getOrientation() == "Vertical") {
                    Bitmap bitmap = BitmapTools.toBitmap(data);
                    bitmap = BitmapTools.rotate(bitmap, 90);
                    data = BitmapTools.toBytes(bitmap);
                }
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }finally {

                Spot spotParticipacao = SpotTools.runAll(currentPhotoPath);

                spotParticipacao.setImagem(data);
                MainActivity.spotParticipacao = spotParticipacao;

                MainActivity.spotParticipacao = spotParticipacao;


                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new ParticipateChallengeFragment(MainActivity.spotOriginal,MainActivity.spotParticipacao))
                        .commit();
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