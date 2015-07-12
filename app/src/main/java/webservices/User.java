package webservices;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utils.Crypto;
import utils.Preferences;

/**
 * Created by NESTOR on 12/07/2015.
 */
public class User {

    private static final String TAG = "USER";

    private Context context;

    public User(Context context) {
        this.context = context;
    }

    public void login(final String email, String password, final LinearLayout linearLayout){
        String md5Password = Crypto.md5(password);

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();

        Map<String, String>  params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", md5Password);

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsLogin, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,response.toString());
                        if(response.has("status")){
                            try {
                                if(response.getBoolean("status")){
                                    Preferences preferences = new Preferences(context);
                                    preferences.setEmail(email);
                                    preferences.setName(response.getString("name"));
                                    preferences.setLastName(response.getString("lastName"));
                                    preferences.save();
                                    new Sync(context,linearLayout).turn();
                                }
                                else{
                                    linearLayout.setVisibility(View.GONE);
                                    SuperToast.create(context, context.getString(R.string.error_login_password), SuperToast.Duration.LONG,
                                            Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            linearLayout.setVisibility(View.GONE);
                            SuperToast.create(context, context.getString(R.string.error_login_password), SuperToast.Duration.LONG,
                                    Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                linearLayout.setVisibility(View.GONE);
                SuperToast.create(context, context.getString(R.string.error_login_timeout), SuperToast.Duration.LONG,
                        Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
            }
        }
        );

        requestQueue.add(request);
    }

    public void register(final String name, final String lastName, final String email, String password){
        String md5Password = Crypto.md5(password);

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.getCache().clear();

        Map<String, String>  params = new HashMap<String, String>();
        params.put("name", name);
        params.put("lastName", lastName);
        params.put("email", email);
        params.put("password", md5Password);

        CustomRequest request = new CustomRequest(Request.Method.POST, Routes.wsRegister, params,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG,response.toString());
                    if(response.has("status")){
                        try {
                            if(response.getBoolean("status")){
                                Preferences preferences = new Preferences(context);
                                preferences.setName(name);
                                preferences.setLastName(lastName);
                                preferences.setEmail(email);
                                preferences.setIsLogin(true);
                                if(preferences.save()){
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    context.startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }
        );

        requestQueue.add(request);
    }
}
