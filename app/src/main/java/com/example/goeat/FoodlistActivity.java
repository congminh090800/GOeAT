package com.example.goeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.goeat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class FoodlistActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String mDistrict;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDistrict = getIntent().getStringExtra("district");
    }


    class FoodlistViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt,tagTxt,ratingTxt;
        ImageView foodImg;
        LinearLayout itemLayout;
        public FoodlistViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt=itemView.findViewById(R.id.textView1);
            foodImg=itemView.findViewById(R.id.foodImg);
            tagTxt=itemView.findViewById(R.id.textView3);
            ratingTxt=itemView.findViewById(R.id.ratingTxt);
            itemLayout=itemView.findViewById(R.id.itemLayout);
        }
    }
}