package model.helpers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import model.*;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class DubbingHelper {

    private DatabaseHelper databaseHelper;
    private Dao<Dubbing, Integer> dubbingDAO;

    public DubbingHelper(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public Dao<Dubbing, Integer> getDubbingDAO() throws SQLException {
        if (dubbingDAO == null) {
            dubbingDAO = databaseHelper.getDao(Dubbing.class);
        }
        return dubbingDAO;
    }

}
