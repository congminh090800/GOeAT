package com.example.goeat.Fragments;

/**
 * @author Admin
 * @date 11/20/2020
 */

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goeat.Place;
import com.example.goeat.R;
import com.example.goeat.TabActivity;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * @author Admin
 * @date 11/19/2020
 */

class nearbyAdapter extends RecyclerView.Adapter<nearbyAdapter.NearbyViewHolder> {
    Context mContext;
    nearbyAdapter(Context c){
        mContext=c;
    }
    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v=LayoutInflater.from(mContext).inflate(R.layout.nearby_item,parent,false);
        NearbyViewHolder holder=new NearbyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyViewHolder holder, int position) {
        holder.nameTxt.setText(TabActivity.placesList.get(position).getName());
        Picasso.get().load(TabActivity.placesList.get(position).getPhoto()).into(holder.foodImg);
        holder.tagTxt.setText("");
        for (String tag:TabActivity.placesList.get(position).getCategories()){
            holder.tagTxt.append(tag);
            if (tag!=TabActivity.placesList.get(position).getCategories().get(TabActivity.placesList.get(position).getCategories().size()-1)){
                holder.tagTxt.append(", ");
            }
        }
        holder.ratingTxt.setText(String.valueOf(TabActivity.placesList.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return TabActivity.placesList.size();
    }

    public static class NearbyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt,tagTxt,ratingTxt;
        ImageView foodImg;
        public NearbyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt=itemView.findViewById(R.id.textView1);
            foodImg=itemView.findViewById(R.id.foodImg);
            tagTxt=itemView.findViewById(R.id.textView3);
            ratingTxt=itemView.findViewById(R.id.ratingTxt);
        }
    }
}