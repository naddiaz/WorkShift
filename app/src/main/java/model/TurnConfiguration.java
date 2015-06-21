package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by NESTOR on 21/06/2015.
 */
@DatabaseTable(tableName = "TurnConfiguration")
public class TurnConfiguration{

    public static final String TURN = "turn";
    public static final String COLOR_START = "colorStart";
    public static final String COLOR_END = "colorEnd";

    @DatabaseField(id = true, generatedId = false, columnName = TURN)
    private int turn;
    @DatabaseField(canBeNull = false, columnName = COLOR_START)
    private String colorStart;
    @DatabaseField(columnName = COLOR_END)
    private String colorEnd;

    public TurnConfiguration(){

    }


    public TurnConfiguration(int turn, String colorStart, String colorEnd) {
        this.turn = turn;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getColorStart() {
        return colorStart;
    }

    public void setColorStart(String colorStart) {
        this.colorStart = colorStart;
    }

    public String getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(String colorEnd) {
        this.colorEnd = colorEnd;
    }

    @Override
    public String toString() {
        return "TurnConfiguration{" +
                "turn=" + turn +
                ", colorStart=" + colorStart +
                ", colorEnd=" + colorEnd +
                '}';
    }
}