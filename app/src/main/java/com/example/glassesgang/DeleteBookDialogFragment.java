package com.example.glassesgang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * DialogFragment for deleting book
 */
public class DeleteBookDialogFragment extends DialogFragment {
    private DeleteBookDialogListener listener;

    // BookProfileActivity implements this interface
    interface DeleteBookDialogListener {
        void onConfirmPressed();  // called when user confirms the deletion of a book
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        return builder
                .setTitle("Delete this Book?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onConfirmPressed();
                        getActivity().finish();  // return user to HomeActivity
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DeleteBookDialogListener) context;
    }
}
