package com.naddiaz.workshift.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naddiaz.workshift.R;

import java.util.ArrayList;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class DetailListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DetailItem> detailItemArrayList;

    public DetailListAdapter(Context context, ArrayList<DetailItem> detailItemArrayList) {
        this.context = context;
        this.detailItemArrayList = detailItemArrayList;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_detail_turn, null, true);
        TextView textViewItemTitle = (TextView) v.findViewById(R.id.textView_title);
        TextView textViewItemDetail = (TextView) v.findViewById(R.id.textView_detail);
        ImageView imageViewIcon = (ImageView) v.findViewById(R.id.imageView_icon);
        DetailItem detailItem = detailItemArrayList.get(i);
        if(detailItem.getDetail() == null){
            textViewItemDetail.setVisibility(View.GONE);
        }
        else{
            textViewItemDetail.setText(detailItem.getDetail());
        }
        textViewItemTitle.setText(detailItem.getTitle());
        imageViewIcon.setImageDrawable(context.getResources().getDrawable(detailItem.getIcon()));
        return v;
    }
}
