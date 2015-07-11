package com.naddiaz.workshift.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.naddiaz.workshift.R;

import java.sql.SQLException;
import java.util.Calendar;

import model.Turn;
import model.helpers.DatabaseHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 21/06/2015.
 */
public class MonthAddFragment extends Fragment {

    private static final String MONTH_ADD_FRAGMENT = "MonthAddFragment";
    DatabaseHelper databaseHelper;
    TurnHelper turnHelper;

    protected int selectedYear;
    protected int selectedMonth;
    public static final int TYPE_YEAR = 0;
    public static final int TYPE_MONTH = 1;

    public static MonthAddFragment newInstance(String text){
        MonthAddFragment mFragment = new MonthAddFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(MONTH_ADD_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_month_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.title_help)
                        .content(R.string.content_help_create_turns)
                        .positiveText(R.string.agree)
                        .show();
                break;
            case R.id.action_save:
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.title_help)
                        .content(R.string.content_help_create_turns)
                        .positiveText(R.string.agree)
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.databaseHelper = new DatabaseHelper(getActivity());
        this.turnHelper = new TurnHelper(databaseHelper);

        View rootView = inflater.inflate(R.layout.fragment_month_add, container, false);

        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR);

        final String[] years = {String.valueOf(current_year-1),String.valueOf(current_year),String.valueOf(current_year+1)};
        final TextView addYear = (TextView) rootView.findViewById(R.id.textView_addYear);
        addYear.setOnClickListener(
                new SelectedDialogClickListener(R.string.year_create_turns,years,addYear,TYPE_YEAR));

        ImageButton addYearButton = (ImageButton) rootView.findViewById(R.id.imageButton_addYear);
        addYearButton.setOnClickListener(
                new SelectedDialogClickListener(R.string.year_create_turns,years,addYear,TYPE_YEAR));

        final String[] months = getResources().getStringArray(R.array.full_months);
        final TextView addMonth = (TextView) rootView.findViewById(R.id.textView_addMonth);
        addMonth.setOnClickListener(
                new SelectedDialogClickListener(R.string.month_create_turns,months,addMonth,TYPE_MONTH));

        ImageButton addMonthButton = (ImageButton) rootView.findViewById(R.id.imageButton_addMonth);
        addMonthButton.setOnClickListener(
                new SelectedDialogClickListener(R.string.month_create_turns,months,addMonth,TYPE_MONTH));

        final EditText ed_sequence = (EditText) rootView.findViewById(R.id.ed_sequence);

        ImageButton ib_save = (ImageButton) rootView.findViewById(R.id.ib_save);
        ib_save.setOnClickListener(new SaveClickListener(ed_sequence));

        return rootView;
    }

    private class SelectedDialogClickListener implements View.OnClickListener{

        private int title;
        private String[] items;
        private TextView textView;
        private int type;

        public SelectedDialogClickListener(int title, String[] items, TextView textView, int type) {
            this.title = title;
            this.items = items;
            this.textView = textView;
            this.type = type;
        }

        @Override
        public void onClick(View view) {
            new MaterialDialog.Builder(getActivity())
                    .title(title)
                    .items(items)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            textView.setText(items[which]);
                            if(type == TYPE_YEAR)
                                selectedYear = Integer.parseInt(items[which]);
                            if(type == TYPE_MONTH)
                                selectedMonth = which + 1;
                        }
                    })
                    .show();
        }
    }

    private class SaveClickListener implements View.OnClickListener{

        private EditText editText;

        public SaveClickListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onClick(View view) {
            String sequence = editText.getText().toString().toUpperCase();
            Log.i(MONTH_ADD_FRAGMENT,sequence);
            int year = selectedYear;
            int month = selectedMonth;
            for(int i=0; i<sequence.length(); i++){
                try {
                    Turn turn = null;
                    if (getResources().getString(R.string.morning).charAt(0) == sequence.charAt(i))
                        turn = new Turn(Turn.MORNING, (i + 1), (month), year);
                    else if (getResources().getString(R.string.afternoon).charAt(0) == sequence.charAt(i))
                        turn = new Turn(Turn.AFTERNOON, (i + 1), (month), year);
                    else if (getResources().getString(R.string.evening).charAt(0) == sequence.charAt(i))
                        turn = new Turn(Turn.EVENING, (i + 1), (month), year);
                    else if (getResources().getString(R.string.unoccupied).charAt(0) == sequence.charAt(i))
                        turn = new Turn(Turn.UNOCCUPIED, (i + 1), (month), year);
                    else if (getResources().getString(R.string.outgoing).charAt(0) == sequence.charAt(i))
                        turn = new Turn(Turn.OUTGOING, (i + 1), (month), year);
                    else if (getResources().getString(R.string.holiday).charAt(0) == sequence.charAt(i))
                        turn = new Turn(Turn.HOLIDAY, (i + 1), (month), year);
                    else if (getResources().getString(R.string.festive).charAt(0) == sequence.charAt(i))
                        turn = new Turn(Turn.FESTIVE, (i + 1), (month), year);
                    if (turn != null) {
                        turn.setTurnActual(turn.getTurnOriginal());
                        turnHelper.getTurnDAO().createOrUpdate(turn);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_side_left, R.anim.slide_side_rigth)
                    .replace(R.id.container, new CalendarFragment()).commit();
        }
    }
}