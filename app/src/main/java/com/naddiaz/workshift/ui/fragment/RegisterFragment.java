package com.naddiaz.workshift.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.naddiaz.workshift.R;

import webservices.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterFragment extends Fragment {

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        TextView textViewRegister = (TextView) rootView.findViewById(R.id.textView_home);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_side_left, R.anim.slide_side_rigth)
                        .replace(R.id.fragment_container, new LoginFragment()).commit();
            }
        });

        final EditText editTextName = (EditText) rootView.findViewById(R.id.editText_name);
        final EditText editTextLastName = (EditText) rootView.findViewById(R.id.editText_lastName);
        final EditText editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);
        final EditText editTextPassword = (EditText) rootView.findViewById(R.id.editText_password);

        Button buttonRegister = (Button) rootView.findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                new User(getActivity()).register(name,lastName,email,password);
            }
        });
        return rootView;
    }
}
