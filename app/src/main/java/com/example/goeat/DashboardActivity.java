package com.example.goeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.osmdroid.util.GeoPoint;

import java.util.concurrent.ExecutionException;

public class DashboardActivity extends AppCompatActivity {
    private Button goBtn,reviewBtn;
    private GeocodingAsync myGeocoding;
    private Address curAddress=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
    @Override
    protected void onResume(){
        super.onResume();
        //FINDING CURRENT ADDRESS

        myGeocoding=new GeocodingAsync(DashboardActivity.this, new GeocodingAsyncResponse() {
            @Override
            public void processFinish(Address address) {
            }
        });
        try {
            curAddress=myGeocoding.execute().get();
        }
        catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
        if (curAddress==null){
            Log.d("Geocoding result","curAddress is null");
        }else
        {
            Log.d("Geocoding result","curAddress:"+curAddress.toString());
        }
    }
}