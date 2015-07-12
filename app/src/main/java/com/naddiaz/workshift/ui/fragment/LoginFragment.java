package com.naddiaz.workshift.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.naddiaz.workshift.R;
import com.naddiaz.workshift.ui.activity.HomeActivity;

import utils.Preferences;
import webservices.Sync;
import webservices.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    Preferences preferences;
    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferences = new Preferences(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        TextView textViewRegister = (TextView) rootView.findViewById(R.id.textView_register);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_side_left, R.anim.slide_side_rigth)
                        .replace(R.id.fragment_container, new RegisterFragment())
                        .commit();
            }
        });

        final EditText editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);
        editTextEmail.setText(preferences.getEmail());
        final EditText editTextPassword = (EditText) rootView.findViewById(R.id.editText_password);
        final LinearLayout linearLayoutLoading = (LinearLayout) rootView.findViewById(R.id.layout_loading);

        Button buttonLogin = (Button) rootView.findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutLoading.setVisibility(View.VISIBLE);
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                new User(getActivity()).login(email,password,linearLayoutLoading);
            }
        });
        return rootView;
    }
}
