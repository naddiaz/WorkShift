package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by NESTOR on 21/06/2015.
 */
@DatabaseTable(tableName = "Comment")
public class Comment {

    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String TEXT = "text";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(canBeNull = false, columnName = DATE)
    private String date;
    @DatabaseField(canBeNull = false, columnName = TEXT)
    private String text;

    public Comment() {
    }

    public Comment(String date, String text) {
        this.date = date;
        this.text = text;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ",\"text\":\"" + text + '\"' +
                ",\"date\":\"" + date + '\"' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}