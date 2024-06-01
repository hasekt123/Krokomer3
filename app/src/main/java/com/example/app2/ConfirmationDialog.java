package com.example.app2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialog extends DialogFragment {

    // Definice rozhraní pro komunikaci s aktivitou
    public interface ConfirmationDialogListener {
        void onConfirm(); // Metoda pro potvrzení akce
        void onCancel();  // Metoda pro zrušení akce
    }

    private ConfirmationDialogListener mListener; // Proměnná pro posluchače

    // Metoda, která je volána při připojení fragmentu k aktivitě
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Pokus o přiřazení aktivity jako posluchače
            mListener = (ConfirmationDialogListener) context;
        } catch (ClassCastException e) {
            // Pokud aktivita neimplementuje rozhraní, vyhoďte výjimku
            throw new ClassCastException(context.toString() + " must implement ConfirmationDialogListener");
        }
    }

    // Metoda pro vytvoření dialogu
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Vytvoření AlertDialog.Builder s kontextem aktivity
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Nastavení zprávy dialogu a tlačítek
        builder.setMessage("Opravdu chcete resetovat počet kroků?")
                .setPositiveButton("Resetovat", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Volání metody onConfirm() při kliknutí na tlačítko "Resetovat"
                        mListener.onConfirm();
                    }
                })
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Volání metody onCancel() při kliknutí na tlačítko "Zrušit"
                        mListener.onCancel();
                    }
                });
        // Vytvoření a vrácení dialogu
        return builder.create();
    }
}