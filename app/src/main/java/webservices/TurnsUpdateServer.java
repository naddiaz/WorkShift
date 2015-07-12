package webservices;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.naddiaz.workshift.ui.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Turn;
import model.TurnConfiguration;
import model.helpers.ChangeHelper;
import model.helpers.CommentHelper;
import model.helpers.DatabaseHelper;
import model.helpers.DubbingHelper;
import model.helpers.TurnConfigurationHelper;
import model.helpers.TurnHelper;
import utils.Preferences;

/**
 * Created by NESTOR on 12/07/2015.
 */
public class TurnsUpdateServer extends AsyncTask<Void, Void, Void> {

    DatabaseHelper databaseHelper;
    TurnHelper turnHelper;
    TurnConfigurationHelper turnConfigurationHelper;
    ChangeHelper changeHelper;
    DubbingHelper dubbingHelper;
    CommentHelper commentHelper;

    Preferences preferences;
    Context context;

    public TurnsUpdateServer(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        turnConfigurationHelper = new TurnConfigurationHelper(databaseHelper);
        turnHelper = new TurnHelper(databaseHelper);
        changeHelper = new ChangeHelper(databaseHelper);
        dubbingHelper = new DubbingHelper(databaseHelper);
        commentHelper = new CommentHelper(databaseHelper);
        preferences = new Preferences(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();

        try {
            ArrayList<Turn> turns = (ArrayList<Turn>) turnHelper.getTurnDAO().queryForAll();
            requestQueue.add(turnUpdate(turns));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CustomRequest turnUpdate(ArrayList<Turn> turns){
        Map<String, String> params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put("turns", turns.toString());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsTurnsUpdate, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("status")){
                            try {
                                if(response.getBoolean("status")){
                                    Log.i("ALL", response.toString());
                                    preferences.setTurnToken(response.getString("token"));
                                    preferences.save();
                                }
                                else{
                                    Log.i("ALL", response.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        return request;
    }
}