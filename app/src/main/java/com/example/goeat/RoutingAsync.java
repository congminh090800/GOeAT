package com.example.goeat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.ArrayList;

public class RoutingAsync extends AsyncTask<ArrayList<GeoPoint>, Void, Road> {
    private Activity mContext;
    public RoutingAsync(Activity context) {
        this.mContext = context;
    }
    protected Road doInBackground(ArrayList<GeoPoint>... params) {
        ArrayList<GeoPoint> waypoints = params[0];
        RoadManager roadManager=new OSRMRoadManager(mContext);
        Road road=roadManager.getRoads(waypoints)[0];
        while(road.mStatus != Road.STATUS_OK){
            road=roadManager.getRoads(waypoints)[0];
        }
        return road;
    }
    protected void onPostExecute(Road result) {
        Road road=result;
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, Color.parseColor("#669df6"),20);
        MapView map=mContext.findViewById(R.id.map);
        map.getOverlays().add(0,roadOverlay);
    }
}
