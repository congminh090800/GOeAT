package com.example.goeat.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goeat.DashboardActivity;
import com.example.goeat.Place;
import com.example.goeat.R;
import com.example.goeat.TabActivity;

public class HomeFragment extends Fragment {
    public HomeFragment(){}
    private ListView listview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final String items[] = {"Rice","Noodle","Bakery","Barbeque","Hotpot","Seafood","Street food","Soup","Sandwich","Restaurant","Vegetarian","Coffee/Dessert"};
        int img_id[] = {R.drawable.rice,R.drawable.noodle,R.drawable.bakery,R.drawable.bbq,R.drawable.hotpot,R.drawable.seafood,R.drawable.street_food,R.drawable.soup,R.drawable.sandwich,R.drawable.restaurant,R.drawable.veggie,R.drawable.coffee_dessert};
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lv = (ListView)rootView.findViewById(R.id.home_list_View);
        homeAdapter adpt = new homeAdapter(this.getActivity(),items,img_id);
        lv.setAdapter(adpt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFoodExist(items[position])){
                Intent homeIntent = new Intent(getActivity(), DashboardActivity.class);
                homeIntent.putExtra("tag",items[position]);
                startActivity(homeIntent);
                }else{
                    Toast.makeText(getContext(), "GẦN ĐÂY KHÔNG CÓ LOẠI QUÁN NÀY", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }
    public boolean isFoodExist(String tag){
        for (Place place: TabActivity.placesList){
            if (place.getCategories().contains(tag)) return true;
        }
        return false;
    }
}