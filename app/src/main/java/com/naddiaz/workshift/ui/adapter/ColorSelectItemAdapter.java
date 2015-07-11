package com.naddiaz.workshift.ui.adapter;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.naddiaz.workshift.R;

/**
 * Created by NESTOR on 11/07/2015.
 */
public class ColorSelectItemAdapter extends BaseAdapter {

    String[] listColors;
    FragmentActivity fragmentActivity;

    public ColorSelectItemAdapter(FragmentActivity fragmentActivity, String[] listColors) {
        this.fragmentActivity = fragmentActivity;
        this.listColors = listColors;
    }

    @Override
    public int getCount() {
        return listColors.length;
    }

    @Override
    public Object getItem(int i) {
        return listColors[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(this.fragmentActivity);
        View v = inflater.inflate(R.layout.item_color_dialog, null, true);
        ImageView imageViewIcon = (ImageView) v.findViewById(R.id.imageView_icon);
        imageViewIcon.setBackgroundColor(Color.parseColor("#"+listColors[i]));
        return v;
    }
}
