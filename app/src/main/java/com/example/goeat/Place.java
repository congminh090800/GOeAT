package com.example.goeat;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Place {
    String address;
    String begin, end;
    String[] categories;
    String name;
    String[] phones;
    String photo;
    int[] price_range;
    GeoPoint position;
    public Place getData(){
        return this;
    }

    public  Place(String address, String begin,String end,String[] categories,String name,String[] phones,String photo,int[] price_range,GeoPoint position){
        this.address = address;
        this.begin = begin;
        this.end = end;
        this.categories = categories;
        this.name=name;
        this.phones = phones;
        this.photo = photo;
        this.price_range = price_range;
        this.position = position;
    }

    public float GetDistance(@NotNull GeoPoint other) {
        float[] result= new float[1];
        Location.distanceBetween(this.position.getLatitude(), this.position.getLongitude(), other.getLatitude(),other.getLongitude(),result);
        return result[0];
    }
    public ArrayList<Place> GetNearBy(@NotNull Place userPlace) {
        final ArrayList<Place> NearBy = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myCity = database.child("HoChiMinh");
        String[] separatedAddress = userPlace.address.split("|");
        DatabaseReference myDistrict = myCity.child(VNCharacterUtils.removeAccent(separatedAddress[2]));
        myDistrict.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapChild : snapshot.getChildren()){
                    String mAddress = snapChild.child("address").getValue(String.class);
                    String mBegin = snapChild.child("begin").getValue(String.class);
                    String mEnd = snapChild.child("end").getValue(String.class);
                    String[] mCategories = snapChild.child("categories").getValue(String[].class);
                    String mName = snapChild.child("name").getValue(String.class);
                    String[] mPhones = snapChild.child("phones").getValue(String[].class);
                    String mPhoto = snapChild.child("photo").getValue(String.class);
                    int[] mPrice_range = snapChild.child("price_range").getValue(int[].class);
                    GeoPoint mPosition = snapChild.child("position").getValue(GeoPoint.class);
                    NearBy.add(new Place(mAddress,mBegin,mEnd,mCategories,mName,mPhones,mPhoto,mPrice_range,mPosition));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return NearBy;
    }
}

