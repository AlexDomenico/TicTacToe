package com.example.tictactoe.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.tictactoe.R;

public class UpdateDialogFragment extends DialogFragment {
    public interface UserNamePasser{
        void userNamePasser(String oldUserName, String updatedUserName);
    }

    UserNamePasser listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (UserNamePasser) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() +
                " must implement UserNamePasser");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String oldUserName = getArguments().getString("activeUserName");
        String title = getContext().getResources().getString(R.string.update_player);
        builder.setMessage(title + " " + oldUserName + "'s username");

        //Set up the input
        final EditText updateUserNameInput = new EditText(getActivity());
        updateUserNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(updateUserNameInput);

        // Set up the buttons
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newUserName = updateUserNameInput.getText().toString();

                listener.userNamePasser(oldUserName, newUserName);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }
}