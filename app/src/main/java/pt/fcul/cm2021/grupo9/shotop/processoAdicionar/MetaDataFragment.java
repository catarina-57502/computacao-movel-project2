package pt.fcul.cm2021.grupo9.shotop.processoAdicionar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.adapters.AdapterList;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;

@SuppressLint("NewApi")
public class MetaDataFragment extends Fragment {

    String imag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_meta_data, container, false);

        MainActivity.db.collection("Spot")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData().get("imagem"));
                                imag = (String) document.getData().get("imagem");
                            }
                            byte[] bytes = Base64.getDecoder().decode(imag);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ImageView imgV = v.findViewById(R.id.imgId);
                            imgV.setImageBitmap(bitmap);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }});

        return v;
    }




}