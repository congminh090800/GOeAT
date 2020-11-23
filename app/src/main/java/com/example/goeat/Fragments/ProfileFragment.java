package com.example.goeat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.goeat.LoginActivity;
import com.example.goeat.R;
import com.example.goeat.User;
import com.example.goeat.auth.Auth;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment {

    Button logout;
    Button changepassword;
    Auth mAuth;
    TextView username;
    TextView email;
    TextView gender;
    TextView date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = Auth.getInstance();
        User u = mAuth.getCurrentUser();
        username = v.findViewById(R.id.username);
        username.append(u.getUsername());
        email = v.findViewById(R.id.email);
        email.append(u.getEmail());
        gender = v.findViewById(R.id.gender);
        gender.append(u.getGender());
        date = v.findViewById(R.id.date);
        date.append(new Date(TimeUnit.DAYS.toDays(u.getDateCreated()))+"");

        changepassword = v.findViewById(R.id.logout);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        logout = v.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
        return v;
    }
}