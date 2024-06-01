package com.example.app2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * ConfirmationDialog shows a dialog to confirm resetting the step count.
 */
public class ConfirmationDialog extends DialogFragment {

    /**
     * Interface for handling dialog actions.
     */
    public interface ConfirmationDialogListener {
        void onConfirm();
        void onCancel();
    }

    private ConfirmationDialogListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (ConfirmationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ConfirmationDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Opravdu chcete resetovat počet kroků?")
                .setPositiveButton("Resetovat", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onConfirm();
                    }
                })
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCancel();
                    }
                });
        return builder.create();
    }
}
