package com.example.goeat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class DashboardActivity extends AppCompatActivity {
    private ImageButton goBtn;
    private GeocodingAsync myGeocoding;
//    //sử dụng SHARED PREFERENCES để lấy địa chỉ hiện tại ở bất cứ class nào, ví dụ bên dưới
//    SharedPreferences sharedPref = getSharedPreferences("GOeAT", Context.MODE_PRIVATE);
//    curAddress=sharedPref.getString("curAddress","Vietnam|Thành phố Hồ Chí Minh|Bình Thạnh");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FINDING CURRENT ADDRESS
        myGeocoding=new GeocodingAsync(DashboardActivity.this);
        myGeocoding.execute();
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
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
    }
    void InitializeUI(){
        goBtn=findViewById(R.id.goBtn);
    }
    @Override
    protected void onResume(){
        super.onResume();
        myGeocoding=new GeocodingAsync(DashboardActivity.this);
        myGeocoding.execute();
    }

}