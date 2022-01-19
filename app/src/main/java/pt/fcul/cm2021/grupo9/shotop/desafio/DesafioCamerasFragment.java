package pt.fcul.cm2021.grupo9.shotop.desafio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.fcul.cm2021.grupo9.shotop.MySpots.SpotInfoFragment;
import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.camera.CameraFragment;
import pt.fcul.cm2021.grupo9.shotop.comparator.ParticipateChallengeFragment;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;
import pt.fcul.cm2021.grupo9.shotop.processoAdicionar.CheckPhotoFragment;

public class DesafioCamerasFragment extends Fragment {

    public Spot spotOriginal;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    boolean cam1 = true;

    Uri photoURI;

    public DesafioCamerasFragment(){

    }

    public DesafioCamerasFragment(Spot s){
        MainActivity.spotOriginal = s;
        this.spotOriginal = s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_desafio_cameras, container, false);

        Button btn1 = (Button) v.findViewById(R.id.btnCamera1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cam1 = true;
                dispatchTakePictureIntent();
            }
        });

        Button btn2 = (Button) v.findViewById(R.id.btnCamera2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cam1 = false;
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new CameraFragment(spotOriginal))
                        .commit();
            }
        });

        return v;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(getContext(),
                    "pt.fcul.cm2021.grupo9.shotop.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }

    }

    public static String currentPhotoPath;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && cam1) {
            Bitmap bm = BitmapFactory.decodeFile(currentPhotoPath);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byte[] byteArray = stream.toByteArray();



            Spot spotParticipacao = SpotTools.runAll(currentPhotoPath);
            spotParticipacao.setImagem(byteArray);
            MainActivity.spotParticipacao = spotOriginal;
            MainActivity.spotParticipacao = spotParticipacao;
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameFragment, new ParticipateChallengeFragment(MainActivity.spotOriginal,MainActivity.spotParticipacao))
                    .commit();
        }

    }

}