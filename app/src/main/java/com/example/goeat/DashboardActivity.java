package com.example.goeat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DashboardActivity extends AppCompatActivity {
    private Button goBtn,reviewBtn;
    private TextView txtLocation;
    private GeocodingAsync myGeocoding;
    private String curAddress;
//    //sử dụng SHARED PREFERENCES để lấy địa chỉ hiện tại ở bất cứ class nào, ví dụ bên dưới
//    SharedPreferences sharedPref = getSharedPreferences("GOeAT", Context.MODE_PRIVATE);
//    curAddress=sharedPref.getString("curAddress","Vietnam|Thành phố Hồ Chí Minh|Bình Thạnh");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FINDING CURRENT ADDRESS
        myGeocoding=new GeocodingAsync(DashboardActivity.this);
        myGeocoding.execute();
        super.onCreate(savedInstanceState);
        //UI INIT
        setContentView(R.layout.activity_dashboard);
        InitializeUI();
        //BUTTONS HANDLING
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
        txtLocation=findViewById(R.id.txtLocation);
    }
    @Override
    protected void onResume(){
        super.onResume();
        myGeocoding=new GeocodingAsync(DashboardActivity.this);
        myGeocoding.execute();
    }

}