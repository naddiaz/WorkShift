package com.naddiaz.workshift.ui.fragment;

import android.os.AsyncTask;
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
import com.naddiaz.workshift.ui.adapter.DetailItem;
import com.naddiaz.workshift.ui.adapter.DetailListAdapter;
import com.naddiaz.workshift.ui.decorators.Decorator;
import com.naddiaz.workshift.ui.dialog.CalendarDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import model.Info;
import model.Turn;
import model.TurnConfiguration;
import model.helpers.DatabaseHelper;
import model.helpers.InfoHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;


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
    TextView textViewLoadData;
    TextView textViewLoadDetail;

    public static DatabaseHelper databaseHelper;
    public static TurnConfigurationHelper turnConfigurationHelper;
    public static TurnHelper turnHelper;
    public static InfoHelper infoHelper;

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
        textViewLoadData = (TextView) rootView.findViewById(R.id.textView_loadData);
        textViewLoadDetail = (TextView) rootView.findViewById(R.id.textView_loadDetail);
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
        detailListAdapter = new DetailListAdapter(getActivity(), detailItemArrayList, calendarView.getSelectedDate());
        listViewDetail.setAdapter(detailListAdapter);

        loadActualMonth(calendarView.getCurrentDate());
        loadActualDay(calendarView.getSelectedDate());

        rootView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT ));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(CALENDAR_FRAGMENT,"onActivityCreated");
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
        loadActualDay(calendarView.getSelectedDate());
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
                    dayViewFacade.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.md_grey_200)));
                }
            });
            new BackgroundDecorator(calendarView.getCurrentDate().getYear(),calendarView.getCurrentDate().getMonth())
                    .executeOnExecutor(Executors.newSingleThreadExecutor());
        }
    }

    private class BackgroundDecorator extends AsyncTask<Void, Void, HashMap<Integer, ArrayList<Date>>> {

        private int month;
        private int year;
        private ArrayList<TurnConfiguration> turnConfigurations;

        public BackgroundDecorator(int year, int month) {
            this.month = month + 1;
            this.year = year;
            databaseHelper = new DatabaseHelper(getActivity());
            turnConfigurationHelper = new TurnConfigurationHelper(databaseHelper);
            turnHelper = new TurnHelper(databaseHelper);
        }

        @Override
        protected HashMap<Integer, ArrayList<Date>> doInBackground(Void... voids) {
            ArrayList<Turn> turns = null;
            HashMap<Integer, ArrayList<Date>> turnHashMap = new HashMap<>();
            try {
                turns = (ArrayList<Turn>) turnHelper.getTurnDAO()
                        .queryBuilder()
                        .where()
                        .eq(Turn.MONTH,month)
                        .and()
                        .eq(Turn.YEAR,year)
                        .query();
                this.turnConfigurations = (ArrayList<TurnConfiguration>) turnConfigurationHelper.getTurnConfigurationDAO()
                        .queryBuilder()
                        .orderBy(Turn.TURN,true)
                        .query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(this.turnConfigurations.isEmpty()){
                try {
                    turnConfigurationHelper.loadDefaultConfiguration();
                    this.turnConfigurations = (ArrayList<TurnConfiguration>) turnConfigurationHelper.getTurnConfigurationDAO()
                            .queryBuilder()
                            .orderBy(Turn.TURN,true)
                            .query();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for(int i=0; i<Turn.TURN_TYPES; i++) {
                ArrayList<Date> tmp = new ArrayList<>();
                for (Turn turn : turns) {
                    if(turn.getTurn() == i){
                        DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
                        Date date = null;
                        try {
                            date = format.parse(turn.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(date != null) {
                            tmp.add(date);
                        }
                    }
                }
                turnHashMap.put(Integer.valueOf(i),tmp);
            }
            return turnHashMap;
        }

        @Override
        protected void onPreExecute() {
            textViewLoadData.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(HashMap<Integer, ArrayList<Date>> turnHashMap) {
            super.onPostExecute(turnHashMap);
            for(Map.Entry<Integer,ArrayList<Date>> entry : turnHashMap.entrySet()){
                if(!entry.getValue().isEmpty()) {
                    TurnConfiguration config = this.turnConfigurations.get(entry.getKey());
                    calendarView.addDecorator(new Decorator(entry.getValue()).generateBackgroundDrawable(config.getColorStart(), config.getColorEnd()));
                }
            }
            textViewLoadData.setVisibility(View.GONE);
        }
    }

    private void loadActualDay(CalendarDay calendarDay){
        new DetailLoader(calendarDay)
                .executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private class DetailLoader extends AsyncTask<Void, Void, Turn> {

        private CalendarDay calendarDay;
        ArrayList<DetailItem> detailItemArrayList = new ArrayList<>();

        public DetailLoader(CalendarDay calendarDay) {
            this.calendarDay = calendarDay;

            databaseHelper = new DatabaseHelper(getActivity());
            turnConfigurationHelper = new TurnConfigurationHelper(databaseHelper);
            turnHelper = new TurnHelper(databaseHelper);
            infoHelper = new InfoHelper(databaseHelper);
        }

        @Override
        protected Turn doInBackground(Void... voids) {
            Turn turn = null;
            try {
                String dateStr = calendarDay.getYear() + ".";
                dateStr += (calendarDay.getMonth() + 1) + ".";
                dateStr += calendarDay.getDay();

                Log.i("TAG",dateStr);

                ArrayList<Turn> turnArrayList = (ArrayList<Turn>) turnHelper.getTurnDAO()
                        .queryBuilder()
                        .where()
                        .eq(Turn.DATE, dateStr)
                        .query();
                Log.i("TAG",turnArrayList.toString());
                if(!turnArrayList.isEmpty()){
                    turn = turnArrayList.get(0);
                    String[] turns = getResources().getStringArray(R.array.turns_names);
                    this.detailItemArrayList.add(new DetailItem().setActualTurn("Turno: " + turns[turn.getTurn()].toUpperCase()));
                }
                else{
                    this.detailItemArrayList.add(new DetailItem().setActualTurn("No se ha definido el turno para este día"));
                }

                ArrayList<Info> infoArrayList =(ArrayList<Info>) infoHelper.getInfoDAO()
                        .queryBuilder()
                        .orderBy(Info.TYPE,true)
                        .where()
                        .eq(Turn.DATE, dateStr)
                        .query();
                if(!infoArrayList.isEmpty()){
                    for(Info info: infoArrayList) {
                        String[] turns = getResources().getStringArray(R.array.turns_names);
                        if(info.getType() == Info.DOUBLE) {
                            this.detailItemArrayList.add(new DetailItem().addDoubleTurn("Doblaje: " + turns[info.getTurn()].toUpperCase()));
                        }
                        else if(info.getType() == Info.COMMENT) {
                            this.detailItemArrayList.add(new DetailItem().addComment(info.getTitle()));
                        }
                        else if(info.getType() == Info.CHANGE) {
                            this.detailItemArrayList.add(new DetailItem().addChangeTurn(info.getTitle(),info.getDetail()));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return turn;
        }

        @Override
        protected void onPreExecute() {
            //textViewLoadDetail.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Turn turn) {
            super.onPostExecute(turn);
            detailListAdapter = new DetailListAdapter(getActivity(), this.detailItemArrayList, this.calendarDay);
            listViewDetail.setAdapter(detailListAdapter);
            detailListAdapter.notifyDataSetChanged();
            //textViewLoadDetail.setVisibility(View.GONE);
        }
    }
    private void defineFloatingButtonActions(View rootView){

        FloatingActionButton fab_changeTurn = (FloatingActionButton) rootView.findViewById(R.id.fab_change_turn);

        fab_changeTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                new CalendarDialog(getActivity(),calendarView).showChangeTurnDialog();
            }
        });

        FloatingActionButton fab_addTurn = (FloatingActionButton) rootView.findViewById(R.id.fab_add_turn);

        fab_addTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                new CalendarDialog(getActivity(),calendarView).showDoubleTurnDialog();
            }
        });

        FloatingActionButton fab_addComment = (FloatingActionButton) rootView.findViewById(R.id.fab_add_comment);
        fab_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                new CalendarDialog(getActivity(),calendarView).showCommentDialog();
            }
        });
    }

}
