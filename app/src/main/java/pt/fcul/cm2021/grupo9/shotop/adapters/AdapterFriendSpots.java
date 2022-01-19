package pt.fcul.cm2021.grupo9.shotop.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.MySpots.ListFriendSpotsFragment;
import pt.fcul.cm2021.grupo9.shotop.MySpots.SpotInfoFragment;
import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.databinding.ListLayoutBinding;
import pt.fcul.cm2021.grupo9.shotop.databinding.ListLayoutSpotBinding;
import pt.fcul.cm2021.grupo9.shotop.databinding.UserRowLayoutBinding;
import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;

public class AdapterFriendSpots extends RecyclerView.Adapter<AdapterFriendSpots.FriendSpotsViewHolder> {

    private List<Spot> spots = new ArrayList<>();
    private Context context;

    public AdapterFriendSpots() {

    }

    public AdapterFriendSpots(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FriendSpotsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendSpotsViewHolder(ListLayoutSpotBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false),
                position -> {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameFragment, new SpotInfoFragment(spots.get(position)))
                            .commit();
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull FriendSpotsViewHolder holder, int position) {
        byte[] bytes = Base64.getDecoder().decode(spots.get(position).getImagem());

        holder.getBinding().imageView.setImageBitmap(
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length)
        );

        holder.getBinding().name.setText(spots.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public void setSpots(List<Spot> updatedSpots) {
        spots = updatedSpots;
        notifyDataSetChanged();
    }

    public class FriendSpotsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ListLayoutSpotBinding binding;
        private ClickListener listener;

        public FriendSpotsViewHolder(ListLayoutSpotBinding binding, ClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;

            this.binding.imageView.setOnClickListener(this);
        }

        public ListLayoutSpotBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            listener.showChallenge(this.getLayoutPosition());
        }
    }

    public interface ClickListener {
        void showChallenge(int position);
    }
}
