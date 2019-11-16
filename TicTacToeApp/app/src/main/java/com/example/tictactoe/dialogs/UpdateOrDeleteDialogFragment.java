package com.example.tictactoe.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.tictactoe.R;
import com.example.tictactoe.ScoreBoard;

import static com.example.tictactoe.ScoreBoard.PlayerUpdateDelete_Cancel;

public class UpdateOrDeleteDialogFragment extends DialogFragment {
    public interface NoticeDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
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
        builder.setMessage(R.string.update_or_delete)
                //Update
                .setPositiveButton(R.string.update_player, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Update player
                        listener.onDialogPositiveClick(UpdateOrDeleteDialogFragment.this);
                    }
                })

                //Delete
                .setNegativeButton(R.string.delete_player, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Delete player
                        listener.onDialogNegativeClick(UpdateOrDeleteDialogFragment.this);
                    }
                })

                //Cancel
                .setNeutralButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Cancel update/delete
                        PlayerUpdateDelete_Cancel();
                    }
                });
        return builder.create();
    }
}