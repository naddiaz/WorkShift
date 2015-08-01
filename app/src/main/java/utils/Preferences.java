package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.naddiaz.workshift.ui.activity.HomeActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import model.Turn;
import model.helpers.ChangeHelper;
import model.helpers.CommentHelper;
import model.helpers.DatabaseHelper;
import model.helpers.DubbingHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;

/**
 * Created by NESTOR on 12/07/2015.
 */
public class Preferences {
    public static final String NAME = "name";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String IS_LOGIN = "isLogin";
    public static final String TURN_TOKEN = "turnToken";
    public static final String CHANGE_TOKEN = "changeToken";
    public static final String DUBBING_TOKEN = "dubbingToken";
    public static final String COMMENT_TOKEN = "commentToken";
    public static final String CONFIG_TOKEN = "configToken";
    public static final String TURN_UPDATE = "turnUpdate";
    public static final String CHANGE_UPDATE = "changeUpdate";
    public static final String DUBBING_UPDATE = "dubbingUpdate";
    public static final String COMMENT_UPDATE = "commentUpdate";
    public static final String CONFIG_UPDATE = "configUpdate";


    Context context;

    public static DatabaseHelper databaseHelper;
    public static TurnConfigurationHelper turnConfigurationHelper;
    public static TurnHelper turnHelper;
    public static ChangeHelper changeHelper;
    public static DubbingHelper dubbingHelper;
    public static CommentHelper commentHelper;

    private String name;
    private String lastName;
    private String email;
    private Boolean isLogin;
    private String turnToken;
    private String changeToken;
    private String dubbingToken;
    private String commentToken;
    private String configToken;
    private int turnUpdate;
    private int changeUpdate;
    private int dubbingUpdate;
    private int commentUpdate;
    private int configUpdate;

    public Preferences(Context context) {
        this.context = context;

        databaseHelper = new DatabaseHelper(context);
        turnConfigurationHelper = new TurnConfigurationHelper(databaseHelper);
        turnHelper = new TurnHelper(databaseHelper);
        changeHelper = new ChangeHelper(databaseHelper);
        dubbingHelper = new DubbingHelper(databaseHelper);
        commentHelper = new CommentHelper(databaseHelper);

        this.read();
    }

    public boolean save(){
        final SharedPreferences prefs = preferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NAME, this.name);
        editor.putString(LAST_NAME, this.lastName);
        editor.putString(EMAIL, this.email);
        editor.putBoolean(IS_LOGIN, this.isLogin);
        return editor.commit();
    }

    public void read(){
        final SharedPreferences prefs = preferences();
        this.name = prefs.getString(NAME,null);
        this.lastName = prefs.getString(LAST_NAME,null);
        this.email = prefs.getString(EMAIL,null);
        this.isLogin = prefs.getBoolean(IS_LOGIN, false);
        this.turnToken = prefs.getString(TURN_TOKEN, "");
        this.changeToken = prefs.getString(CHANGE_TOKEN,"");
        this.dubbingToken = prefs.getString(DUBBING_TOKEN,"");
        this.commentToken = prefs.getString(COMMENT_TOKEN,"");
        this.configToken = prefs.getString(CONFIG_TOKEN,"");
        this.turnUpdate = prefs.getInt(TURN_UPDATE, 0);
        this.changeUpdate = prefs.getInt(CHANGE_UPDATE, 0);
        this.dubbingUpdate = prefs.getInt(DUBBING_UPDATE, 0);
        this.commentUpdate = prefs.getInt(COMMENT_UPDATE, 0);
        this.configUpdate = prefs.getInt(CONFIG_UPDATE, 0);
    }

    private SharedPreferences preferences(){
        return this.context.getSharedPreferences(HomeActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getTurnToken() {
        return turnToken;
    }

    public void setTurnToken(String turnToken) {
        this.turnToken = turnToken;
    }

    public String getChangeToken() {
        return changeToken;
    }

    public void setChangeToken(String changeToken) {
        this.changeToken = changeToken;
    }

    public String getDubbingToken() {
        return dubbingToken;
    }

    public void setDubbingToken(String dubbingToken) {
        this.dubbingToken = dubbingToken;
    }

    public String getCommentToken() {
        return commentToken;
    }

    public void setCommentToken(String commentToken) {
        this.commentToken = commentToken;
    }

    public String getConfigToken() {
        return configToken;
    }

    public void setConfigToken(String configToken) {
        this.configToken = configToken;
    }

    public int getTurnUpdate() {
        return turnUpdate;
    }

    public void setTurnUpdate(int turnUpdate) {
        this.turnUpdate = turnUpdate;
    }

    public int getChangeUpdate() {
        return changeUpdate;
    }

    public void setChangeUpdate(int changeUpdate) {
        this.changeUpdate = changeUpdate;
    }

    public int getDubbingUpdate() {
        return dubbingUpdate;
    }

    public void setDubbingUpdate(int dubbingUpdate) {
        this.dubbingUpdate = dubbingUpdate;
    }

    public int getCommentUpdate() {
        return commentUpdate;
    }

    public void setCommentUpdate(int commentUpdate) {
        this.commentUpdate = commentUpdate;
    }

    public int getConfigUpdate() {
        return configUpdate;
    }

    public void setConfigUpdate(int configUpdate) {
        this.configUpdate = configUpdate;
    }
}
