package kamehameha.beam.percept.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;
import java.util.ArrayList;

import kamehameha.beam.percept.R;
import kamehameha.beam.percept.camera.CameraCanvas;
import kamehameha.beam.percept.location.LocateUser;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraCanvas mCameraCanvas;
    private LocateUser mLocation;


    private static final int MY_PERMISSION_REQUEST_CODE = 6969;
    private static final String[] PERMISSIONS_REQUIRED = {  Manifest.permission.CAMERA,
                                                            Manifest.permission.LOCATION_HARDWARE,
                                                            Manifest.permission.CONTROL_LOCATION_UPDATES,
                                                            Manifest.permission.ACCESS_COARSE_LOCATION};

    private TextView coordinatesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatesText = (TextView) findViewById(R.id.coordinates);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!hasPermissions()) {
                requestPermissions(PERMISSIONS_REQUIRED,MY_PERMISSION_REQUEST_CODE);
            }
            else{
                startApp();
            }
        }
        else {
            startApp();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermissions(){
        for(String perm: PERMISSIONS_REQUIRED){
            if(checkSelfPermission(perm)!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSION_REQUEST_CODE){
            boolean isOK=true;
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
            }
        }
    }


    private void startApp(){
        mCamera = Camera.open();
        SurfaceView cameraHolder = (SurfaceView) findViewById(R.id.camera_preview);

        mCameraCanvas = new CameraCanvas(this, mCamera, cameraHolder.getHolder());

        mLocation = new LocateUser(getApplicationContext());

        mLocation.buildApi();
        mLocation.connect();

        ArrayList<Double> co = mLocation.getData();
        if (co != null) {
            coordinatesText.setText("latitude: " + co.get(0) + " longitude: " + co.get(1));
        } else {
            coordinatesText.setText("no data available");
        }
    }




}
