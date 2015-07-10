package com.naddiaz.workshift.ui.adapter;

import com.naddiaz.workshift.R;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class DetailItem {

    private String title;
    private String detail;
    private int icon;

    public DetailItem() {
    }

    public DetailItem(String title, String detail, int icon) {
        this.title = title;
        this.detail = detail;
        this.icon = icon;
    }


    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getIcon() {
        return icon;
    }

}
