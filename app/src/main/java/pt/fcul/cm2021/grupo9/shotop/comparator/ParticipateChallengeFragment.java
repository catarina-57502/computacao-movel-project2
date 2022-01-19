package pt.fcul.cm2021.grupo9.shotop.comparator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.location.MapaFragment;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;

public class ParticipateChallengeFragment extends Fragment {

    private View view;
    private Spot ogSpot;
    private Spot newSpot;
    private Comparator comparator= new Comparator(); //precisa refactor disto
    private TextView score;
    private Desafio desafio;

    FirebaseUser firebaseUser;

    public ParticipateChallengeFragment() {
        this.ogSpot = MainActivity.spotOriginal;
        this.newSpot = MainActivity.spotParticipacao;
    }

    public ParticipateChallengeFragment(Spot ogSpot, Spot newSpot) {

        this.ogSpot = MainActivity.spotOriginal;
        this.newSpot = MainActivity.spotParticipacao;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_participate_challenge, container, false);
        if(ogSpot != null && newSpot != null){
            score = view.findViewById(R.id.Score);
            int result = (int) comparator.compareTwoSpots(ogSpot,newSpot);
            score.setText( result +"%");
            FirebaseAuth mAuth = FirebaseAuth.getInstance();


            comprimir();


            desafio = new Desafio(newSpot.getImagem(),ogSpot.getId(), ogSpot.getIdUser(), mAuth.getCurrentUser().getUid(), Integer.toString(result));
        }



        Button btn = (Button) view.findViewById(R.id.submitEntry);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                submitEntry(desafio);
            }
        });

      return view;
    }

    public void comprimir(){
        byte[] bytes = Base64.getDecoder().decode(newSpot.getImagem());
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        newSpot.setImagem(baos.toByteArray());
    }

    private void submitEntry(Desafio desafio) {
        MainActivity.db.collection("Desafio")
                .add(desafio)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("SHOTOP", "DocumentSnapshot added with ID: " + documentReference.getId());

                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameFragment, new MapaFragment())
                                .commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("SHOTOP", "Error adding document", e);
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameFragment, new MapaFragment())
                                .commit();
                    }
                });
    }



}