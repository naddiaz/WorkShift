package model.helpers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import model.Info;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class InfoHelper {

    private DatabaseHelper databaseHelper;
    private Dao<Info, Integer> infoDAO;

    public InfoHelper(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public Dao<Info, Integer> getInfoDAO() throws SQLException {
        if (infoDAO == null) {
            infoDAO = databaseHelper.getDao(Info.class);
        }
        return infoDAO;
    }
}