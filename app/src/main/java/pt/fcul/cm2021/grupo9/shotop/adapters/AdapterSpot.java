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

import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;

public class AdapterSpot extends BaseAdapter {
    private ArrayList<Spot> spots;
    private Context context;

    public AdapterSpot(ArrayList<Spot> spots, Context context) {
        this.spots = spots;
        this.context = context;
    }

    @Override
    public int getCount() {
        return spots.size();
    }

    @Override
    public Object getItem(int position) {
        return spots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_layout_spot, parent, false);
        }
        TextView tvName = (TextView)convertView.findViewById(R.id.name);
        tvName.setText(spots.get(position).getNome());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);

        byte[] bytes = Base64.getDecoder().decode(spots.get(position).getImagem());
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        imageView.setImageBitmap(bitmap);

        return convertView;
    }
}

