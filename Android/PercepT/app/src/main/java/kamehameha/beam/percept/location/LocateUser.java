package kamehameha.beam.percept.location;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import kamehameha.beam.percept.models.CoordinatePoint;

/**
 * Created by sbjr on 10/17/16.
 *
 * location service class which provides the present location
 */

public class LocateUser implements ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Location mLocation;
    private GoogleApiClient mApiClient;
    private LocationRequest mLocationRequest;


    private Context mContext;


    private static final long UPDATE_INTERVAL = 300;


    public LocateUser(Context mContext) {
        this.mContext = mContext;
    }

    private boolean checkPlayService() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (result != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    public void connect() {

        if (checkPlayService() && mApiClient != null) {
            mApiClient.connect();
        }
    }


    public void disconnect() {
        if (mApiClient.isConnected()) {
            mApiClient.disconnect();
        }
    }

    public void buildApi() {
        mApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public CoordinatePoint getData() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Permission not granted", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
            if (mLocation != null) {
                CoordinatePoint coordinatePoint = new CoordinatePoint();
                coordinatePoint.setLatitude(mLocation.getLatitude());
                coordinatePoint.setLongitude(mLocation.getLongitude());

                return coordinatePoint;
            } else {
                Toast.makeText(mContext, "mLocation is null", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL - 200);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(1);
    }

    public void startUpdate() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext,"Permission not granted",Toast.LENGTH_SHORT).show();
        }
        else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, this);
        }
    }

    public void stopUpate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient,this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(mContext,"Connection failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(mContext,"Connected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }

}



