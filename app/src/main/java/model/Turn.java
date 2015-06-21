package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by NESTOR on 21/06/2015.
 */
@DatabaseTable(tableName = "Turn")
public class Turn{

    public static final int TURN_MORNING = 0;
    public static final int TURN_AFTERNOON = 1;
    public static final int TURN_EVENING = 2;
    public static final int TURN_UNOCCUPIED = 3;
    public static final int TURN_OUTGOING = 4;
    public static final int TURN_HOLIDAY = 5;
    public static final int TURN_FESTIVE = 6;
    public static final int TURN_MORNING_AFTERNOON = 7;
    public static final int TURN_MORNING_EVENING = 8;
    public static final int TURN_AFTERNOON_EVENING = 9;

    public static final int TURN_TYPES = 10;

    public static final String TURN = "turn";
    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";


    @DatabaseField(canBeNull = false, columnName = TURN)
    private int turn;
    @DatabaseField(id = true, canBeNull = false, columnName = DATE)
    String date;
    @DatabaseField(canBeNull = false, columnName = MONTH)
    private int month;
    @DatabaseField(canBeNull = false, columnName = YEAR)
    private int year;

    public Turn() {
    }

    public Turn(int turn, int day, int month, int year) {
        this.turn = turn;
        date = year + "." + month + "." + day;
        this.year = year;
        this.month = month;
    }

    @Override
    public String toString() {
        return "Turn{" +
                "turn=" + turn +
                ", date='" + date + '\'' +
                ", month=" + month +
                ", year=" + year +
                '}';
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}