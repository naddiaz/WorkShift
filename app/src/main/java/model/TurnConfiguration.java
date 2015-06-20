package model;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class TurnConfiguration{

    private DatabaseHelper databaseHelper;
    private Dao<TurnConfiguration, Integer> turnConfigurationDAO;

    public  TurnConfiguration(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public Dao<TurnConfiguration, Integer> getTurnConfigurationDAO() throws SQLException {
        if (turnConfigurationDAO == null) {
            turnConfigurationDAO = databaseHelper.getDao(TurnConfiguration.class);
        }
        return turnConfigurationDAO;
    }
}
