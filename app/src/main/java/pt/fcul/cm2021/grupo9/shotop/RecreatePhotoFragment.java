package pt.fcul.cm2021.grupo9.shotop;

import static android.media.ExifInterface.TAG_DATETIME;
import static android.media.ExifInterface.TAG_DATETIME_ORIGINAL;
import static android.media.ExifInterface.TAG_FLASH;
import static android.media.ExifInterface.TAG_IMAGE_WIDTH;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


//Explorar como sobrebor uma foto no momento tirar a foto e recolher os metadados numa foto -  implementar num fragmento

public class RecreatePhotoFragment extends Fragment {

    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recreate_photo, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button button1 = (Button) getActivity().findViewById(R.id.btn_take_photo);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        Button button2 = (Button) getActivity().findViewById(R.id.btn_recreate_photo);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //TODO
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "Unable to open camera", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "pt.fcul.cm2021.grupo9.shotop.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap imageBitmap = decodeFile(currentPhotoPath);
            View vImg = getActivity().findViewById(R.id.img1);
            ((ImageView) vImg).setImageBitmap(imageBitmap);
            TextView textView = (TextView) getActivity().findViewById(R.id.photo_metadata);
            getImageMetaData(imageBitmap);

        }
    }


    @SuppressLint("NewApi")
    private static void getImageMetaData(Bitmap img) {

        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            final ExifInterface exif = new ExifInterface(bs);
            System.out.println(exif.getAttribute(ExifInterface.TAG_ISO));
            System.out.println(exif.getAttribute(ExifInterface.TAG_WHITE_BALANCE));
            System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
            System.out.println(exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP));

            /*
              this.datetime = inFile.getAttribute(ExifInterface.TAG_DATETIME);
            this.exposureTime = inFile.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            this.flash = inFile.getAttribute(ExifInterface.TAG_FLASH);
            this.focalLength = inFile.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            this.gpsAltitude = inFile.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
            this.gpsAltitudeRef = inFile.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
            this.gpsDateStamp = inFile.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            this.gpsLatitude = inFile.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            this.gpsLatitudeRef = inFile.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            this.gpsLongitude = inFile.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            this.gpsLongitudeRef = inFile.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            this.gpsProcessingMethod = inFile.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
            this.gpsTimestamp = inFile.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            this.iso = inFile.getAttribute(ExifInterface.TAG_ISO);
            this.make = inFile.getAttribute(ExifInterface.TAG_MAKE);
            this.model = inFile.getAttribute(ExifInterface.TAG_MODEL);
            this.orientation = inFile.getAttribute(ExifInterface.TAG_ORIENTATION);
            this.whiteBalance = inFile.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
             */


/*
ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            Metadata metadata = ImageMetadataReader.readMetadata(new BufferedInputStream(bs), bitmapdata.length);
             for(Directory directory : metadata.getDirectories())
            {
                Log.d("LOG", "Directory: " + directory.getName());

                // Log all errors.
                for(String error : directory.getErrors())
                {
                    Log.d("LOG", "> error: " + error);
                }

                // Log all tags.
                for(Tag tag : directory.getTags())
                {
                    Log.d("LOG", "> tag: " + tag.getTagName() + " = " + tag.getDescription());
                }
            }
 */

            // Log each directory.


        }
        catch(Exception e)
        {
            // TODO: handle exception
        }

    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 500;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

}