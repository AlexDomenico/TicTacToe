package com.example.tictactoe.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.tictactoe.R;

public class UpdateOrDeleteDialogFragment extends DialogFragment {
    private String activeUserName;


    public interface NoticeDialogListener{
        void onDialogPositiveClick(DialogFragment dialog, String username);
        void onDialogNegativeClick(DialogFragment dialog, String username);
    }

    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NoticeDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() +
                    " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        activeUserName = getArguments().getString("username");
        String title = getContext().getResources().getString(R.string.update_or_delete);
        builder.setMessage(title + " " + activeUserName)
                //Update
                .setPositiveButton(R.string.update_player, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Update player
                        listener.onDialogPositiveClick(UpdateOrDeleteDialogFragment.this, activeUserName);
                    }
                })

                //Delete
                .setNegativeButton(R.string.delete_player, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Delete player
                        listener.onDialogNegativeClick(UpdateOrDeleteDialogFragment.this, activeUserName);
                    }
                })

                //Cancel
                .setNeutralButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Cancel update/delete
                    }
                });
        return builder.create();
    }
}