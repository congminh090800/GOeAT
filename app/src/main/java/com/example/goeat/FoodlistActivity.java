package com.example.goeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.goeat.R;
import com.example.goeat.database.PlaceDAO;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FoodlistActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String mDistrict;
    private Task<List<Place>> taskDistrictDetail;
    static public List<Place> listPlaceDistrict = new ArrayList<Place>();
    private PlaceDAO mPlaceDAO;
    private RecyclerView foodlistRecyclerView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);
        mDistrict = getIntent().getStringExtra("district");
        Log.e("districttest",mDistrict);
        listPlaceDistrict.clear();
        RecyclerView foodlistView = (RecyclerView)findViewById(R.id.foodListRecyclerView);
        final FoodlistAdapter adapter = new FoodlistAdapter(listPlaceDistrict);
        foodlistView.setLayoutManager(new LinearLayoutManager(this));
        foodlistView.setAdapter(adapter);
        PlaceDAO.getInstance().getPlacesByDistrict(mDistrict)
                .addOnSuccessListener(new OnSuccessListener<List<Place>>() {
                    @Override
                    public void onSuccess(List<Place> places) {
                        for (Place place : places) {
                            listPlaceDistrict.add(place);
                            adapter.notifyDataSetChanged();
                        }
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Places",e.getMessage());
                    }
                });
    }
    public class FoodlistAdapter extends RecyclerView.Adapter<FoodlistAdapter.FoodListViewHolder>{
        private List<Place> DistrictInfo ;
        public FoodlistAdapter(List<Place> district){
            this.DistrictInfo = district;

        }


        public  class FoodListViewHolder extends RecyclerView.ViewHolder{
            TextView nameTxt;
            ImageView foodImg;
            LinearLayout itemLayout;
            public FoodListViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTxt=itemView.findViewById(R.id.textView1);
                foodImg=itemView.findViewById(R.id.foodImg);
                itemLayout=itemView.findViewById(R.id.foodListLayout);
            }
        }
        @Override
        public FoodlistAdapter.FoodListViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            final Context context = parent.getContext();
            LayoutInflater inflater  = LayoutInflater.from(context);

            View FoodListView = inflater.inflate(R.layout.foodlist_item,parent,false);
            final FoodListViewHolder viewHolder = new FoodListViewHolder(FoodListView);
            viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index=viewHolder.getAdapterPosition();
                    Intent foodlistIntent = new Intent(FoodlistActivity.this, EditActivity.class);
                    foodlistIntent.putExtra("index",index);
                    foodlistIntent.putExtra("district",mDistrict);
                    finish();
                    context.startActivity(foodlistIntent);
                }
            });
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(FoodlistAdapter.FoodListViewHolder holder,int position){
            holder.nameTxt.setText(DistrictInfo.get(position).getName());
            Picasso.get().load(DistrictInfo.get(position).getPhoto()).into(holder.foodImg);
        }
        @Override
        public int getItemCount() {
            return DistrictInfo.size();
        }

    }

}