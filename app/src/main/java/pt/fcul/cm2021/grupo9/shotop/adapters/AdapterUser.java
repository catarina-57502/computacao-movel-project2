package pt.fcul.cm2021.grupo9.shotop.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.fcul.cm2021.grupo9.shotop.databinding.UserRowLayoutBinding;
import pt.fcul.cm2021.grupo9.shotop.entidades.User;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserViewHolder> {

    private List<User> users = new ArrayList<>();

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private final UserRowLayoutBinding binding;

        public UserViewHolder(UserRowLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public UserRowLayoutBinding getBinding() {
            return binding;
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(UserRowLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false)
        );
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
}
