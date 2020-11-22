package com.example.goeat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class GeocodingAsync extends AsyncTask<Void, Void, Address> implements LocationListener{
    Activity contextParent;
    public LocationManager mLocationManager;
    public long mLastime=0;

    public GeocodingAsync(Activity contextParent) {
        this.contextParent = contextParent;
        this.mLocationManager = (LocationManager)contextParent.getSystemService(LOCATION_SERVICE);
        Log.d("Geocoding:", "reverse-geocoding current location AsyncTask created");
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (ContextCompat.checkSelfPermission(contextParent, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 1, 0.0f, this);
        }
    }
    @Override
    protected Address doInBackground(Void... voids) {
        Location location = null;
        Address address=null;
        mLastime=System.currentTimeMillis();
        while(System.currentTimeMillis()-mLastime<=2000) {
            if (ContextCompat.checkSelfPermission(contextParent, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null)
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            mLocationManager.removeUpdates(this);
        }
        SharedPreferences sharedPref = contextParent.getSharedPreferences("GOeAT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        final GeoPoint currentPoint = new GeoPoint(location);
        address=getAddress(currentPoint);

        if (currentPoint==null) {
            Log.d("Geocoding:","location is null");
            return null;
        }
        else {
            String district="";
            if (address.getSubAdminArea()!=null) {
                district=address.getSubAdminArea();
            }
            editor.putString("curAddress",district);
            editor=putDouble(editor,"mStartLadtitude",location.getLatitude());
            editor=putDouble(editor,"mStartLongtitude",location.getLongitude());
            editor.apply();
        }
        return address;
    }
    @Override
    public void onLocationChanged(Location location) {
    }
    @Override public void onProviderDisabled(String provider) {}

    @Override public void onProviderEnabled(String provider) {}

    @Override public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    protected void onPostExecute(Address address) {
    }

    public Address getAddress(GeoPoint p){
        Geocoder geocoder = new Geocoder(contextParent, Locale.US);
        //String theAddress;
        Address address;
        try {
            double dLatitude = p.getLatitude();
            double dLongitude = p.getLongitude();
            List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1);
            //StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                address = addresses.get(0);
//                int n = address.getMaxAddressLineIndex();
//                for (int i=0; i<=n; i++) {
//                    if (i!=0)
//                        sb.append(", ");
//                    sb.append(address.getAddressLine(i));
//                }
//                theAddress = sb.toString();
            } else {
               address= null;
            }
        } catch (IOException e) {
            address = null;
        }
        if (address != null) {
            return address;
        } else {
            return null;
        }
    }
    public String getAddressStr(Address address){
        String theAddress;
            StringBuilder sb = new StringBuilder();
            if (address!=null) {
                int n = address.getMaxAddressLineIndex();
                for (int i=0; i<=n; i++) {
                    if (i!=0)
                        sb.append(", ");
                    sb.append(address.getAddressLine(i));
                }
                theAddress = sb.toString();
            } else {
                theAddress = null;
            }
        if (theAddress != null) {
            return theAddress;
        } else {
            return "";
        }
    }
    //cast to get shared preferences
    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
}
