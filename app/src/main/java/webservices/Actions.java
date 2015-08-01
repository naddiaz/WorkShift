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
import model.helpers.DatabaseHelper;
import model.helpers.TurnHelper;
import utils.Preferences;
import utils.Token;

/**
 * Created by NESTOR on 12/07/2015.
 */
public class Actions {

    private static final String TAG = "ACTIONS";

    private Context context;
    Preferences preferences;

    public Actions(Context context) {
        this.context = context;
        preferences = new Preferences(context);
    }

    public void addMonth(ArrayList<Turn> turnArrayList){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();


        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put("turns", turnArrayList.toString());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsActionAddMonth, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    public void addChange(Turn turn, Change change){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();


        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put("turn", turn.toString());
        params.put("change", change.toString());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsActionAddChange, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    public void delChange(Turn turn, Change change, Dubbing dubbing){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();


        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put("turn", turn.toString());
        params.put("change", change.toString());
        params.put("dubbing", dubbing.toString());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsActionDelChange, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    public void addDubbing(Turn turn, Dubbing dubbing){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();


        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put("turn", turn.toString());
        params.put("dubbing", dubbing.toString());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsActionAddDubbing, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    public void delDubbing(Turn turn, Dubbing dubbing){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();


        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put("turn", turn.toString());
        params.put("dubbing", dubbing.toString());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsActionDelDubbing, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    public void addComment(Turn turn, Comment comment){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();


        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put("turn", turn.toString());
        params.put("comment", comment.toString());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsActionAddComment, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    public void delComment(Turn turn, Comment comment){
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();


        Map<String, String>  params = new HashMap<String, String>();
        params.put(Preferences.EMAIL, preferences.getEmail());
        params.put("turn", turn.toString());
        params.put("comment", comment.toString());

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsActionDelComment, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }
}
