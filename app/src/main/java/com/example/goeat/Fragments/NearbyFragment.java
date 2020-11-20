package com.example.goeat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.goeat.R;

public class NearbyFragment extends Fragment {
    public NearbyFragment(){}
    private ListView listview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String items[] = {"Item1","Item2","Item3","Item4","Item5","Item6","Item7","Item8","Item9","Item10"};
        String mdescription[] = {"Destination","Destination","Destination","Destination","Destination","Destination","Destination","Destination","Destination","Destination"};
        int flags[] = {R.drawable.login_logo, R.drawable.login_logo, R.drawable.login_logo, R.drawable.login_logo, R.drawable.login_logo, R.drawable.login_logo, R.drawable.login_logo, R.drawable.login_logo, R.drawable.login_logo, R.drawable.login_logo};
        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        ListView lv = (ListView)rootView.findViewById(R.id.list_View);
        nearbyAdapter adpt = new nearbyAdapter(this.getActivity(),items,mdescription,flags);
        lv.setAdapter(adpt);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}