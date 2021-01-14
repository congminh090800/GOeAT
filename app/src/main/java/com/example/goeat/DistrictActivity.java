package com.example.goeat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DistrictActivity extends AppCompatActivity {
    ListView districtListView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        final String district[] = {"BinhChanh" ,
                "BinhTan" ,
                "BinhThanh" ,
                "District1" ,
                "District10" ,
                "District11" ,
                "District12" ,
                "District2" ,
                "District3" ,
                "District4" ,
                "District5" ,
                "District6" ,
                "District7" ,
                "District8" ,
                "District9" ,
                "GoVap" ,
                "NhaBe" ,
                "PhuNhuan" ,
                "TanBinh" ,
                "TanPhu" ,
                "ThuDuc"};
        districtListView=(ListView)findViewById(R.id.districtListview);
        districtAdapter adapter = new districtAdapter(this,district);
        districtListView.setAdapter(adapter);
        districtListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent districtIntent = new Intent(DistrictActivity.this,FoodlistActivity.class);
                districtIntent.putExtra("district",district[position]);
                startActivity(districtIntent);
            }
        });
    }
    class districtAdapter extends ArrayAdapter<String>{
        String mdistrict[];
        Context context;
        districtAdapter(Context c,String district[]){
            super(c,R.layout.district_item,R.id.district_textView,district);
            this.context=c;
            this.mdistrict = district;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View districtItem = layoutInflater.inflate(R.layout.district_item,parent,false);
            TextView districtName = districtItem.findViewById(R.id.district_textView);

            districtName.setText(mdistrict[position]);
            return districtItem;
        }
    }
}