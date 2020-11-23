package com.example.goeat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Random;


public class DashboardActivity extends AppCompatActivity {
    private ImageButton goBtn,rerollBtn,commentBtn;
    private ImageView food;
    private TextView name,address,tags,phone,opcl,pricerange,dashboard_txtRating;
    private RatingBar ratingbar;
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
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindow(v);
            }
        });
    }

    // Darken the background when Window Popup
    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }
    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }
    public void onButtonShowPopupWindow(View v) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup_comment, null);

        final ViewGroup root = (ViewGroup)getWindow().getDecorView().getRootView();
        // create the popup window
        applyDim(root, 0.5f);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                clearDim(root);
                return true;

            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clearDim(root);
            }
        });
    }
    void InitializeUI(){
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        goBtn=findViewById(R.id.goBtn);
        commentBtn=findViewById(R.id.commentBtn);
        rerollBtn=findViewById(R.id.rerollBtn);
        food=findViewById(R.id.food);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        tags=findViewById(R.id.tags);
        phone=findViewById(R.id.phone);
        opcl=findViewById(R.id.opcl);
        pricerange=findViewById(R.id.pricerange);
        dashboard_txtRating=findViewById(R.id.dashboard_txtRating);
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
        if( curPlace.getRating() > 7.5){
            dashboard_txtRating.setBackgroundResource(R.drawable.rating_point);
        }else if( curPlace.getRating() > 5){
            dashboard_txtRating.setBackgroundResource(R.drawable.rating_point_medium);
        }else{
            dashboard_txtRating.setBackgroundResource(R.drawable.rating_point_low);
        }
        if (curPlace.getRating()>=10.0){
            dashboard_txtRating.setText("10");}
        dashboard_txtRating.setText(String.valueOf(curPlace.getRating()));
        double ratingPoint = curPlace.getRating()/2;
        ratingbar.setRating((float)ratingPoint);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}