package pt.fcul.cm2021.grupo9.shotop.processoAdicionar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.camera.BitmapTools;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;


public class CheckPhotoFragment extends Fragment {

    Bitmap bitmap;
    String currentPhotoPath;
    Spot spot;
    String orientationRadio = "Vertical";

    CheckPhotoFragment(){

    }

    CheckPhotoFragment(Bitmap bm, String currentPhotoPath, Spot spot){
        this.bitmap = bm;
        this.currentPhotoPath = currentPhotoPath;
        this.spot = spot;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_check_photo, container, false);
        ImageView imgView = v.findViewById(R.id.photoview);

        getDados();
        setPic(imgView);


        Button btn = (Button) v.findViewById(R.id.repetirBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new StartAddFragment())
                        .commit();
            }
        });

        Button btnCtn = (Button) v.findViewById(R.id.continuarBtn);

        btnCtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBar progressBar = v.findViewById(R.id.progress_loader);
                progressBar.setVisibility(View.VISIBLE);

                spot.setOrientation(orientationRadio);

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new VisionPhotoFragment(bitmap,spot))
                        .commit();
            }
        });

        RadioGroup rb = (RadioGroup) v.findViewById(R.id.radioG);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_V:
                        orientationRadio = "Vertical";
                        break;
                    case R.id.radio_H:
                        orientationRadio = "Horizontal";
                        break;
                }
            }

        });

        final View view = v.findViewById(R.id.circle_two);
        view.setBackground(getResources().getDrawable(R.drawable.circle));

        return v;
    }




    public void getDados(){
        if(StartAddFragment.currentPhotoPath != null){
            String selectedImagePath = StartAddFragment.currentPhotoPath;;
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

    public void checkCategoria(String ctg, String value){
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

    private void setPic(ImageView imgV) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        // Determine how much to scale down the image
        int scaleFactor = 1;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);


        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if(spot.getOrientation().toLowerCase().contains(("right"))){
            bitmap = BitmapTools.rotate(bitmap,90);
        }else{
            bitmap = BitmapTools.rotate(bitmap,0);
        }


        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byteArray = stream.toByteArray();
        spot.setImagem(byteArray);


        imgV.setImageBitmap(bitmap);
    }
}