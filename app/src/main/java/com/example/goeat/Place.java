package com.example.goeat;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
public class Place{
    String address;
    String begin;
    List<String> categories;
    String end;
    String name;
    List<String> phones;
    String photo;
    Position position;
    PriceRange price_range;

    public Place(){
    }
    public String getString(){
        return name+" "+address+" "+phones.get(0)+" "+categories.get(0)+" "+photo+" "+begin+" "+end+" "+price_range.getString()+position.getString();
    }
    public String getAddress() {
        return address;
    }
    public String getBegin() {
        return begin;
    }
    public List<String> getCategories() {
        return categories;
    }
    public String getEnd() {
        return end;
    }
    public String getName(){
        return name;
    }
    public List<String> getPhones() {
        return phones;
    }
    public String getPhoto() {
        return photo;
    }
    public Position getPosition() {
        return position;
    }
    public PriceRange getPrice_range() {
        return price_range;
    }
}




