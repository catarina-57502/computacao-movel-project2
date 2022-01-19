package pt.fcul.cm2021.grupo9.shotop.community;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import pt.fcul.cm2021.grupo9.shotop.R;

public class AddFriendDialogFragment extends AppCompatDialogFragment
{
    private EditText etFriend;
    private AddFriendDialogFragmentListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_friend_dialog, null);

        builder.setView(view)
                .setTitle("Add Friend")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String friend = etFriend.getText().toString();
                        listener.addFriend(friend);
                    }
                });

        etFriend = view.findViewById(R.id.et_friend);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddFriendDialogFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddFriendDialogFragmentListener");
        }
    }

    public interface AddFriendDialogFragmentListener {
        void addFriend(String friend);
    }
}
