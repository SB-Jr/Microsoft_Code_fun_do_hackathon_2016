package kamehameha.beam.percept.utility;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Created by sbjr on 10/11/16.
 * Camera class to which uses the camera object passed to it to fill the frame with camera view
 */

public class CameraCanvas extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mHolder;

    public CameraCanvas(Context context, Camera camera,SurfaceHolder holder) {
        super(context);
        mCamera = camera;
        mHolder = holder;
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null){
            mCamera.stopPreview();
        }
    }
}
