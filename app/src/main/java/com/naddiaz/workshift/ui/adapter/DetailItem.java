package com.naddiaz.workshift.ui.adapter;

import com.naddiaz.workshift.R;

import model.Info;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class DetailItem {

    public static final String ACTION_CHANGE_TURN = "ic_action_change";
    public static final String ACTION_DOUBLE_TURN = "ic_action_double";
    public static final String ACTION_COMMENT = "ic_action_comment";
    public static final String ACTION_ACTUAL_TURN = "ic_action_actual";

    private String title;
    private String detail;
    private int icon;
    private int type;

    public DetailItem() {
    }

    public DetailItem(String title, String detail, String icon) {
        this.title = title;
        this.detail = detail;
        if(icon == ACTION_CHANGE_TURN) {
            this.icon = R.drawable.ic_action_change;
            this.type = Info.CHANGE;
        }
        if(icon == ACTION_DOUBLE_TURN) {
            this.icon = R.drawable.ic_action_double;
            this.type = Info.DOUBLE;
        }
        if(icon == ACTION_COMMENT) {
            this.icon = R.drawable.ic_action_comment;
            this.type = Info.COMMENT;
        }
        if(icon == ACTION_ACTUAL_TURN) {
            this.icon = R.drawable.ic_action_actual;
            this.type = Info.ACTUAL_TURN;
        }
    }

    public DetailItem setActualTurn(String turn){
        this.title = turn;
        this.detail = null;
        this.icon = R.drawable.ic_action_actual;
        this.type = Info.ACTUAL_TURN;
        return this;
    }

    public DetailItem addChangeTurn(String actual, String last){
        this.title = actual;
        this.detail = last;
        this.icon = R.drawable.ic_action_change;
        this.type = Info.CHANGE;
        return this;
    }

    public DetailItem addDoubleTurn(String turn){
        this.title = turn;
        this.detail = null;
        this.icon = R.drawable.ic_action_double;
        this.type = Info.DOUBLE;
        return this;
    }

    public DetailItem addComment(String comment){
        this.title = comment;
        this.detail = null;
        this.icon = R.drawable.ic_action_comment;
        this.type = Info.COMMENT;
        return this;
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

    public int getType() {
        return type;
    }
}
