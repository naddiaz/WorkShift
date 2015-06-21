package model.helpers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import model.Turn;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class TurnHelper {

    private DatabaseHelper databaseHelper;
    private Dao<Turn, Integer> turnDAO;

    public TurnHelper(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public Dao<Turn, Integer> getTurnDAO() throws SQLException {
        if (turnDAO == null) {
            turnDAO = databaseHelper.getDao(Turn.class);
        }
        return turnDAO;
    }

}
