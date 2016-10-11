package kamehameha.beam.percept.ui;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import kamehameha.beam.percept.R;
import kamehameha.beam.percept.utility.CameraCanvas;

public class MainActivity extends AppCompatActivity{

    private Camera mCamera;
    private CameraCanvas mCameraCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCamera = Camera.open();
        SurfaceView cameraHolder = (SurfaceView) findViewById(R.id.camera_preview);

        mCameraCanvas = new CameraCanvas(this,mCamera,cameraHolder.getHolder());

    }
}
