package kamehameha.beam.percept.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;


import java.util.ArrayList;

import kamehameha.beam.percept.AugmentingBoard.AugmentCanvas;
import kamehameha.beam.percept.R;
import kamehameha.beam.percept.callbackinterfaces.LocationChangeCallback;
import kamehameha.beam.percept.callbackinterfaces.OrientationChangeCallback;
import kamehameha.beam.percept.camera.CameraCanvas;
import kamehameha.beam.percept.location.LocateUser;
import kamehameha.beam.percept.location.NearbyLocation;
import kamehameha.beam.percept.location.OrientUser;
import kamehameha.beam.percept.models.CoordinatePoint;

public class MainActivity extends AppCompatActivity implements OrientationChangeCallback,LocationChangeCallback{

    //supporting objects for the activity
    private LocateUser mLocation;
    private OrientUser mOrientation;

    //permission objects
    private static final int MY_PERMISSION_REQUEST_CODE = 6969;
    private static final String[] PERMISSIONS_REQUIRED = {  Manifest.permission.CAMERA,
                                                            Manifest.permission.LOCATION_HARDWARE,
                                                            Manifest.permission.CONTROL_LOCATION_UPDATES,
                                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                                            Manifest.permission.INTERNET};

    private ArrayList<String> permissionsNeeded = new ArrayList<>();


    //view objects used in the activity
    private TextView mCoordinatesTextView;
    private TextView mDirectionTextView;
    private CameraCanvas mCameraHolder;
    private AugmentCanvas mAugmentCanvas;


    private CoordinatePoint presentPoint;
    private NearbyLocation nearbyLocation;
    private UpdateNearSpace updateNearSpace;

    private double direction;
    private String jsonNearLocationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Intent intent  = getIntent();
        if(intent.hasExtra("data")){
            jsonNearLocationData = intent.getStringExtra("data");
        }

        checkPermissions();

        mCoordinatesTextView = (TextView) findViewById(R.id.coordinates);
        mCameraHolder = (CameraCanvas) findViewById(R.id.camera_preview);
        mDirectionTextView = (TextView) findViewById(R.id.direction);
        mAugmentCanvas = (AugmentCanvas) findViewById(R.id.augment_canvas);



    }


    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//ask permissions only if version more than m
            for(String perm: PERMISSIONS_REQUIRED){
                if(checkSelfPermission(perm)!=PackageManager.PERMISSION_GRANTED){
                    permissionsNeeded.add(perm);
                }
            }
            if(!permissionsNeeded.isEmpty()){
                requestPermissions(new String[]{permissionsNeeded.get(0)},MY_PERMISSION_REQUEST_CODE);
                return;
            }
            else{//if no permission required then directly start the app functionality
                startApp2();
            }
        }
        else {//start the app functionality if android version of device is less than m
            startApp2();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSION_REQUEST_CODE){
            permissionsNeeded.remove(0);
            if(!permissionsNeeded.isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{permissionsNeeded.get(0)}, MY_PERMISSION_REQUEST_CODE);
                    return;
                }
            }
            else {
                startApp2();
            }
            /*boolean isOK=true;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                    isOK=false;
                    break;
                }
            }
            if(isOK){
                startApp();
                return;
            }
            else{
                finish();
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext())
                        .setMessage(getString(R.string.wont_work_description))
                        .setTitle(getString(R.string.wont_work))
                        .setCancelable(false)
                        .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setPositiveButton(getString(R.string.grant_permission), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(PERMISSIONS_REQUIRED,MY_PERMISSION_REQUEST_CODE);
                                }
                            }
                        });
                builder.create().show();
            }*/
        }
    }


    private void startApp(){
        //camera part of the application
        //mCameraCanvas = new CameraCanvas(this, mCameraHolder.getHolder(),null);//check null

        //users location detection part of the application
        mLocation = new LocateUser(getApplicationContext(),this);
        mLocation.buildApi();
        mLocation.connect();
        CoordinatePoint co = mLocation.getData();
        if (co != null) {
            mCoordinatesTextView.setText("latitude: " + co.getLatitude() + " longitude: " + co.getLongitude());
        } else {
            mCoordinatesTextView.setText("no data available");
        }

        //users Orientation detection part of the application
        mOrientation = new OrientUser(getApplicationContext(),this);
        double direction = mOrientation.getDirection();
        mDirectionTextView.setText("Direction: "+direction);

        //fetch locations
        NearbyLocation nearbyLocation = new NearbyLocation(getResources().getDisplayMetrics().widthPixels,getResources().getDisplayMetrics().heightPixels);

        //put the location on screen
        mAugmentCanvas.showLocation(nearbyLocation.getAugmentableNearPoints(direction,co));
    }

    private void startApp2(){

        //mCameraHolder.init();

        mLocation = new LocateUser(getApplicationContext(),this);

        //mLocation = new LocateUser(getApplicationContext());
        mOrientation = new OrientUser(getApplicationContext(),this);

        //presentPoint = new CoordinatePoint(13.345410, 74.795835);

        nearbyLocation = new NearbyLocation(getResources().getDisplayMetrics().widthPixels,getResources().getDisplayMetrics().heightPixels);
        nearbyLocation.populatePoints(jsonNearLocationData);

        updateNearSpace = new UpdateNearSpace();
        updateNearSpace.execute();
    }

    @Override
    public void onOrientationChange(double azimuthal) {
        mDirectionTextView.setText("orientation: "+azimuthal);
        direction = azimuthal;
        //mAugmentCanvas.showLocation(nearbyLocation.getAugmentableNearPoints(azimuthal,presentPoint));
    }

    public void buttonCapture(View v){
        mAugmentCanvas.showLocation(nearbyLocation.getAugmentableNearPoints(direction,presentPoint));
    }

    @Override
    public void onLocationChange(Location location) {
        if(location!=null) {
            mCoordinatesTextView.setText("latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
            presentPoint = new CoordinatePoint(location.getLatitude(), location.getLongitude());
        }
    }

    public class UpdateNearSpace extends AsyncTask<Void, Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            while(true){
                try {
                    publishProgress();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mAugmentCanvas.showLocation(nearbyLocation.getAugmentableNearPoints(direction,presentPoint));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateNearSpace.cancel(true);
    }
}
