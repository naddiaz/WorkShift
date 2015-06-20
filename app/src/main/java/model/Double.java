package model;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class Double {

    private DatabaseHelper databaseHelper;
    private Dao<Double, Integer> doubleDAO;

    public Double(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public Dao<Double, Integer> getDoubleDAO() throws SQLException {
        if (doubleDAO == null) {
            doubleDAO = databaseHelper.getDao(Double.class);
        }
        return doubleDAO;
    }
}