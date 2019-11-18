package com.example.tictactoe.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.tictactoe.R;
import com.example.tictactoe.ScoreBoard;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateDialogFragment extends DialogFragment {
    public interface UserNamePasser{
        public void userNamePasser(String updatedUserName);
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
        String username = getArguments().getString("username");
        String title = getContext().getResources().getString(R.string.update_player);
        builder.setMessage(title + " " + username + "'s username");

        //Set up the input
        final EditText updateUserNameInput = new EditText(getActivity());
        updateUserNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(updateUserNameInput);

        // Set up the buttons
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userName = updateUserNameInput.getText().toString();

                listener.userNamePasser(userName);
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