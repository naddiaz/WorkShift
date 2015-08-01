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
import java.util.Map;

import model.Change;
import model.Comment;
import model.Dubbing;
import model.Turn;
import model.helpers.ChangeHelper;
import model.helpers.CommentHelper;
import model.helpers.DatabaseHelper;
import model.helpers.DubbingHelper;
import model.helpers.TurnHelper;
import webservices.Actions;

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
        changeHelper = new ChangeHelper(databaseHelper);
        dubbingHelper = new DubbingHelper(databaseHelper);
        commentHelper = new CommentHelper(databaseHelper);
    }

    public CalendarDialog(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        databaseHelper = new DatabaseHelper(fragmentActivity);
        turnHelper = new TurnHelper(databaseHelper);
        changeHelper = new ChangeHelper(databaseHelper);
        dubbingHelper = new DubbingHelper(databaseHelper);
        commentHelper = new CommentHelper(databaseHelper);
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
                final Turn turn = turnArrayList.get(0);

                new MaterialDialog.Builder(fragmentActivity)
                        .title(fragmentActivity.getString(R.string.change_turn) + ": " + fragmentActivity.getResources().getString(Turn.nameFromInt.get(turn.getTurnOriginal())))
                        .items(R.array.turns_names_simple)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                for (Map.Entry<Integer, Integer> turnInt : Turn.intFromName.entrySet()) {
                                    if (fragmentActivity.getResources().getString(turnInt.getKey()).equals(String.valueOf(text))) {
                                        if (turnInt.getValue() != turn.getTurnActual()) {
                                            Change change = new Change(turnInt.getValue(), turn.getDate());
                                            Turn newTurn = turn;
                                            newTurn.setTurnActual(turnInt.getValue());
                                            newTurn.setIsChange(true);
                                            try {
                                                changeHelper.getChangeDAO().createOrUpdate(change);
                                                turnHelper.getTurnDAO().createOrUpdate(newTurn);
                                                new Actions(fragmentActivity).addChange(newTurn, change);
                                                fragmentActivity.getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.container, new CalendarFragment())
                                                        .commit();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
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

    public void deleteChangeDialog(final String dateStr) {
        ArrayList<Turn> turnArrayList = null;
        try {
            turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();

            if (!turnArrayList.isEmpty() && turnArrayList.get(0).isChange()) {
                final Turn turn = turnArrayList.get(0);
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.delete_change_turn_title)
                        .content(R.string.delete_change_turn)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.disagree)
                        .callback(new MaterialDialog.ButtonCallback() {
                                      @Override
                                      public void onPositive(MaterialDialog dialog) {
                                          Change change = new Change(turn.getTurnActual(), turn.getDate());
                                          Dubbing dubbing = new Dubbing(turn.getTurnActual(), turn.getDate());
                                          try {
                                              changeHelper.getChangeDAO().delete(change);
                                              dubbingHelper.getDubbingDAO().delete(dubbing);
                                              Turn newTurn = turn;
                                              newTurn.setIsChange(false);
                                              newTurn.setIsDubbing(false);
                                              newTurn.setTurnActual(turn.getTurnOriginal());
                                              turnHelper.getTurnDAO().createOrUpdate(newTurn);
                                              new Actions(fragmentActivity).delChange(newTurn, change, dubbing);
                                          } catch (SQLException e) {
                                              e.printStackTrace();
                                          }
                                          fragmentActivity.getSupportFragmentManager()
                                                  .beginTransaction()
                                                  .replace(R.id.container, new CalendarFragment())
                                                  .commit();
                                      }
                                  }
                        )
                        .show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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
            if (turnArrayList.isEmpty() || turnArrayList.get(0).isDubbing()) {
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.info_delete_double_for_remove_title)
                        .content(R.string.info_delete_double_for_remove)
                        .positiveText(R.string.agree)
                        .show();
            }
            else if(turnArrayList.get(0).getTurnActual() != Turn.MORNING &&
            turnArrayList.get(0).getTurnActual() != Turn.AFTERNOON &&
            turnArrayList.get(0).getTurnActual() != Turn.EVENING){
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.info_delete_double_for_remove_title)
                        .content(R.string.info_delete_double_not_valid)
                        .positiveText(R.string.agree)
                        .show();
            }
            else{
                final Turn turn = turnArrayList.get(0);

                new MaterialDialog.Builder(fragmentActivity)
                        .title(fragmentActivity.getString(R.string.double_turn) + ": " + fragmentActivity.getResources().getString(Turn.nameFromInt.get(turn.getTurnOriginal())))
                        .items(R.array.turns_names_double)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                for (Map.Entry<Integer, Integer> turnInt : Turn.intFromName.entrySet()) {
                                    if (fragmentActivity.getResources().getString(turnInt.getKey()).equals(String.valueOf(text))) {
                                        if (turnInt.getValue() != turn.getTurnActual()) {
                                            Dubbing dubbing = new Dubbing(turnInt.getValue(), turn.getDate());
                                            Turn newTurn = turn;
                                            newTurn.setTurnActual(turnInt.getValue() + turn.getTurnActual());
                                            newTurn.setIsDubbing(true);
                                            try {
                                                dubbingHelper.getDubbingDAO().createOrUpdate(dubbing);
                                                turnHelper.getTurnDAO().createOrUpdate(newTurn);
                                                new Actions(fragmentActivity).addDubbing(newTurn,dubbing);
                                                fragmentActivity.getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.container, new CalendarFragment())
                                                        .commit();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
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

    public void deleteDoubleDialog(final String dateStr) {
        ArrayList<Turn> turnArrayList = null;
        try {
            turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();

            if (!turnArrayList.isEmpty() && turnArrayList.get(0).isDubbing()) {
                final Turn turn = turnArrayList.get(0);
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.delete_double_turn_title)
                        .content(R.string.delete_double_turn)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.disagree)
                        .callback(new MaterialDialog.ButtonCallback() {
                                      @Override
                                      public void onPositive(MaterialDialog dialog) {
                                          Dubbing dubbing = new Dubbing(turn.getTurnActual(), turn.getDate());
                                          try {
                                              dubbingHelper.getDubbingDAO().delete(dubbing);
                                              Turn newTurn = turn;
                                              newTurn.setIsDubbing(false);
                                              if (turn.isChange()) {
                                                  ArrayList<Change> changeArrayList = (ArrayList<Change>) changeHelper.getChangeDAO()
                                                          .queryBuilder()
                                                          .where()
                                                          .eq(Turn.DATE, dateStr)
                                                          .query();
                                                  newTurn.setTurnActual(changeArrayList.get(0).getTurn());
                                              }
                                              else {
                                                  newTurn.setTurnActual(turn.getTurnOriginal());
                                              }
                                              turnHelper.getTurnDAO().createOrUpdate(newTurn);
                                              new Actions(fragmentActivity).delDubbing(newTurn, dubbing);
                                          } catch (SQLException e) {
                                              e.printStackTrace();
                                          }
                                          fragmentActivity.getSupportFragmentManager()
                                                  .beginTransaction()
                                                  .replace(R.id.container, new CalendarFragment())
                                                  .commit();
                                      }
                                  }
                        )
                        .show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showCommentDialog() {
        String dateStr = calendarView.getSelectedDate().getYear() + ".";
        dateStr += (calendarView.getSelectedDate().getMonth() + 1) + ".";
        dateStr += calendarView.getSelectedDate().getDay();
        ArrayList<Turn> turnArrayList = null;
        try {
            turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!turnArrayList.isEmpty()) {
            final Turn turn = turnArrayList.get(0);
            MaterialDialog dialog = new MaterialDialog.Builder(fragmentActivity)
                    .title(R.string.turn_add_comment)
                    .customView(R.layout.dialog_comment, true)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            if (!editTextAddComment.getText().equals("")) {
                                Comment comment = new Comment(turn.getDate(), editTextAddComment.getText().toString().toUpperCase());
                                turn.setContainComment(true);
                                try {
                                    turnHelper.getTurnDAO().createOrUpdate(turn);
                                    commentHelper.getCommentDAO().create(comment);
                                    new Actions(fragmentActivity).addComment(turn,comment);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                fragmentActivity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container, new CalendarFragment())
                                        .commit();
                            }
                        }
                    }).build();
            editTextAddComment = (EditText) dialog.getCustomView().findViewById(R.id.editText_turnComment);
            dialog.show();
        }
    }

    public void deleteCommentDialog(final String dateStr,final String text) {
        ArrayList<Turn> turnArrayList = null;
        try {
            turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                    .queryBuilder()
                    .where()
                    .eq(Turn.DATE, dateStr)
                    .query();

            if (!turnArrayList.isEmpty() && turnArrayList.get(0).isContainComment()) {
                final Turn turn = turnArrayList.get(0);
                new MaterialDialog.Builder(fragmentActivity)
                        .title(R.string.delete_comment_title)
                        .content(R.string.delete_comment)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.disagree)
                        .callback(new MaterialDialog.ButtonCallback() {
                                      @Override
                                      public void onPositive(MaterialDialog dialog) {
                                          try {
                                              ArrayList<Comment> commentArrayList = (ArrayList<Comment>) commentHelper.getCommentDAO()
                                                      .queryBuilder()
                                                      .where()
                                                      .eq(Comment.DATE, dateStr)
                                                      .query();
                                              if (!commentArrayList.isEmpty()) {
                                                  Comment cmt = null;
                                                  for (Comment comment : commentArrayList) {
                                                      if (comment.getText().equals(text)){
                                                          cmt = comment;
                                                      }
                                                  }
                                                  commentHelper.getCommentDAO().delete(cmt);
                                                  if (commentArrayList.size() == 1) {
                                                      turn.setContainComment(false);
                                                      turnHelper.getTurnDAO().createOrUpdate(turn);
                                                  }
                                                  new Actions(fragmentActivity).delComment(turn,cmt);
                                              }
                                          } catch (SQLException e) {
                                              e.printStackTrace();
                                          }
                                          fragmentActivity.getSupportFragmentManager()
                                                  .beginTransaction()
                                                  .replace(R.id.container, new CalendarFragment())
                                                  .commit();
                                      }
                                  }
                        )
                        .show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
