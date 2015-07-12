package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.naddiaz.workshift.R;

import java.util.HashMap;

/**
 * Created by NESTOR on 21/06/2015.
 */
@DatabaseTable(tableName = "Turn")
public class Turn{

    public static final int MORNING = 1;
    public static final int AFTERNOON = 2;
    public static final int EVENING = 4;
    public static final int UNOCCUPIED = 7;
    public static final int OUTGOING = 8;
    public static final int HOLIDAY = 9;
    public static final int FESTIVE = 10;

    //Compose turns: SUM(X,Y)
    public static final int MORNING_AFTERNOON = 3;
    public static final int MORNING_EVENING = 5;
    public static final int AFTERNOON_EVENING = 6;

    public static final int TYPES = 10;

    public static HashMap<Integer,Integer> nameFromInt = new HashMap<>();
    static {
        nameFromInt.put(MORNING, R.string.morning);
        nameFromInt.put(AFTERNOON, R.string.afternoon);
        nameFromInt.put(EVENING, R.string.evening);
        nameFromInt.put(UNOCCUPIED, R.string.unoccupied);
        nameFromInt.put(OUTGOING, R.string.outgoing);
        nameFromInt.put(HOLIDAY, R.string.holiday);
        nameFromInt.put(FESTIVE, R.string.festive);
    }

    public static HashMap<Integer,Integer> intFromName = new HashMap<>();
    static {
        intFromName.put(R.string.morning,MORNING);
        intFromName.put(R.string.afternoon,AFTERNOON);
        intFromName.put(R.string.evening,EVENING);
        intFromName.put(R.string.unoccupied,UNOCCUPIED);
        intFromName.put(R.string.outgoing,OUTGOING);
        intFromName.put(R.string.holiday,HOLIDAY);
        intFromName.put(R.string.festive,FESTIVE);
    }

    public static final int IC_CHANGE = R.drawable.ic_action_change;
    public static final int IC_DUBBING = R.drawable.ic_action_double;
    public static final int IC_COMMENT = R.drawable.ic_action_comment;
    public static final int IC_ACTUAL = R.drawable.ic_action_actual;

    public static final String TURN_ORIGINAL = "turnOriginal";
    public static final String TURN_ACTUAL = "turnActual";
    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String IS_CHANGE = "isChange";
    public static final String IS_DUBBING = "isDubbing";
    public static final String CONTAIN_COMMENT = "containComment";


    @DatabaseField(canBeNull = false, columnName = TURN_ORIGINAL)
    private int turnOriginal;
    @DatabaseField(canBeNull = false, columnName = TURN_ACTUAL)
    private int turnActual;
    @DatabaseField(id = true, canBeNull = false, columnName = DATE)
    String date;
    @DatabaseField(canBeNull = false, columnName = MONTH)
    private int month;
    @DatabaseField(canBeNull = false, columnName = YEAR)
    private int year;
    @DatabaseField(columnName = IS_CHANGE)
    private boolean isChange;
    @DatabaseField(columnName = IS_DUBBING)
    private boolean isDubbing;
    @DatabaseField(columnName = CONTAIN_COMMENT)
    private boolean containComment;

    public Turn() {
    }

    public Turn(int turn, String date) {
        this.turnOriginal = turn;
        this.date = date;
    }

    public Turn(int turn, int day, int month, int year) {
        this.turnOriginal = turn;
        this.date = year + "." + month + "." + day;
        this.year = year;
        this.month = month;
    }

    public Turn(int turn, int day, int month, int year, boolean isDubbing, boolean containComment) {
        this.turnOriginal = turn;
        this.month = month;
        this.year = year;
        this.date = year + "." + month + "." + day;
        this.isDubbing = isDubbing;
        this.containComment = containComment;
    }

    @Override
    public String toString() {
        return "{" +
                "\"turnOriginal\":" + turnOriginal +
                ",\"turnActual\":" + turnActual +
                ",\"date\":\"" + date + '\"' +
                ",\"month\":" + month +
                ",\"year\":" + year +
                ",\"isChange\":" + isChange +
                ",\"isDubbing\":" + isDubbing +
                ",\"containComment\":" + containComment +
                '}';
    }

    public int getTurnOriginal() {
        return turnOriginal;
    }

    public void setTurnOriginal(int turnOriginal) {
        this.turnOriginal = turnOriginal;
    }

    public int getTurnActual() {
        return turnActual;
    }

    public void setTurnActual(int turnActual) {
        this.turnActual = turnActual;
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

    public boolean isChange() {
        return isChange;
    }

    public void setIsChange(boolean isChange) {
        this.isChange = isChange;
    }

    public boolean isDubbing() {
        return isDubbing;
    }

    public void setIsDubbing(boolean isDubbing) {
        this.isDubbing = isDubbing;
    }

    public boolean isContainComment() {
        return containComment;
    }

    public void setContainComment(boolean containComment) {
        this.containComment = containComment;
    }


}