package utils;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Turn;
import model.helpers.ChangeHelper;
import model.helpers.CommentHelper;
import model.helpers.DatabaseHelper;
import model.helpers.DubbingHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 26/07/2015.
 */
public class Token {

    Context context;

    public static DatabaseHelper databaseHelper;
    public static TurnConfigurationHelper turnConfigurationHelper;
    public static TurnHelper turnHelper;
    public static ChangeHelper changeHelper;
    public static DubbingHelper dubbingHelper;
    public static CommentHelper commentHelper;

    public Token(Context context) {
        this.context = context;

        databaseHelper = new DatabaseHelper(context);
        turnConfigurationHelper = new TurnConfigurationHelper(databaseHelper);
        turnHelper = new TurnHelper(databaseHelper);
        changeHelper = new ChangeHelper(databaseHelper);
        dubbingHelper = new DubbingHelper(databaseHelper);
        commentHelper = new CommentHelper(databaseHelper);
    }

    private static int generate(String data){
        int hash = 0;
        for(int i=0; i<data.length(); i++){
            hash += (int) data.charAt(i);
        }
        return hash;
    }

    public int generateTurnToken(){
        ArrayList<Turn> turns = null;
        try {
            turns = (ArrayList<Turn>) turnHelper.getTurnDAO().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generate(turns.toString());
    }
}
