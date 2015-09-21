package com.naddiaz.workshift.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.dialog.CalendarDialog;
import com.naddiaz.workshift.ui.fragment.CalendarFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Turn;
import model.helpers.ChangeHelper;
import model.helpers.CommentHelper;
import model.helpers.DatabaseHelper;
import model.helpers.DubbingHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class DetailListAdapter extends BaseAdapter {

    private FragmentActivity fragmentActivity;
    private ArrayList<DetailItem> detailItemArrayList;
    private CalendarDay selectedDay;
    private CalendarFragment calendarFragment;
    private MaterialCalendarView calendarView;

    public static DatabaseHelper databaseHelper;
    public static TurnHelper turnHelper;
    public static ChangeHelper changeHelper;
    public static DubbingHelper dubbingHelper;
    public static CommentHelper commentHelper;

    public DetailListAdapter(FragmentActivity fragmentActivity, ArrayList<DetailItem> detailItemArrayList, CalendarDay selectedDay, CalendarFragment calendarFragment, MaterialCalendarView calendarView) {
        this.fragmentActivity = fragmentActivity;
        this.detailItemArrayList = detailItemArrayList;
        this.selectedDay = selectedDay;
        this.calendarFragment = calendarFragment;
        this.calendarView = calendarView;

        databaseHelper = new DatabaseHelper(fragmentActivity);
        turnHelper = new TurnHelper(databaseHelper);
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
        final DetailItem detailItem = detailItemArrayList.get(i);
        if(detailItem.getDetail() == null){
            textViewItemDetail.setVisibility(View.GONE);
        }
        else{
            textViewItemDetail.setText(detailItem.getDetail());
        }
        textViewItemTitle.setText(detailItem.getTitle());
        imageViewIcon.setImageDrawable(this.fragmentActivity.getResources().getDrawable(detailItem.getIcon()));

        final String dateStr = selectedDay.getYear() + "." + (selectedDay.getMonth() + 1) + "." + selectedDay.getDay();

        if (detailItem.getIcon() == Turn.IC_CHANGE) {
            linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CalendarDialog(fragmentActivity, calendarView, calendarFragment).deleteChangeDialog(dateStr);
                }
            });
        }
        else if (detailItem.getIcon() == Turn.IC_DUBBING) {
            linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CalendarDialog(fragmentActivity, calendarView, calendarFragment).deleteDoubleDialog(dateStr);
                }
            });
        }
        else if (detailItem.getIcon() == Turn.IC_COMMENT) {
            linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CalendarDialog(fragmentActivity, calendarView, calendarFragment).deleteCommentDialog(dateStr, detailItem.getTitle());
                }
            });
        }
        if (i == 0) {
            imageViewEdit.setVisibility(View.GONE);
        }
        return v;
    }
}
