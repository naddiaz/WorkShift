package com.naddiaz.workshift.ui.dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
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

import model.Info;
import model.Turn;
import model.helpers.DatabaseHelper;
import model.helpers.InfoHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class CalendarDialog {
    FragmentActivity fragmentActivity;
    MaterialCalendarView calendarView;

    EditText editTextAddComment;

    public static DatabaseHelper databaseHelper;
    public static TurnHelper turnHelper;
    public static InfoHelper infoHelper;

    public CalendarDialog(FragmentActivity fragmentActivity, MaterialCalendarView calendarView) {
        this.fragmentActivity = fragmentActivity;
        this.calendarView = calendarView;
        databaseHelper = new DatabaseHelper(fragmentActivity);
        turnHelper = new TurnHelper(databaseHelper);
        infoHelper = new InfoHelper(databaseHelper);
    }

    public void showChangeTurnDialog() {
        String title = "";
        Turn turn = null;
        try {
            String dateStr = calendarView.getSelectedDate().getYear() + ".";
            dateStr += (calendarView.getSelectedDate().getMonth() + 1)  + ".";
            dateStr += calendarView.getSelectedDate().getDay();
            ArrayList<Turn> turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();
            if(!turnArrayList.isEmpty()){
                turn = turnArrayList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final List<String> turns = Arrays.asList(fragmentActivity.getResources().getStringArray(R.array.turns_names));
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

                            Info infoChange = new Info(changeTurn.getTurn(),changeTurn.getDate(),Info.CHANGE);
                            infoChange.setTitle("Original: " + turns.get(finalTurn.getTurn()).toUpperCase());
                            infoChange.setDetail("Cambio: " + turns.get(changeTurn.getTurn()).toUpperCase());
                            try {
                                turnHelper.getTurnDAO().createOrUpdate(changeTurn);

                                ArrayList<Info> infoArrayList = (ArrayList<Info>) infoHelper.getInfoDAO()
                                        .queryBuilder()
                                        .where()
                                        .eq(Info.DATE, finalTurn.getDate())
                                        .and()
                                        .eq(Info.TYPE,Info.DOUBLE)
                                        .or()
                                        .eq(Info.TYPE,Info.CHANGE)
                                        .query();

                                infoHelper.getInfoDAO().delete(infoArrayList);
                                infoHelper.getInfoDAO().create(infoChange);

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

    public void showDoubleTurnDialog() {
        String title = "";
        Turn turn = null;
        try {
            String dateStr = calendarView.getSelectedDate().getYear() + ".";
            dateStr += (calendarView.getSelectedDate().getMonth() + 1)  + ".";
            dateStr += calendarView.getSelectedDate().getDay();
            ArrayList<Turn> turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();
            if(!turnArrayList.isEmpty()){
                turn = turnArrayList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (turn != null) {
            if (turn.getTurn() != Turn.TURN_MORNING && turn.getTurn() != Turn.TURN_AFTERNOON && turn.getTurn() != Turn.TURN_EVENING) {
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.not_double_change)
                        .positiveText(R.string.agree)
                        .build()
                        .show();
            } else {
                final List<String> turns = Arrays.asList(fragmentActivity.getResources().getStringArray(R.array.turns_names_simple));
                title = fragmentActivity.getString(R.string.double_turn);
                final Turn finalTurn = turn;
                new MaterialDialog.Builder(fragmentActivity)
                    .title(title)
                    .items(R.array.turns_names_double)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which != finalTurn.getTurn()) {
                                Turn changeTurn = new Turn();
                                changeTurn.setDate(finalTurn.getDate());
                                changeTurn.setMonth(finalTurn.getMonth());
                                changeTurn.setYear(finalTurn.getYear());
                                if (finalTurn.getTurn() == Turn.TURN_MORNING && which == Turn.TURN_AFTERNOON) {
                                    changeTurn.setTurn(Turn.TURN_MORNING_AFTERNOON);
                                } else if (finalTurn.getTurn() == Turn.TURN_MORNING && which == Turn.TURN_EVENING) {
                                    changeTurn.setTurn(Turn.TURN_MORNING_EVENING);
                                } else if (finalTurn.getTurn() == Turn.TURN_AFTERNOON && which == Turn.TURN_EVENING) {
                                    changeTurn.setTurn(Turn.TURN_AFTERNOON_EVENING);
                                }
                                else if (finalTurn.getTurn() == Turn.TURN_AFTERNOON && which == Turn.TURN_MORNING) {
                                    changeTurn.setTurn(Turn.TURN_MORNING_AFTERNOON);
                                } else if (finalTurn.getTurn() == Turn.TURN_EVENING && which == Turn.TURN_MORNING) {
                                    changeTurn.setTurn(Turn.TURN_MORNING_EVENING);
                                } else if (finalTurn.getTurn() == Turn.TURN_EVENING && which == Turn.TURN_AFTERNOON) {
                                    changeTurn.setTurn(Turn.TURN_AFTERNOON_EVENING);
                                }
                                Info info = new Info(which, changeTurn.getDate(), Info.DOUBLE);
                                info.setTitle(turns.get(which).toString().toUpperCase());
                                try {
                                    turnHelper.getTurnDAO().createOrUpdate(changeTurn);
                                    infoHelper.getInfoDAO().create(info);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                fragmentActivity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container, new CalendarFragment())
                                        .commit();
                            }
                            return true;
                        }
                    })
                    .negativeText(R.string.disagree)
                    .show();
            }
        }
        else {
            title = fragmentActivity.getString(R.string.change_not_turn);
            new MaterialDialog.Builder(fragmentActivity)
                    .title(title)
                    .positiveText(R.string.agree)
                    .show();
        }
    }

    public void showCommentDialog() {
        String title = "";
        Turn turn = null;
        try {
            String dateStr = calendarView.getSelectedDate().getYear() + ".";
            dateStr += (calendarView.getSelectedDate().getMonth() + 1)  + ".";
            dateStr += calendarView.getSelectedDate().getDay();
            ArrayList<Turn> turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();
            if(!turnArrayList.isEmpty()){
                turn = turnArrayList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (turn != null) {
            final Turn finalTurn = turn;
            MaterialDialog dialog = new MaterialDialog.Builder(fragmentActivity)
            .title(R.string.turn_add_comment)
                    .customView(R.layout.dialog_comment, true)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            if(!editTextAddComment.getText().equals("")){
                                Info info = new Info(finalTurn.getTurn(), finalTurn.getDate(), Info.COMMENT);
                                info.setTitle(editTextAddComment.getText().toString().toUpperCase());
                                try {
                                    infoHelper.getInfoDAO().create(info);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                fragmentActivity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container, new CalendarFragment())
                                        .commit();
                            }
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                        }
                    }).build();
            editTextAddComment = (EditText) dialog.getCustomView().findViewById(R.id.editText_turnComment);
            dialog.show();
        } else {
            title = fragmentActivity.getString(R.string.change_not_turn);
            new MaterialDialog.Builder(fragmentActivity)
                    .title(title)
                    .positiveText(R.string.agree)
                    .show();
        }

    }
}
