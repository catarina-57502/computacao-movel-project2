package pt.fcul.cm2021.grupo9.shotop.processoAdicionar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import pt.fcul.cm2021.grupo9.shotop.R;


public class CheckPhotoFragment extends Fragment {

    Bitmap bitmap;
    String currentPhotoPath;

    CheckPhotoFragment(Bitmap bm, String currentPhotoPath){
        this.bitmap = bm;
        this.currentPhotoPath = currentPhotoPath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_check_photo, container, false);
        ImageView imgView = v.findViewById(R.id.photoview);
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
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragment, new VisionPhotoFragment(bitmap))
                        .commit();
            }
        });

        final View view = v.findViewById(R.id.circle_two);
        view.setBackground(getResources().getDrawable(R.drawable.circle));

        return v;
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
        imgV.setImageBitmap(bitmap);
    }
}