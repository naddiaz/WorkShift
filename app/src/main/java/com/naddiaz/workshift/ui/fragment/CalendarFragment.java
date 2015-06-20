package com.naddiaz.workshift.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.adapter.DetailListAdapter;
import com.naddiaz.workshift.ui.dialog.CalendarDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.naddiaz.workshift.ui.adapter.DetailItem;


public class CalendarFragment extends Fragment implements OnDateChangedListener, OnMonthChangedListener {

    private static final String CALENDAR_FRAGMENT = "CalendarFragment";
    private static final String SELECTED_CALENDAR_DAY = "selected_calendar_day";
    private static final String SELECTED_CALENDAR_MONTH = "selected_calendar_month";
    private static final String SELECTED_CALENDAR_YEAR = "selected_calendar_year";

    private ArrayList<CalendarDay> calendarDayArrayList = new ArrayList<>();
    ArrayList<DetailItem> detailItemArrayList = new ArrayList<>();
    DetailListAdapter detailListAdapter;

    FloatingActionMenu floatingActionMenu;
    MaterialCalendarView calendarView;
    TextView textViewDateBox;
    ListView listViewDetail;

    public static CalendarFragment newInstance(String text){
        CalendarFragment mFragment = new CalendarFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(CALENDAR_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        RelativeLayout relativeLayoutContainer = (RelativeLayout) rootView.findViewById(R.id.container);
        relativeLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionMenu.close(true);
            }
        });
        floatingActionMenu = (FloatingActionMenu) rootView.findViewById(R.id.fam_options);
        defineFloatingButtonActions(rootView);
        calendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        textViewDateBox = (TextView) rootView.findViewById(R.id.tv_date_box);
        listViewDetail = (ListView) rootView.findViewById(R.id.listView_detailTurn);

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);


        Calendar calendar = Calendar.getInstance();
        if(savedInstanceState != null){
            String dateStr = "";
            if(savedInstanceState.getInt(SELECTED_CALENDAR_DAY) < 10){
                dateStr += "0" + savedInstanceState.getInt(SELECTED_CALENDAR_DAY);
            }
            else{
                dateStr += savedInstanceState.getInt(SELECTED_CALENDAR_DAY);
            }
            if(savedInstanceState.getInt(SELECTED_CALENDAR_MONTH) < 10){
                dateStr += ".0" + savedInstanceState.getInt(SELECTED_CALENDAR_MONTH);
            }
            else{
                dateStr += "." + savedInstanceState.getInt(SELECTED_CALENDAR_MONTH);
            }
            dateStr += "." + savedInstanceState.getInt(SELECTED_CALENDAR_YEAR);
            Log.i(CALENDAR_FRAGMENT,dateStr);
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = null;
            try {
                date = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(date != null){
                calendarView.setSelectedDate(date);
            }
            else{
                calendarView.setSelectedDate(calendar.getTime());
            }
            Log.i(CALENDAR_FRAGMENT,date.toString());
        }
        else{
            calendarView.setSelectedDate(calendar.getTime());
        }
        Log.i(CALENDAR_FRAGMENT,calendarView.getSelectedDate() + "");

        textViewDateBox.setText(calendarView.getSelectedDate().getDay() + " "
                + getResources().getStringArray(R.array.full_months)[calendarView.getSelectedDate().getMonth()] + " "
                + calendarView.getSelectedDate().getYear());
        detailListAdapter = new DetailListAdapter(getActivity(), detailItemArrayList);
        listViewDetail.setAdapter(detailListAdapter);

        loadActualMonth(calendarView.getCurrentDate());

        rootView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT ));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SELECTED_CALENDAR_DAY, calendarView.getSelectedDate().getDay());
        savedInstanceState.putInt(SELECTED_CALENDAR_MONTH, calendarView.getSelectedDate().getMonth() + 1);
        savedInstanceState.putInt(SELECTED_CALENDAR_YEAR, calendarView.getSelectedDate().getYear());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDateChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(calendarDay.getDate().after(date) || calendarDay.getDate().equals(date)) {
            floatingActionMenu.showMenuButton(true);
        }
        else{
            floatingActionMenu.hideMenuButton(true);
        }
        textViewDateBox.setText(calendarDay.getDay() + " "
                + getResources().getStringArray(R.array.full_months)[calendarDay.getMonth()] + " "
                + calendarDay.getYear());
        floatingActionMenu.close(true);

        detailItemArrayList.add(new DetailItem().setActualTurn("Turno: TARDE y NOCHE"));
        detailItemArrayList.add(new DetailItem().addChangeTurn("Actual: TARDE","Anterior: MAÑANA"));
        detailItemArrayList.add(new DetailItem("Doblaje: NOCHE",null,DetailItem.ACTION_DOUBLE_TURN));
        detailItemArrayList.add(new DetailItem("CAMBIO CON MARIA DEL CARMEN EL DÍA 22",null,DetailItem.ACTION_COMMENT));
        detailListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
        loadActualMonth(calendarDay);
        floatingActionMenu.close(true);
    }


    private void loadActualMonth(CalendarDay calendarDay){
        final int month = calendarDay.getMonth();
        if(!calendarDayArrayList.contains(calendarDay)){
            calendarDayArrayList.add(calendarDay);
            calendarView.addDecorators(new DayViewDecorator() {
                @Override
                public boolean shouldDecorate(CalendarDay calendarDay) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (month == calendarDay.getMonth() && calendarDay.getDate().before(date)) {
                        return true;
                    }
                    return false;
                }

                @Override
                public void decorate(DayViewFacade dayViewFacade) {
                    dayViewFacade.addSpan(new StrikethroughSpan());
                    dayViewFacade.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.md_grey_400)));
                }
            });
        }
    }

    private void defineFloatingButtonActions(View rootView){

        FloatingActionButton fab_define_turns = (FloatingActionButton) rootView.findViewById(R.id.fab_change_turn);

        fab_define_turns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                new CalendarDialog(getActivity()).showChangeTurnDialog();
            }
        });

        FloatingActionButton fab_enter_month = (FloatingActionButton) rootView.findViewById(R.id.fab_add_turn);

        fab_enter_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
            }
        });

        FloatingActionButton fab_discard_interval = (FloatingActionButton) rootView.findViewById(R.id.fab_add_comment);
        fab_discard_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
            }
        });
    }

}
