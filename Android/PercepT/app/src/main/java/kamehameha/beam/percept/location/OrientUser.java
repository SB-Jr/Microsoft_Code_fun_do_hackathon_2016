package kamehameha.beam.percept.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaActionSound;

import kamehameha.beam.percept.callbackinterfaces.OrientationChangeCallback;

/**
 * Created by sbjr on 10/19/16.
 */

public class OrientUser implements SensorEventListener{

    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor mGeoMagnetic;
    private Sensor mAccelerometer;
    double x;
    double y;
    double z;


    private float[] mAccelerometerValues;
    private float[] mGeoMagneticValues;

    private double direction=0.0;
    private double oldDirection =0.0;

    private OrientationChangeCallback orientationChangeCallback;

    public OrientUser(Context mContext,OrientationChangeCallback orientationChangeCallback) {
        this.mContext = mContext;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGeoMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        this.orientationChangeCallback = orientationChangeCallback;

        registerListener();
    }

    public void unregisterListener(){
        mSensorManager.unregisterListener(this,mAccelerometer);
        mSensorManager.unregisterListener(this,mGeoMagnetic);
    }

    public void registerListener(){
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mGeoMagnetic,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public double getDirection() {
        return direction;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerometerValues = event.values;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeoMagneticValues = event.values;
        }

        if (mAccelerometerValues != null && mGeoMagneticValues != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mAccelerometerValues, mGeoMagneticValues);

            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                x = orientation[0];
                y = orientation[1];
                z = orientation[2];
                //double s = Math.sqrt(x*x + y*y + z*z);

                // at this point, orientation contains the azimuth(direction), pitch and roll values.
                double azimuth = 180*orientation[0]/Math.PI;//azimuth=[0,360),0 is north and 180 is south
                double pitch = 180*orientation[1]/Math.PI;
                double roll = 180*orientation[2]/ Math.PI;

                azimuth+=90;
                //roll+=90;

                direction = roll;

                //changing the range of direction to -180 to 180
                /*if(azimuth>180){
                    direction = azimuth-360;
                }
                else {
                    direction = azimuth;
                }*/

                if(Math.abs(direction-oldDirection)<=3.0){    //if change in angle is less, then no change
                    direction = oldDirection;
                }
                else{
                    oldDirection = direction;
                }
                orientationChangeCallback.onOrientationChange(direction);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
