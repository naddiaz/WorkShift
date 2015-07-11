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
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.adapter.ColorItem;
import com.naddiaz.workshift.ui.adapter.ColorListAdapter;
import com.naddiaz.workshift.ui.adapter.DetailItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import model.Turn;
import model.TurnConfiguration;
import model.helpers.DatabaseHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 11/07/2015.
 */
public class PersonalizedFragment  extends Fragment {
    private static final String PERSONALIZED_FRAGMENT = "PersonalizedFragment";

    DatabaseHelper databaseHelper;
    TurnConfigurationHelper turnConfigurationHelper;


    public static PersonalizedFragment newInstance(String text){
        PersonalizedFragment mFragment = new PersonalizedFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(PERSONALIZED_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.databaseHelper = new DatabaseHelper(getActivity());
        this.turnConfigurationHelper = new TurnConfigurationHelper(databaseHelper);
        ArrayList<TurnConfiguration> configurationArrayList = null;
        try {
            configurationArrayList = (ArrayList<TurnConfiguration>) turnConfigurationHelper.getTurnConfigurationDAO().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        View rootView = inflater.inflate(R.layout.fragment_personalized, container, false);
        ArrayList<ColorItem> colorItemArrayList = new ArrayList<>();
        if(configurationArrayList != null){
            String[] turns = getActivity().getResources().getStringArray(R.array.turns_names);
            for(TurnConfiguration turnConfiguration : configurationArrayList){
                colorItemArrayList.add(new ColorItem(turnConfiguration.getTurn(),turns[turnConfiguration.getTurn()],turnConfiguration.getColorStart(),turnConfiguration.getColorEnd()));
            }
        }
        ListView listViewColorPicker = (ListView) rootView.findViewById(R.id.listView_colorPicker);
        ColorListAdapter colorListAdapter = new ColorListAdapter(getActivity(),colorItemArrayList);
        listViewColorPicker.setAdapter(colorListAdapter);
        colorListAdapter.notifyDataSetChanged();
        return rootView;
    }
}
