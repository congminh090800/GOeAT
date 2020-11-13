package com.example.goeat;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;

public class GeocodingAsync extends AsyncTask<Void, Void, Void> {
    Activity contextParent;

    public GeocodingAsync(Activity contextParent) {
        this.contextParent = contextParent;
        Log.d("Test", "AsyncTask created");
    }
    @Override
    protected Void doInBackground(Void... voids) {
        final GeoPoint startPoint = new GeoPoint(10.8759, 106.7990);
        Log.d("Test","Address: "+getAddress(startPoint));
        return null;
    }
    public String getAddress(GeoPoint p){
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
