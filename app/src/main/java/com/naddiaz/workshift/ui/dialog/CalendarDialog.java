package com.naddiaz.workshift.ui.dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.fragment.CalendarFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Turn;
import model.helpers.DatabaseHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class CalendarDialog {
    FragmentActivity fragmentActivity;
    MaterialCalendarView calendarView;

    public static DatabaseHelper databaseHelper;
    public static TurnHelper turnHelper;

    public CalendarDialog(FragmentActivity fragmentActivity, MaterialCalendarView calendarView) {
        this.fragmentActivity = fragmentActivity;
        this.calendarView = calendarView;
        databaseHelper = new DatabaseHelper(fragmentActivity);
        turnHelper = new TurnHelper(databaseHelper);
    }

    public void showChangeTurnDialog() {
        String title = "";
        Turn turn = null;
        try {
            String dateStr = calendarView.getSelectedDate().getYear() + ".";
            dateStr += (calendarView.getSelectedDate().getMonth() + 1)  + ".";
            dateStr += calendarView.getSelectedDate().getDay();

            Log.i("TAG",dateStr);

            ArrayList<Turn> turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();
            Log.i("TAG",turnArrayList.toString());
            if(!turnArrayList.isEmpty()){
                turn = turnArrayList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<String> turns = Arrays.asList(fragmentActivity.getResources().getStringArray(R.array.turns_names_simple));
        if(turn != null){
            title = fragmentActivity.getString(R.string.change_turn) + ": " + turns.get(turn.getTurn());
            final Turn finalTurn = turn;
            new MaterialDialog.Builder(fragmentActivity)
                    .title(title)
                    .items(R.array.turns_names_simple)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            Turn changeTurn = new Turn();
                            changeTurn.setDate(finalTurn.getDate());
                            changeTurn.setMonth(finalTurn.getMonth());
                            changeTurn.setYear(finalTurn.getYear());
                            changeTurn.setTurn(which);
                            try {
                                turnHelper.getTurnDAO().createOrUpdate(changeTurn);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            fragmentActivity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container,new CalendarFragment())
                                    .commit();
                            return true;
                        }
                    })
                    .negativeText(R.string.disagree)
                    .show();
        }
        else{
            title = fragmentActivity.getString(R.string.change_not_turn);
            new MaterialDialog.Builder(fragmentActivity)
                    .title(title)
                    .positiveText(R.string.agree)
                    .show();
        }


    }
}
