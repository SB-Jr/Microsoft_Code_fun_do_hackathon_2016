package kamehameha.beam.percept.location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import static android.os.Build.VERSION.SDK;
import static android.os.Build.VERSION.SDK_INT;

public class LocateUserService extends Service {

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private LocationChangeCallback mLocationChangeCallback;

    public LocateUserService(LocationChangeCallback mLocationChangeCallback) {
        this.mLocationChangeCallback = mLocationChangeCallback;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    MyLocationListener[] myLocationListeners = new MyLocationListener[]{
            new MyLocationListener(LocationManager.GPS_PROVIDER),
            new MyLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, myLocationListeners[1]);
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, myLocationListeners[1]);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < myLocationListeners.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(myLocationListeners[i]);
                }
                else{
                    mLocationManager.removeUpdates(myLocationListeners[i]);
                }
            }
        }
    }

    private void init(){
        if(mLocationManager == null){
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class MyLocationListener implements android.location.LocationListener {

        Location mLastLocation;

        public MyLocationListener(String provider) {
            mLastLocation = new Location(provider);
            mLocationChangeCallback.onLocationChange(mLastLocation);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
            mLocationChangeCallback.onLocationChange(mLastLocation);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
