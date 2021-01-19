package com.example.goeat;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Place{
    long id;
    String address;
    String begin;
    List<String> categories;
    String end;
    String name;
    List<String> phones;
    String photo;
    Position position;
    PriceRange price_range;
    double Rating;
    int TotalReviews;

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
    public PriceRange getPrice_range() { return price_range;}

    public double getRating() {
        return round(Rating,1);
    }

    public int getTotalReviews() {
        return TotalReviews;
    }
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        double ratingP=(double) Math.round(value * scale) / scale;
        return ratingP;
    }
    public double getTrueRating(){
        return Rating;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPrice_range(PriceRange price_range) {
        this.price_range = price_range;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public void setTotalReviews(int totalReviews) {
        TotalReviews = totalReviews;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("address", address);
        result.put("begin", begin);
        result.put("end", end);
        result.put("categories", categories);
        result.put("name", name);
        result.put("phones", phones);
        result.put("photo", photo);
        result.put("position", position);
        result.put("price_range", price_range);
        result.put("Rating", Rating);
        result.put("TotalReviews", TotalReviews);

        return result;
    }
}




