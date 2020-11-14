package com.example.goeat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.os.CancellationSignal;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class GeocodingAsync extends AsyncTask<Void, Void, Address> implements LocationListener{
    Activity contextParent;
    protected LocationManager mLocationManager;
    public GeocodingAsyncResponse delegate=null;
    public GeocodingAsync(Activity contextParent,GeocodingAsyncResponse res) {
        this.contextParent = contextParent;
        this.mLocationManager = (LocationManager)contextParent.getSystemService(LOCATION_SERVICE);
        this.delegate=res;
        Log.d("Geocoding:", "reverse-geocoding current location AsyncTask created");
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (ContextCompat.checkSelfPermission(contextParent, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 1000*60*10, 0.0f, this);
        }
    }
    @Override
    protected Address doInBackground(Void... voids) {
        Location location = null;
        Address address=null;
        if (ContextCompat.checkSelfPermission(contextParent, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null)
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        final GeoPoint currentPoint = new GeoPoint(location);
        address=getAddress(currentPoint);
        if (currentPoint==null) {
            Log.d("Geocoding:","location is null");
            return null;
        }
        else {
            Log.d("Geocoding:",address.toString());
        }
        return address;
    }
    @Override
    public void onLocationChanged(Location location) {
        // your code and required methods go here...
        Log.d("Geocoding coord",location.toString());
        mLocationManager.removeUpdates(this);
    }
    @Override public void onProviderDisabled(String provider) {}

    @Override public void onProviderEnabled(String provider) {}

    @Override public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    protected void onPostExecute(Address address) {
        delegate.processFinish(address);
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
    public String getAddressStr(GeoPoint p){
        Geocoder geocoder = new Geocoder(contextParent);
        String theAddress;
        try {
            double dLatitude = p.getLatitude();
            double dLongitude = p.getLongitude();
            List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
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
        } catch (IOException e) {
            theAddress = null;
        }
        if (theAddress != null) {
            return theAddress;
        } else {
            return "";
        }
    }
}
