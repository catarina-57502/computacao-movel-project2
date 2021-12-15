package pt.fcul.cm2021.grupo9.shotop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListCheckBox extends BaseAdapter {
    private ArrayList<VisionResponse> listVision;
    private Context context;

    public AdapterListCheckBox(ArrayList<VisionResponse> listVision, Context context) {
        this.listVision = listVision;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listVision.size();
    }

    @Override
    public Object getItem(int position) {
        return listVision.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_layout_checkbox, parent, false);
        }
        TextView tvName = (TextView)convertView.findViewById(R.id.name);
        tvName.setText(listVision.get(position).description);


        return convertView;
    }
}