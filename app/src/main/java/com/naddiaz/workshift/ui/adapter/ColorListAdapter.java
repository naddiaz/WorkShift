package com.naddiaz.workshift.ui.adapter;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.decorators.Colors;
import com.naddiaz.workshift.ui.dialog.CalendarDialog;
import com.naddiaz.workshift.ui.fragment.CalendarFragment;
import com.naddiaz.workshift.ui.fragment.PersonalizedFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Turn;
import model.TurnConfiguration;
import model.helpers.ChangeHelper;
import model.helpers.CommentHelper;
import model.helpers.DatabaseHelper;
import model.helpers.DubbingHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class ColorListAdapter extends BaseAdapter {

    private FragmentActivity fragmentActivity;
    private ArrayList<ColorItem> colorItemArrayList;
    private CalendarDay selectedDay;

    public static DatabaseHelper databaseHelper;
    public static TurnConfigurationHelper turnConfigurationHelper;

    public ColorListAdapter(FragmentActivity fragmentActivity, ArrayList<ColorItem> colorItemArrayList) {
        this.fragmentActivity = fragmentActivity;
        this.colorItemArrayList = colorItemArrayList;

        databaseHelper = new DatabaseHelper(fragmentActivity);
        turnConfigurationHelper = new TurnConfigurationHelper(databaseHelper);
    }

    @Override
    public int getCount() {
        return colorItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return colorItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(this.fragmentActivity);
        View v = inflater.inflate(R.layout.item_color_picker, null, true);
        LinearLayout linearLayoutContainer = (LinearLayout) v.findViewById(R.id.linearLayout_container);
        TextView textViewTurn = (TextView) v.findViewById(R.id.textView_turn);
        textViewTurn.setText(colorItemArrayList.get(i).getText());
        ImageView imageViewIcon = (ImageView) v.findViewById(R.id.imageView_icon);
        ArrayList<TurnConfiguration> configurationArrayList = null;
        try {
            configurationArrayList = (ArrayList<TurnConfiguration>) turnConfigurationHelper.getTurnConfigurationDAO().queryBuilder()
                    .where()
                    .eq(TurnConfiguration.TURN, colorItemArrayList.get(i).getTurn())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(configurationArrayList != null && !configurationArrayList.isEmpty()) {
            int[] colors = new int[2];
            colors[0] = Color.parseColor("#" + configurationArrayList.get(0).getColorStart());
            colors[1] = Color.parseColor("#" + configurationArrayList.get(0).getColorEnd());
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
            gd.setCornerRadius(10f);

            imageViewIcon.setBackground(gd);
            if (configurationArrayList.get(0).getTurn() != Turn.MORNING_AFTERNOON &&
                    configurationArrayList.get(0).getTurn() != Turn.MORNING_EVENING &&
                    configurationArrayList.get(0).getTurn() != Turn.AFTERNOON_EVENING) {
                final ArrayList<TurnConfiguration> finalConfigurationArrayList = configurationArrayList;
                linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(fragmentActivity)
                                .title(R.string.select_color_title)
                                .adapter(new ColorSelectItemAdapter(fragmentActivity, Colors.listColors),
                                        new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                                ArrayList<TurnConfiguration> configurationArrayList = null;
                                                try {
                                                    configurationArrayList = (ArrayList<TurnConfiguration>) turnConfigurationHelper.getTurnConfigurationDAO()
                                                            .queryForAll();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
                                                if (configurationArrayList != null) {
                                                    int count = 0;
                                                    TurnConfiguration morningTurn = null;
                                                    TurnConfiguration afternoonTurn = null;
                                                    TurnConfiguration eveningTurn = null;
                                                    for (TurnConfiguration turnConfiguration : configurationArrayList) {
                                                        Log.i("COLOR", turnConfiguration.getColorStart() + " :: " + Colors.listColors[which]);
                                                        if (turnConfiguration.getColorStart().equals(Colors.listColors[which])) {
                                                            count++;
                                                        }
                                                        if (turnConfiguration.getTurn() == Turn.MORNING)
                                                            morningTurn = turnConfiguration;
                                                        if (turnConfiguration.getTurn() == Turn.AFTERNOON)
                                                            afternoonTurn = turnConfiguration;
                                                        if (turnConfiguration.getTurn() == Turn.EVENING)
                                                            eveningTurn = turnConfiguration;
                                                    }
                                                    Log.i("COLOR COUNT", String.valueOf(count));
                                                    if (count == 0) {
                                                        TurnConfiguration turnConfiguration = new TurnConfiguration(finalConfigurationArrayList.get(0).getTurn()
                                                                , Colors.listColors[which], Colors.listColors[which]);
                                                        try {
                                                            turnConfigurationHelper.getTurnConfigurationDAO().createOrUpdate(turnConfiguration);
                                                            if (turnConfiguration.getTurn() == Turn.MORNING) {
                                                                TurnConfiguration tTmp = new TurnConfiguration(Turn.MORNING_AFTERNOON
                                                                        , Colors.listColors[which], afternoonTurn.getColorEnd());
                                                                turnConfigurationHelper.getTurnConfigurationDAO().createOrUpdate(tTmp);
                                                                tTmp = new TurnConfiguration(Turn.MORNING_EVENING
                                                                        , Colors.listColors[which], eveningTurn.getColorEnd());
                                                                turnConfigurationHelper.getTurnConfigurationDAO().createOrUpdate(tTmp);
                                                            } else if (turnConfiguration.getTurn() == Turn.AFTERNOON) {
                                                                TurnConfiguration tTmp = new TurnConfiguration(Turn.MORNING_AFTERNOON
                                                                        , morningTurn.getColorStart(), Colors.listColors[which]);
                                                                turnConfigurationHelper.getTurnConfigurationDAO().createOrUpdate(tTmp);
                                                                tTmp = new TurnConfiguration(Turn.AFTERNOON_EVENING
                                                                        , Colors.listColors[which], eveningTurn.getColorEnd());
                                                                turnConfigurationHelper.getTurnConfigurationDAO().createOrUpdate(tTmp);
                                                            } else if (turnConfiguration.getTurn() == Turn.EVENING) {
                                                                TurnConfiguration tTmp = new TurnConfiguration(Turn.MORNING_EVENING
                                                                        , morningTurn.getColorStart(), Colors.listColors[which]);
                                                                turnConfigurationHelper.getTurnConfigurationDAO().createOrUpdate(tTmp);
                                                                tTmp = new TurnConfiguration(Turn.AFTERNOON_EVENING
                                                                        , afternoonTurn.getColorStart(), Colors.listColors[which]);
                                                                turnConfigurationHelper.getTurnConfigurationDAO().createOrUpdate(tTmp);
                                                            }
                                                            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
                                                            mFragmentManager.beginTransaction()
                                                                    .replace(R.id.container, new PersonalizedFragment()).commit();
                                                        } catch (SQLException e) {
                                                            e.printStackTrace();
                                                        }
                                                        dialog.hide();
                                                    } else {
                                                        Toast.makeText(fragmentActivity, R.string.select_color_used, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        })
                                .show();
                    }
                });
            } else {
                linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(fragmentActivity, R.string.select_color_generated, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        return v;
    }
}
