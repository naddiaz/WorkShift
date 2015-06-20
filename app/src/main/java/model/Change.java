package model;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class Change {

    private DatabaseHelper databaseHelper;
    private Dao<Change, Integer> changeDAO;

    public Change(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public Dao<Change, Integer> getChangeDAO() throws SQLException {
        if (changeDAO == null) {
            changeDAO = databaseHelper.getDao(Change.class);
        }
        return changeDAO;
    }
}
