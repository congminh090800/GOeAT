package com.example.goeat;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
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
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;


import android.location.LocationListener;
import android.util.Log;

import com.example.goeat.auth.ScaleBitmap;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.NetworkLocationIgnorer;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LocationListener, SensorEventListener {
    float mAzimuthAngleSpeed = 0.0f;
    //private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    protected DirectedLocationOverlay myLocationOverlay;
    protected LocationManager mLocationManager;
    protected SensorManager mSensorManager;
    protected Sensor mOrientation;

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
        map.getController().setZoom(15.0);

        myLocationOverlay = new DirectedLocationOverlay(this);
        Drawable myLocationDrawable= ResourcesCompat.getDrawable(getResources(),R.drawable.current_location_icon,null);
        Bitmap myLocationBitmap=((BitmapDrawable)myLocationDrawable).getBitmap();
        myLocationBitmap=ScaleBitmap.scaleDown(myLocationBitmap,120,true);

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
        map.getController().setCenter(myLocationOverlay.getLocation());
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
}