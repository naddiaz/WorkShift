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

import model.Change;
import model.Comment;
import model.Dubbing;
import model.Turn;
import model.helpers.ChangeHelper;
import model.helpers.CommentHelper;
import model.helpers.DatabaseHelper;
import model.helpers.DubbingHelper;
import model.helpers.TurnHelper;
import utils.Crypto;
import utils.Preferences;
import utils.Token;

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
    ChangeHelper changeHelper;
    DubbingHelper dubbingHelper;
    CommentHelper commentHelper;

    public Sync(Context context) {
        this.context = context;
        preferences = new Preferences(context);
        databaseHelper = new DatabaseHelper(context);
    }

    public Sync(Context context,LinearLayout linearLayout) {
        this.context = context;
        this.linearLayout = linearLayout;
        preferences = new Preferences(context);
        databaseHelper = new DatabaseHelper(context);
    }

    public void all(){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();

        int token = new Token(context).generateTurnToken();
        Log.i("TOKEN", String.valueOf(token));

        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put(Preferences.TURN_TOKEN, String.valueOf(token));

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsSyncTurns, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("status")){
                            try {
                                if(response.getBoolean("status")){
                                    Log.i(TAG, response.toString());
                                    if(linearLayout != null)
                                        linearLayout.setVisibility(View.GONE);
                                }
                                else{
                                    Log.i(TAG,response.toString());
                                    preferences.setTurnToken(response.getString("token"));
                                    preferences.save();
                                    updateLocalTurns(response.getString("turns"));
                                    updateLocalChanges(response.getString("changes"));
                                    updateLocalDubbings(response.getString("dubbings"));
                                    updateLocalComments(response.getString("comments"));
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
                SuperToast.create(context, context.getString(R.string.error_login_timeout), SuperToast.Duration.LONG,
                        Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
                if(linearLayout != null)
                    linearLayout.setVisibility(View.GONE);
            }
        });
        requestQueue.add(request);
    }

    private void updateLocalTurns(String turns) throws JSONException {
        turnHelper = new TurnHelper(databaseHelper);

        JSONArray jsonArray = new JSONArray(turns);
        Log.i(TAG, jsonArray.toString());
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

    private void updateLocalChanges(String changes) throws JSONException {
        changeHelper = new ChangeHelper(databaseHelper);

        JSONArray jsonArray = new JSONArray(changes);
        Log.i(TAG, jsonArray.toString());
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Change change = new Change();
            change.setDate(jsonObject.getString(Change.DATE));
            change.setTurn(jsonObject.getInt(Change.TURN));
            try {
                changeHelper.getChangeDAO().createOrUpdate(change);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLocalDubbings(String dubbings) throws JSONException {
        dubbingHelper = new DubbingHelper(databaseHelper);

        JSONArray jsonArray = new JSONArray(dubbings);
        Log.i(TAG, jsonArray.toString());
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Dubbing dubbing = new Dubbing();
            dubbing.setDate(jsonObject.getString(Dubbing.DATE));
            dubbing.setTurn(jsonObject.getInt(Dubbing.TURN));
            try {
                dubbingHelper.getDubbingDAO().createOrUpdate(dubbing);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLocalComments(String comments) throws JSONException {
        commentHelper = new CommentHelper(databaseHelper);

        JSONArray jsonArray = new JSONArray(comments);
        Log.i(TAG, jsonArray.toString());
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Comment comment = new Comment();
            comment.setDate(jsonObject.getString(Comment.DATE));
            comment.setText(jsonObject.getString(Comment.TEXT));
            try {
                commentHelper.getCommentDAO().createOrUpdate(comment);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
