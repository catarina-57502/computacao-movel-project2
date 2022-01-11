package pt.fcul.cm2021.grupo9.shotop.processoAdicionar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterList;


public class MetaDataFragment extends Fragment {

    private static final int PICK_IMAGE = 5;
    private static final int RESULT_OK = 200;

    ArrayList<String> ar = new ArrayList<>();

    ListView lv;
    Button b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_meta_data, container, false);
        b = v.findViewById(R.id.but);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(StartAddFragment.currentPhotoPath != null){
                    String selectedImagePath = StartAddFragment.currentPhotoPath;;
                    System.out.println("PATH = " + selectedImagePath);
                    try {
                        File jpegFile = new File(selectedImagePath);
                        Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
                        System.out.println(metadata.getDirectoryCount());
                        String s = "";
                        for (Directory directory : metadata.getDirectories()) {
                            for (Tag tag : directory.getTags()) {
                                //System.out.println(tag);
                                ar.add(tag.toString());
                                s = s + tag + "\n";
                            }
                        }
                        System.out.println(s);
                        lv.invalidateViews();
                        b.setVisibility(View.GONE);

                    } catch (ImageProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /*
                 Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "select image"),
                        PICK_IMAGE);
                 */

            }
        });

        lv = v.findViewById(R.id.listmeta);
        AdapterList adapter = new AdapterList(ar,getContext());
        lv.setAdapter(adapter);


        return v;
    }


/*
public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==  PICK_IMAGE) {
            Uri selectedImageUri = data.getData();
            System.out.println("Oiiuii");
            System.out.println(selectedImageUri);
            String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
            System.out.println(selectedImagePath);
            try {
                File jpegFile = new File(selectedImagePath);
                Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
                System.out.println(metadata.getDirectoryCount());
                String s = "";
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        //System.out.println(tag);
                        ar.add(tag.toString());
                        s = s + tag + "\n";
                    }
                }
                System.out.println(s);
                lv.invalidateViews();
                b.setVisibility(View.GONE);

            } catch (ImageProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else{

        }
    }
 */
    /*
    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }
     */


}