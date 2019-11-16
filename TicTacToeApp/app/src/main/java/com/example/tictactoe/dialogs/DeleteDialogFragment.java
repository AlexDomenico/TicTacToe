package com.example.tictactoe.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.tictactoe.R;
import com.example.tictactoe.ScoreBoard;

import static com.example.tictactoe.ScoreBoard.PlayerUpdateDelete_Cancel;

public class DeleteDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_title)
                // Delete
                .setPositiveButton(R.string.delete_player, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })

                // Cancel
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PlayerUpdateDelete_Cancel();
                    }
                });
        return builder.create();
    }
}