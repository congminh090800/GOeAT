package com.example.goeat;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;


import android.location.LocationListener;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.example.goeat.auth.ScaleBitmap;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.NetworkLocationIgnorer;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LocationListener, SensorEventListener, MapView.OnFirstLayoutListener {
    float mAzimuthAngleSpeed = 0.0f;
    //private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    protected DirectedLocationOverlay myLocationOverlay;
    protected LocationManager mLocationManager;
    protected SensorManager mSensorManager;
    protected Sensor mOrientation;
    private double mLadtitude,mLongtitude;
    private GeoPoint mStartPoint,mEndPoint;
    private Marker startMarker,endMarker;
    private ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>(2);
    private Button testBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().setOsmdroidBasePath(new File(Environment.getExternalStorageDirectory(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(Environment.getExternalStorageDirectory(), "osmdroid/tiles"));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);
        map = (MapView) findViewById(R.id.map);
        testBtn=findViewById(R.id.testBtn);
        if (map.getScreenRect(null).height() <= 0) {
            mInitialBoundingBox = computeArea(waypoints);
            map.addOnFirstLayoutListener(this);
        } else
            map.zoomToBoundingBox(computeArea(waypoints), false);
//        requestPermissionsIfNecessary(new String[] {
//                // if you need to show the current location, uncomment the line below
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                // WRITE_EXTERNAL_STORAGE is required in order to show the map
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        });
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setTilesScaledToDpi(true);

        //map.setHorizontalMapRepetitionEnabled(false);
        //map.setVerticalMapRepetitionEnabled(false);
        map.setScrollableAreaLimitLatitude(MapView.getTileSystem().getMaxLatitude(), MapView.getTileSystem().getMinLatitude(), 0);
        map.getOverlayManager().getTilesOverlay().setLoadingBackgroundColor(Color.parseColor("#00af54"));
        map.getOverlayManager().getTilesOverlay().setLoadingLineColor(Color.parseColor("#50c34c"));
        map.setMaxZoomLevel(21.0);
        map.setMinZoomLevel(3.0);
        map.setMultiTouchControls(true);
        
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(map);
        mRotationGestureOverlay.setEnabled(true);
        map.getOverlays().add(mRotationGestureOverlay);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);

        IMapController mapController = map.getController();
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        getRoadAsync();
        myLocationOverlay = new DirectedLocationOverlay(this);
        Drawable myLocationDrawable= ResourcesCompat.getDrawable(getResources(),R.mipmap.current_location_icon,null);
        Bitmap myLocationBitmap=((BitmapDrawable)myLocationDrawable).getBitmap();
        myLocationBitmap=ScaleBitmap.scaleDown(myLocationBitmap,130,true);
        myLocationOverlay.setDirectionArrow(myLocationBitmap);
        map.getOverlays().add(myLocationOverlay);
        Location location = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null)
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location != null) {
            onLocationChanged(location);
            Log.d("location null","its not null");
        } else {
            Log.d("currentLocation","current location not found");
            myLocationOverlay.setEnabled(false);
        }
        map.invalidate();
    }
    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        boolean isOneProviderEnabled = startLocationUpdates();
        myLocationOverlay.setEnabled(isOneProviderEnabled);
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        if (mStartPoint!=null && mEndPoint !=null){
            centerTheRoute();
        }else{
            map.getController().setZoom(15.0);
            map.getController().setCenter(myLocationOverlay.getLocation());
        }
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up

    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(this);
        }
        mSensorManager.unregisterListener(this);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        ArrayList<String> permissionsToRequest = new ArrayList<>();
