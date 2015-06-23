package com.naddiaz.workshift.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naddiaz.workshift.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Info;
import model.Turn;
import model.helpers.DatabaseHelper;
import model.helpers.InfoHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class DetailListAdapter extends BaseAdapter {

    private FragmentActivity fragmentActivity;
    private ArrayList<DetailItem> detailItemArrayList;
    private CalendarDay selectedDay;

    public static DatabaseHelper databaseHelper;
    public static TurnHelper turnHelper;
    public static InfoHelper infoHelper;

    public DetailListAdapter(FragmentActivity fragmentActivity, ArrayList<DetailItem> detailItemArrayList, CalendarDay selectedDay) {
        this.fragmentActivity = fragmentActivity;
        this.detailItemArrayList = detailItemArrayList;
        this.selectedDay = selectedDay;

        databaseHelper = new DatabaseHelper(fragmentActivity);
        turnHelper = new TurnHelper(databaseHelper);
        infoHelper = new InfoHelper(databaseHelper);
    }

    @Override
    public int getCount() {
        return detailItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return detailItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(this.fragmentActivity);
        View v = inflater.inflate(R.layout.item_detail_turn, null, true);
        LinearLayout linearLayoutContainer = (LinearLayout) v.findViewById(R.id.linearLayout_container);
        TextView textViewItemTitle = (TextView) v.findViewById(R.id.textView_title);
        TextView textViewItemDetail = (TextView) v.findViewById(R.id.textView_detail);
        ImageView imageViewIcon = (ImageView) v.findViewById(R.id.imageView_icon);
        ImageView imageViewEdit = (ImageView) v.findViewById(R.id.imageView_edit);
        DetailItem detailItem = detailItemArrayList.get(i);
        if(detailItem.getDetail() == null){
            textViewItemDetail.setVisibility(View.GONE);
        }
        else{
            textViewItemDetail.setText(detailItem.getDetail());
        }
        textViewItemTitle.setText(detailItem.getTitle());
        imageViewIcon.setImageDrawable(this.fragmentActivity.getResources().getDrawable(detailItem.getIcon()));

        String dateStr = selectedDay.getYear() + ".";
        dateStr += (selectedDay.getMonth() + 1) + ".";
        dateStr += selectedDay.getDay();

        if (detailItem.getType() == Info.DOUBLE) {
            linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "IS A DOUBLE");
                }
            });
        }
        else if (detailItem.getType() == Info.COMMENT) {
            linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "IS A COMMENT");
                }
            });
        }
        else if (detailItem.getType() == Info.CHANGE) {
            linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "IS A CHANGE");
                }
            });
        }
        else if (detailItem.getType() == Info.ACTUAL_TURN) {
            imageViewEdit.setVisibility(View.GONE);
        }
        return v;
    }
}
