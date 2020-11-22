package com.example.goeat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Random;


public class DashboardActivity extends AppCompatActivity {
    private ImageButton goBtn,rerollBtn;
    private ImageView food;
    private TextView name,address,tags,phone,opcl,pricerange;
//    //sử dụng SHARED PREFERENCES để lấy địa chỉ hiện tại ở bất cứ class nào, ví dụ bên dưới
//    SharedPreferences sharedPref = getSharedPreferences("GOeAT", Context.MODE_PRIVATE);
//    curAddress=sharedPref.getString("curAddress","Vietnam|Thành phố Hồ Chí Minh|Bình Thạnh");
    private Random mRandFoodIndex;
    private String mTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        //UI INIT
        setContentView(R.layout.activity_dashboard);
        mRandFoodIndex=new Random();
        mTag=getIntent().getStringExtra("tag");
        InitializeUI();
        reRandomizeFood();
        //BUTTONS HANDLING
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        rerollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reRandomizeFood();
            }
        });
    }
    void InitializeUI(){
        goBtn=findViewById(R.id.goBtn);
        rerollBtn=findViewById(R.id.rerollBtn);
        food=findViewById(R.id.food);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        tags=findViewById(R.id.tags);
        phone=findViewById(R.id.phone);
        opcl=findViewById(R.id.opcl);
        pricerange=findViewById(R.id.pricerange);
    }
    void reRandomizeFood(){
        int foodIndex;
        do{
            foodIndex=mRandFoodIndex.nextInt(TabActivity.placesList.size());
        }while(!TabActivity.placesList.get(foodIndex).getCategories().contains(mTag));
        Place curPlace=TabActivity.placesList.get(foodIndex);
        Picasso.get().load(curPlace.getPhoto()).into(food);
        name.setText(curPlace.getName());
        address.setText(curPlace.getAddress());
        tags.setText("TAGS: ");
        for (String tag:curPlace.getCategories()){
            tags.append(tag);
            if (tag!=curPlace.getCategories().get(curPlace.getCategories().size()-1)){
                tags.append(", ");
            }
        }
        phone.setText("PHONE: "+curPlace.getPhones().get(0));
        opcl.setText("OPEN/CLOSED: "+curPlace.getBegin()+" - "+curPlace.getEnd());
        pricerange.setText("PRICE RANGE: "+curPlace.getPrice_range().min_price+" - "+curPlace.getPrice_range().max_price+"(VND)");
        SharedPreferences sharedPref = getSharedPreferences("GOeAT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor=putDouble(editor,"mEndLadtitude",curPlace.getPosition().latitude);
        editor=putDouble(editor,"mEndLongtitude",curPlace.getPosition().longitude);
        editor.putString("mRouteInfo",curPlace.getAddress());
        editor.apply();
    }
    //cast to get shared preferences
    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
}