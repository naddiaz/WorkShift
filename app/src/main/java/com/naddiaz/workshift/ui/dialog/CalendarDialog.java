package com.naddiaz.workshift.ui.dialog;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.fragment.CalendarFragment;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Change;
import model.Turn;
import model.helpers.ChangeHelper;
import model.helpers.CommentHelper;
import model.helpers.DatabaseHelper;
import model.helpers.DubbingHelper;
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
    public static ChangeHelper changeHelper;
    public static DubbingHelper dubbingHelper;
    public static CommentHelper commentHelper;

    public CalendarDialog(FragmentActivity fragmentActivity, MaterialCalendarView calendarView) {
        this.fragmentActivity = fragmentActivity;
        this.calendarView = calendarView;
        databaseHelper = new DatabaseHelper(fragmentActivity);
        turnHelper = new TurnHelper(databaseHelper);
    }

    public CalendarDialog(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        databaseHelper = new DatabaseHelper(fragmentActivity);
        turnHelper = new TurnHelper(databaseHelper);
    }

    public void showChangeTurnDialog() {
        try {
            String dateStr = calendarView.getSelectedDate().getYear() + ".";
            dateStr += (calendarView.getSelectedDate().getMonth() + 1) + ".";
            dateStr += calendarView.getSelectedDate().getDay();

            ArrayList<Turn> turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();
            if (turnArrayList.isEmpty() || turnArrayList.get(0).isChange()) {
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.info_delete_change_for_remove_title)
                        .content(R.string.info_delete_change_for_remove)
                        .positiveText(R.string.agree)
                        .show();
            }
            else{
                changeHelper = new ChangeHelper(databaseHelper);
                ArrayList<Change> changeArrayList = (ArrayList<Change>) changeHelper.getChangeDAO()
                        .queryBuilder()
                        .where()
                        .eq(Turn.DATE, dateStr)
                        .query();
                final Turn turn = turnArrayList.get(0);

                new MaterialDialog.Builder(fragmentActivity)
                        .title(fragmentActivity.getString(R.string.change_turn) + ": " + fragmentActivity.getResources().getString(Turn.nameFromInt.get(turn.getTurnOriginal())))
                        .items(R.array.turns_names_simple)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    public void showDoubleTurnDialog() {
        try {
            String dateStr = calendarView.getSelectedDate().getYear() + ".";
            dateStr += (calendarView.getSelectedDate().getMonth() + 1) + ".";
            dateStr += calendarView.getSelectedDate().getDay();

            ArrayList<Turn> turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();

            ArrayList<Info> infoArrayList = (ArrayList<Info>) infoHelper.getInfoDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .and()
                    .eq(Info.TYPE,Info.DOUBLE)
                    .query();
            if(!infoArrayList.isEmpty()){
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.info_delete_double_for_remove_title)
                        .content(R.string.info_delete_double_for_remove)
                        .positiveText(R.string.agree)
                        .show();
            }
            else {
                final Turn turn = turnArrayList.get(0);
                final List<String> turns = Arrays.asList(fragmentActivity.getResources().getStringArray(R.array.turns_names));
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.double_turn)
                        .items(R.array.turns_names_double)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which != turn.getTurn()) {
                                    Turn changeTurn = new Turn();
                                    changeTurn.setDate(turn.getDate());
                                    changeTurn.setMonth(turn.getMonth());
                                    changeTurn.setYear(turn.getYear());

                                    if (turn.getTurn() == Turn.TURN_MORNING && which == Turn.TURN_AFTERNOON) {
                                        changeTurn.setTurn(Turn.TURN_MORNING_AFTERNOON);
                                    } else if (turn.getTurn() == Turn.TURN_MORNING && which == Turn.TURN_EVENING) {
                                        changeTurn.setTurn(Turn.TURN_MORNING_EVENING);
                                    } else if (turn.getTurn() == Turn.TURN_AFTERNOON && which == Turn.TURN_EVENING) {
                                        changeTurn.setTurn(Turn.TURN_AFTERNOON_EVENING);
                                    } else if (turn.getTurn() == Turn.TURN_AFTERNOON && which == Turn.TURN_MORNING) {
                                        changeTurn.setTurn(Turn.TURN_MORNING_AFTERNOON);
                                    } else if (turn.getTurn() == Turn.TURN_EVENING && which == Turn.TURN_MORNING) {
                                        changeTurn.setTurn(Turn.TURN_MORNING_EVENING);
                                    } else if (turn.getTurn() == Turn.TURN_EVENING && which == Turn.TURN_AFTERNOON) {
                                        changeTurn.setTurn(Turn.TURN_AFTERNOON_EVENING);
                                    }

                                    Info info = new Info(turn.getTurn(), turn.getDate(), Info.DOUBLE);
                                    info.setTitle("Doblaje: " + turns.get(changeTurn.getTurn()).toString().toUpperCase());
                                    try {
                                        turnHelper.getTurnDAO().update(changeTurn);
                                        infoHelper.getInfoDAO().createOrUpdate(info);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                fragmentActivity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container, new CalendarFragment())
                                        .commit();
                                return true;
                            }
                        })
                        .negativeText(R.string.disagree)
                        .show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                        if (!editTextAddComment.getText().equals("")) {
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

    public void deleteChangeDialog(final String dateStr, final int month, final int year) {
        new MaterialDialog.Builder(fragmentActivity)
            .title(R.string.delete_change_turn_title)
            .content(R.string.delete_change_turn)
            .positiveText(R.string.agree)
            .negativeText(R.string.disagree)
            .callback(new MaterialDialog.ButtonCallback() {
                  @Override
                  public void onPositive(MaterialDialog dialog) {
                      try {
                          ArrayList<Info> infoArrayList = (ArrayList<Info>) infoHelper.getInfoDAO()
                                  .queryBuilder()
                                  .where()
                                  .eq(Info.DATE, dateStr)
                                  .and()
                                  .eq(Info.TYPE, Info.DOUBLE)
                                  .query();
                          infoHelper.getInfoDAO().delete(infoArrayList);
                          infoArrayList = (ArrayList<Info>) infoHelper.getInfoDAO()
                                  .queryBuilder()
                                  .where()
                                  .eq(Info.DATE, dateStr)
                                  .and()
                                  .eq(Info.TYPE, Info.CHANGE)
                                  .query();
                          infoHelper.getInfoDAO().delete(infoArrayList);

                          Turn changeTurn = new Turn();
                          changeTurn.setDate(infoArrayList.get(0).getDate());
                          changeTurn.setMonth(month);
                          changeTurn.setYear(year);
                          changeTurn.setTurn(infoArrayList.get(0).getTurn());

                          turnHelper.getTurnDAO().update(changeTurn);

                      } catch (SQLException e) {
                          e.printStackTrace();
                      }
                      fragmentActivity.getSupportFragmentManager()
                              .beginTransaction()
                              .replace(R.id.container, new CalendarFragment())
                              .commit();
                  }

                  @Override
                  public void onNegative(MaterialDialog dialog) {
                  }
              }
            )
            .show();
    }

    public void deleteDoubleDialog(final String dateStr, final int month, final int year) {
        new MaterialDialog.Builder(fragmentActivity)
                .title(R.string.delete_double_turn_title)
                .content(R.string.delete_double_turn)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onPositive(MaterialDialog dialog) {
                                  try {
                                      ArrayList<Info> infoArrayList = (ArrayList<Info>) infoHelper.getInfoDAO()
                                              .queryBuilder()
                                              .where()
                                              .eq(Info.DATE, dateStr)
                                              .and()
                                              .eq(Info.TYPE, Info.DOUBLE)
                                              .query();
                                      infoHelper.getInfoDAO().delete(infoArrayList);

                                      ArrayList<Turn> turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                                              .queryBuilder()
                                              .where()
                                              .eq(Turn.DATE, dateStr)
                                              .query();

                                      Turn changeTurn = new Turn();
                                      changeTurn.setDate(infoArrayList.get(0).getDate());
                                      changeTurn.setMonth(month);
                                      changeTurn.setYear(year);
                                      changeTurn.setTurn(turnArrayList.get(0).getTurn());

                                      turnHelper.getTurnDAO().update(changeTurn);

                                  } catch (SQLException e) {
                                      e.printStackTrace();
                                  }
                                  fragmentActivity.getSupportFragmentManager()
                                          .beginTransaction()
                                          .replace(R.id.container, new CalendarFragment())
                                          .commit();
                              }

                              @Override
                              public void onNegative(MaterialDialog dialog) {
                              }
                          }
                )
                .show();
    }*/
}
