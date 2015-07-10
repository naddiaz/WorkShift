package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by NESTOR on 21/06/2015.
 */
@DatabaseTable(tableName = "Dubbing")
public class Dubbing {

    public static final String TURN = "turn";
    public static final String DATE = "date";


    @DatabaseField(canBeNull = false, columnName = TURN)
    private int turn;
    @DatabaseField(id = true, canBeNull = false, columnName = DATE)
    String date;

    public Dubbing() {
    }

    public Dubbing(int turn, int day, int month, int year) {
        this.turn = turn;
        this.date = year + "." + month + "." + day;
    }

    @Override
    public String toString() {
        return "Turn{" +
                "turn=" + turn +
                ", date='" + date + '\'' +
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

}