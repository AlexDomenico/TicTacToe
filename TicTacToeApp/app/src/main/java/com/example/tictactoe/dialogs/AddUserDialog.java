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

public class AddUserDialog extends DialogFragment {

    public interface AddUserNamePasser{
        void addUserNamePasser(String addUsername);
    }

    AddUserDialog.AddUserNamePasser listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddUserNamePasser) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.button_add_user);

        //Set up the input
        final EditText userNameInput = new EditText(getActivity());
        userNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(userNameInput);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.addUserNamePasser(userNameInput.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancel adding
            }
        });

        return builder.create();
    }
}