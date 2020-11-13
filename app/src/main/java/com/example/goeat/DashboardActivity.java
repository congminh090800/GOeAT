package com.example.goeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import org.osmdroid.util.GeoPoint;

public class DashboardActivity extends AppCompatActivity {
    private Button goBtn,reviewBtn;
    private GeocodingAsync myGeocoding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        InitializeUI();
        myGeocoding=new GeocodingAsync(DashboardActivity.this);
        myGeocoding.execute();
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
            }
        });
    }
    void InitializeUI(){
        goBtn=findViewById(R.id.goBtn);
        reviewBtn=findViewById(R.id.reviewBtn);
    }
}