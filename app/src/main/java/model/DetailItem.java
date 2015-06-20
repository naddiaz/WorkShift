package model;

import com.naddiaz.workshift.R;

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

    public DetailItem() {
    }

    public DetailItem(String title, String detail, String icon) {
        this.title = title;
        this.detail = detail;
        if(icon == ACTION_CHANGE_TURN)
            this.icon = R.drawable.ic_action_change;
        if(icon == ACTION_DOUBLE_TURN)
            this.icon = R.drawable.ic_action_double;
        if(icon == ACTION_COMMENT)
            this.icon = R.drawable.ic_action_comment;
        if(icon == ACTION_ACTUAL_TURN)
            this.icon = R.drawable.ic_action_actual;
    }

    public DetailItem setActualTurn(String turn){
        this.title = turn;
        this.detail = null;
        this.icon = R.drawable.ic_action_actual;
        return this;
    }

    public DetailItem addChangeTurn(String actual, String last){
        this.title = actual;
        this.detail = last;
        this.icon = R.drawable.ic_action_change;
        return this;
    }

    public DetailItem addDoubleTurn(String turn){
        this.title = turn;
        this.detail = null;
        this.icon = R.drawable.ic_action_double;
        return this;
    }

    public DetailItem addComment(String comment){
        this.title = comment;
        this.detail = null;
        this.icon = R.drawable.ic_action_comment;
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
}
