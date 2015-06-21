package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by NESTOR on 21/06/2015.
 */
@DatabaseTable(tableName = "Info")
public class Info {

    public static final int ACTUAL_TURN = 0;
    public static final int CHANGE = 1;
    public static final int DOUBLE = 2;
    public static final int COMMENT = 3;

    public static final String TURN = "turn";
    public static final String DATE = "date";
    public static final String TITLE = "title";
    public static final String DETAIL = "detail";
    public static final String TYPE = "type";


    @DatabaseField(canBeNull = false, columnName = TURN)
    private int turn;
    @DatabaseField(id = true, canBeNull = false, columnName = DATE)
    String date;
    @DatabaseField(canBeNull = false, columnName = TITLE)
    String title;
    @DatabaseField(columnName = DETAIL)
    String detail;
    @DatabaseField(canBeNull = false, columnName = TYPE)
    int type;

    public Info() {
    }

    public Info(int turn, String date, int type) {
        this.turn = turn;
        this.date = date;
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getTurn() {
        return turn;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getType() {
        return type;
    }
}