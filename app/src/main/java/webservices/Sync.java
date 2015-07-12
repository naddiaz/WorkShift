package webservices;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.activity.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Turn;
import model.helpers.DatabaseHelper;
import model.helpers.TurnHelper;
import utils.Crypto;
import utils.Preferences;

/**
 * Created by NESTOR on 12/07/2015.
 */
public class Sync {

    private static final String TAG = "SYNC";

    private Context context;
    Preferences preferences;
    LinearLayout linearLayout;

    DatabaseHelper databaseHelper;
    TurnHelper turnHelper;

    public Sync(Context context) {
        this.context = context;
        preferences = new Preferences(context);
    }

    public Sync(Context context,LinearLayout linearLayout) {
        this.context = context;
        this.linearLayout = linearLayout;
        preferences = new Preferences(context);
    }

    public void turn(){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();

        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put(Preferences.TURN_TOKEN, preferences.getTurnToken());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsSyncTurns, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("status")){
                            try {
                                if(response.getBoolean("status")){
                                    Log.i(TAG,response.toString());
                                    if(linearLayout != null)
                                        linearLayout.setVisibility(View.GONE);
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    context.startActivity(intent);
                                }
                                else{
                                    Log.i(TAG,response.toString());
                                    preferences.setTurnToken(response.getString("token"));
                                    preferences.save();
                                    updateLocalDataBase(response.getString("turns"));
                                    if(linearLayout != null)
                                        linearLayout.setVisibility(View.GONE);
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    context.startActivity(intent);
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
        requestQueue.add(request);
    }

    private void updateLocalDataBase(String turns) throws JSONException {
        databaseHelper = new DatabaseHelper(context);
        turnHelper = new TurnHelper(databaseHelper);

        JSONArray jsonArray = new JSONArray(turns);
        Log.i(TAG,jsonArray.toString());
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Turn turn = new Turn();
            turn.setTurnActual(jsonObject.getInt(Turn.TURN_ACTUAL));
            turn.setTurnOriginal(jsonObject.getInt(Turn.TURN_ORIGINAL));
            turn.setMonth(jsonObject.getInt(Turn.MONTH));
            turn.setYear(jsonObject.getInt(Turn.YEAR));
            turn.setDate(jsonObject.getString(Turn.DATE));
            turn.setIsChange(jsonObject.getBoolean(Turn.IS_CHANGE));
            turn.setIsDubbing(jsonObject.getBoolean(Turn.IS_DUBBING));
            turn.setContainComment(jsonObject.getBoolean(Turn.CONTAIN_COMMENT));
            try {
                turnHelper.getTurnDAO().createOrUpdate(turn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
