package kamehameha.beam.percept.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by sbjr on 10/11/16.
 * Camera class to which uses the camera object passed to it to fill the frame with camera view
 */

public class CameraCanvas extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mHolder;

    public CameraCanvas(Context context) {
        super(context);
        init();
    }


    public CameraCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mCamera = Camera.open();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try{
            mHolder = holder;
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(width,height);
            mCamera.setParameters(parameters);//dont set the parameters if camera not working
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
        }
    }
}