//        for (int i = 0; i < grantResults.length; i++) {
//            permissionsToRequest.add(permissions[i]);
//        }
//        if (permissionsToRequest.size() > 0) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    permissionsToRequest.toArray(new String[0]),
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }
//
//    private void requestPermissionsIfNecessary(String[] permissions) {
//        ArrayList<String> permissionsToRequest = new ArrayList<>();
//        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // Permission is not granted
//                permissionsToRequest.add(permission);
//            }
//        }
//        if (permissionsToRequest.size() > 0) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    permissionsToRequest.toArray(new String[0]),
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }


    boolean startLocationUpdates(){
        boolean result = false;
        for (final String provider : mLocationManager.getProviders(true)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(provider, 2000, 0.0f, this);
                result = true;
            }
        }
        return result;
    }
    private final NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
    long mLastTime = 0; // milliseconds
    double mSpeed = 0.0; // km/h
    @Override public void onLocationChanged(final Location pLoc) {
        Log.d("currentProvider",pLoc.getProvider());
        //This should ignore network provider
        long currentTime = System.currentTimeMillis();
        if (mIgnorer.shouldIgnore(pLoc.getProvider(), currentTime))
            return;
        double dT = currentTime - mLastTime;
        if (dT < 100){
            return;
        }
        mLastTime = currentTime;

        GeoPoint newLocation = new GeoPoint(pLoc);
            //we get the location for the first time:
            myLocationOverlay.setEnabled(true);

        GeoPoint prevLocation = myLocationOverlay.getLocation();
        myLocationOverlay.setLocation(newLocation);
        mStartPoint=newLocation;
        myLocationOverlay.setAccuracy((int)pLoc.getAccuracy());
        Log.d("currentLocation","current:"+newLocation.getLatitude()+" "+newLocation.getLongitude());
        if (prevLocation!=null && pLoc.getProvider().equals(LocationManager.GPS_PROVIDER)){
            mSpeed = pLoc.getSpeed() * 3.6;
            //TODO: check if speed is not too small
            if (mSpeed >= 0.1){
                mAzimuthAngleSpeed = pLoc.getBearing();

                myLocationOverlay.setBearing(mAzimuthAngleSpeed);
            }
        }
        //map.getController().animateTo(myLocationOverlay.getLocation());
        //just redraw the location overlay:
        map.invalidate();
    }
    @Override public void onProviderDisabled(String provider) {}

    @Override public void onProviderEnabled(String provider) {}

    @Override public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {
        myLocationOverlay.setAccuracy(accuracy);
        map.invalidate();
    }

    static float mAzimuthOrientation = 0.0f;
    @Override public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ORIENTATION:
                if (mSpeed < 0.1){
					float azimuth = event.values[0];
					if (Math.abs(azimuth-mAzimuthOrientation)>2f){
						mAzimuthOrientation = azimuth;
						myLocationOverlay.setBearing(mAzimuthOrientation);
						map.invalidate();
					}

                }
                break;
            default:
                break;
        }
    }
    //cast long bits back to double
    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
    //ROUTING SECTION
    public void getRoadAsync(){
        SharedPreferences sharedPref = getSharedPreferences("GOeAT", Context.MODE_PRIVATE);
        mLadtitude=getDouble(sharedPref,"mStartLadtitude",0);
        mLongtitude=getDouble(sharedPref,"mStartLongtitude",0);
        mStartPoint=new GeoPoint(mLadtitude,mLongtitude);
        mEndPoint=new GeoPoint(10.8535, 106.7932);
        if (mStartPoint==null || mEndPoint==null) return;
        waypoints.add(mStartPoint);
        waypoints.add(mEndPoint);
        //Draw markers
        startMarker = new Marker(map);
        startMarker.setPosition(mStartPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_CENTER);
        startMarker.setIcon(getResources().getDrawable(R.mipmap.marker_current_location,null));
        map.getOverlays().add(startMarker);

        endMarker=new Marker(map);
        endMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        endMarker.setIcon(getResources().getDrawable(R.mipmap.marker_destination,null));
        endMarker.setPosition(mEndPoint);
        map.getOverlays().add(endMarker);

        //Routing
        new RoutingAsync(this).execute(waypoints);
    }
    public void centerTheRoute(){
        if (map.getScreenRect(null).height() <= 0) {
            mInitialBoundingBox = computeArea(waypoints);
            map.addOnFirstLayoutListener(this);
        } else
            map.zoomToBoundingBox(computeArea(waypoints), false);
    }
    public BoundingBox computeArea(ArrayList<GeoPoint> points) {

        double nord = 0, sud = 0, ovest = 0, est = 0;

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) == null) continue;

            double lat = points.get(i).getLatitude();
            double lon = points.get(i).getLongitude();

            if ((i == 0) || (lat > nord)) nord = lat;
            if ((i == 0) || (lat < sud)) sud = lat;
            if ((i == 0) || (lon < ovest)) ovest = lon;
            if ((i == 0) || (lon > est)) est = lon;

        }

        return new BoundingBox(nord+0.02, est+0.02, sud-0.02, ovest-0.02);

    }
    BoundingBox mInitialBoundingBox = null;
    @Override
    public void onFirstLayout(View v, int left, int top, int right, int bottom) {
        if (mInitialBoundingBox != null)
            map.zoomToBoundingBox(mInitialBoundingBox, false);
    }
}