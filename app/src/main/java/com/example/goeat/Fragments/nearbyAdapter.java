package com.example.goeat.Fragments;

/**
 * @author Admin
 * @date 11/20/2020
 */

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goeat.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Admin
 * @date 11/19/2020
 */

class nearbyAdapter extends BaseAdapter {
    private Activity activity;
    private String[] items;
    private String[] rDescription;
    private int[] flag;
    private LayoutInflater mInflater;
    public nearbyAdapter(Activity activity,String[] items,String[] description,int[] flag){
        this.activity = activity;
        this.items = items;
        this.rDescription = description;
        this.flag = flag;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby, container, false);
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder{
        TextView tvName,rDescription;
        ImageView imgs;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();


        if(view == null) {
            view = inflater.inflate(R.layout.nearby_item,null);
            holder = new ViewHolder();
            holder.tvName = (TextView) view.findViewById(R.id.textView1);
            holder.rDescription = view.findViewById(R.id.textView2);
            holder.imgs = view.findViewById(R.id.icon);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(items[i]);
        holder.rDescription.setText(rDescription[i]);
        holder.imgs.setImageResource(flag[i]);

        return view;
    }
}