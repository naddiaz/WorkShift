package com.naddiaz.workshift.ui.dialog;

import android.content.Context;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.naddiaz.workshift.R;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class CalendarDialog {
    Context context;

    public CalendarDialog(Context context) {
        this.context = context;
    }

    public void showChangeTurnDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
            .title("Cambiar el turno")
            .customView(R.layout.dialog_change_turn, true)
            .positiveText("Guardar")
            .negativeText(android.R.string.cancel)
            .callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    Log.i("DIALOG", "onPositive");
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    Log.i("DIALOG", "onNegative");
                }
            }).build();
        dialog.show();
    }
}
