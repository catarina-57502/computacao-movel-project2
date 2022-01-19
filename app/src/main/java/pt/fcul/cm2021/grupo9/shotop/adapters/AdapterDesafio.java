package pt.fcul.cm2021.grupo9.shotop.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.comparator.Desafio;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;
import pt.fcul.cm2021.grupo9.shotop.main.MainActivity;

public class AdapterDesafio extends BaseAdapter {

    private ArrayList<Desafio> desafios;
    private Context context;

    Comparator<Desafio> compareByScore = new Comparator<Desafio>() {
        @Override
        public int compare(Desafio o1, Desafio o2) {
            Integer i1 = o1.getScoreInt();
            Integer i2 = o2.getScoreInt();
            return i2.compareTo(i1);
        }
    };

    public AdapterDesafio(ArrayList<Desafio> desafios, Context context) {
        this.desafios = desafios;
        this.context = context;
        Collections.sort(desafios, compareByScore);
    }

    @Override
    public int getCount() {
        return desafios.size();
    }

    @Override
    public Object getItem(int position) {
        return desafios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_layout_desafios, parent, false);
        }
        ImageView imageViewTrofeu = (ImageView)convertView.findViewById(R.id.imageViewTrofeu);

        if(position == 0){
            imageViewTrofeu.setImageResource(R.drawable.gold);
        }
        if(position == 1){
            imageViewTrofeu.setImageResource(R.drawable.silver);
        }
        if(position == 2){
            imageViewTrofeu.setImageResource(R.drawable.bronze);
        }
        if(position > 2){
            imageViewTrofeu.setImageResource(R.drawable.outros);
        }
        TextView tvName = (TextView)convertView.findViewById(R.id.name);
        tvName.setText(MainActivity.getUserFromID(desafios.get(position).getIdUserParticipante()).getNome() );

        TextView tvScore = (TextView)convertView.findViewById(R.id.score);
        tvScore.setText(desafios.get(position).getScore() + "%");

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);

        byte[] bytes = Base64.getDecoder().decode(desafios.get(position).getFotoParticipacao());
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos);
        imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
