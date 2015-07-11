package model.helpers;

import com.j256.ormlite.dao.Dao;
import com.naddiaz.workshift.ui.decorators.Colors;

import java.sql.SQLException;

import model.Turn;
import model.TurnConfiguration;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class TurnConfigurationHelper {

    private DatabaseHelper databaseHelper;
    private Dao<TurnConfiguration, Integer> turnConfigurationDAO;

    public TurnConfigurationHelper(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public Dao<TurnConfiguration, Integer> getTurnConfigurationDAO() throws SQLException {
        if (turnConfigurationDAO == null) {
            turnConfigurationDAO = databaseHelper.getDao(TurnConfiguration.class);
        }
        return turnConfigurationDAO;
    }

    public void loadDefaultConfiguration() throws SQLException {
        TurnConfiguration turnConfiguration = new TurnConfiguration();

        turnConfiguration.setTurn(Turn.MORNING);
        turnConfiguration.setColorStart(Colors.BROWN);
        turnConfiguration.setColorEnd(Colors.TEAL);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.AFTERNOON);
        turnConfiguration.setColorStart(Colors.AMBER);
        turnConfiguration.setColorEnd(Colors.AMBER);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.EVENING);
        turnConfiguration.setColorStart(Colors.RED);
        turnConfiguration.setColorEnd(Colors.RED);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.OUTGOING);
        turnConfiguration.setColorStart(Colors.LIGHT_BLUE);
        turnConfiguration.setColorEnd(Colors.LIGHT_BLUE);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.UNOCCUPIED);
        turnConfiguration.setColorStart(Colors.GREEN);
        turnConfiguration.setColorEnd(Colors.GREEN);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.HOLIDAY);
        turnConfiguration.setColorStart(Colors.INDIGO);
        turnConfiguration.setColorEnd(Colors.INDIGO);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.FESTIVE);
        turnConfiguration.setColorStart(Colors.BROWN);
        turnConfiguration.setColorEnd(Colors.BROWN);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.MORNING_AFTERNOON);
        turnConfiguration.setColorStart(Colors.TEAL);
        turnConfiguration.setColorEnd(Colors.AMBER);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.MORNING_EVENING);
        turnConfiguration.setColorStart(Colors.TEAL);
        turnConfiguration.setColorEnd(Colors.RED);
        this.getTurnConfigurationDAO().create(turnConfiguration);

        turnConfiguration = new TurnConfiguration();
        turnConfiguration.setTurn(Turn.AFTERNOON_EVENING);
        turnConfiguration.setColorStart(Colors.AMBER);
        turnConfiguration.setColorEnd(Colors.RED);
        this.getTurnConfigurationDAO().create(turnConfiguration);
    }
}
