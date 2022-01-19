package pt.fcul.cm2021.grupo9.shotop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.MySpots.ListFriendSpotsFragment;
import pt.fcul.cm2021.grupo9.shotop.R;
import pt.fcul.cm2021.grupo9.shotop.databinding.UserRowLayoutBinding;
import pt.fcul.cm2021.grupo9.shotop.entidades.User;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserViewHolder> {

    private List<User> users = new ArrayList<>();
    private Context context;

    public AdapterUser() {

    }

    public AdapterUser(Context context) {
        this.context = context;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final UserRowLayoutBinding binding;
        private ClickListener listener;

        public UserViewHolder(UserRowLayoutBinding binding, ClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;

            this.binding.btnShowSpot.setOnClickListener(this);

        }

        public UserRowLayoutBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            listener.showSpots(this.getLayoutPosition());
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(UserRowLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false),
                position -> {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    ListFriendSpotsFragment fragment = new ListFriendSpotsFragment(users.get(position));
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameFragment, fragment)
                            .commit();

                });

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.getBinding().tvUserName.setText(users.get(position).getNome());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> updatedUsers) {
        users = updatedUsers;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void showSpots(int position);
    }
}