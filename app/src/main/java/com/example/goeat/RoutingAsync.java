package com.example.goeat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RoutingAsync extends AsyncTask<ArrayList<GeoPoint>, Void, Road> {
    Activity mContext;
    private RoadManager roadManager;
    public RoutingAsync(Activity context) {
        this.mContext = context;
        roadManager=new OSRMRoadManager(mContext);
    }
    @Override
    protected Road doInBackground(ArrayList<GeoPoint>... params) {
        ArrayList<GeoPoint> waypoints = params[0];
        Road road=roadManager.getRoad(waypoints);
        while(road.mStatus != Road.STATUS_OK){
            road=roadManager.getRoad(waypoints);
        }
        return road;
    }
    @Override
    protected void onPostExecute(Road result) {
        MapView map=mContext.findViewById(R.id.map);
        TextView routeLen=mContext.findViewById(R.id.routeLength);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(result, Color.parseColor("#669df6"),20);
        roadOverlay.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
        routeLen.setText("Distance: "+Integer.toString((int)Math.round(roadOverlay.getDistance()/1000d))+" km");
        map.getOverlays().add(0,roadOverlay);
    }
}
