package com.example.goeat.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goeat.HistoryVal;
import com.example.goeat.Place;
import com.example.goeat.R;
import com.example.goeat.TabActivity;
import com.example.goeat.auth.Auth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.ToLongFunction;

import static com.example.goeat.TabActivity.historyList;
import static com.example.goeat.TabActivity.visitedList;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    View v;
    String UId;
    public HistoryFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        recyclerView=v.findViewById(R.id.history_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    @Override
    public void onResume() {
        super.onResume();
        updateOperation();
    }
    public void updateOperation(){
        for (HistoryVal val:historyList){
            Log.d("historytesting",val.getString());
        }
        queryToFoodInfo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("historytesting",visitedList.size()+"");
                historyAdapter hAdapter=new historyAdapter(getActivity());
                recyclerView.setAdapter(hAdapter);
            }
        },200);
    }

    public void queryToFoodInfo(){
        visitedList.clear();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Places").child("HoChiMinh");
        for (HistoryVal val: historyList){
            db.child(val.getDistrict()).child(val.getPlaceID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("historytesting",snapshot.getValue().toString());
                    TabActivity.visitedList.add(snapshot.getValue(Place.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("history","faild on loading history");
                }
            });
        }
    }
}