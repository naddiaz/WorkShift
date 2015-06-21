package com.naddiaz.workshift.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.widget.CalendarView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.naddiaz.workshift.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import model.helpers.DatabaseHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class CalendarDialog {
    Context context;
    MaterialCalendarView calendarView;

    public static DatabaseHelper databaseHelper;
    public static TurnHelper turnHelper;

    public CalendarDialog(Context context,MaterialCalendarView calendarView) {
        this.context = context;
        this.calendarView = calendarView;
        databaseHelper = new DatabaseHelper(context);
        turnHelper = new TurnHelper(databaseHelper);
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
                }
            }).build();
        dialog.show();
    }
}
