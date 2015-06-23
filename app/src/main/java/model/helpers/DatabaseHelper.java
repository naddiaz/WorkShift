package model.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.lang.*;
import java.sql.SQLException;

import model.Info;
import model.Turn;
import model.TurnConfiguration;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "work_shift.db";
    private static final int DATABASE_VERSION = 4;


    public DatabaseHelper(Context context) {
        super(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Turn.class);
            TableUtils.createTable(connectionSource, TurnConfiguration.class);
            TableUtils.createTable(connectionSource, Info.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,Turn.class,true);
            TableUtils.dropTable(connectionSource,TurnConfiguration.class,true);
            TableUtils.dropTable(connectionSource,Info.class,true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    @Override
    public void close() {
        super.close();
    }
}