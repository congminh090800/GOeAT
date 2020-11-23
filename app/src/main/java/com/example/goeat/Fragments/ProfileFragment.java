package com.example.goeat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.goeat.LoginActivity;
import com.example.goeat.R;
import com.example.goeat.auth.Auth;

import org.apache.commons.lang3.ObjectUtils;

public class ProfileFragment extends Fragment {

    Button logout;
    Auth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = Auth.getInstance();
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = v.findViewById(R.id.button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return v;
    }
}