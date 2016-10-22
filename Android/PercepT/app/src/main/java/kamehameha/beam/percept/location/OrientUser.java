package kamehameha.beam.percept.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import kamehameha.beam.percept.callbackinterfaces.OrientationChangeCallback;

/**
 * Created by sbjr on 10/19/16.
 */

public class OrientUser implements SensorEventListener{

    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor mGeoMagnetic;
    private Sensor mAccelerometer;

    private float[] mAccelerometerValues;
    private float[] mGeoMagneticValues;

    private double direction=0.0;

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

                // at this point, orientation contains the azimuth(direction), pitch and roll values.
                double azimuth = 180*orientation[0]/Math.PI;
                double pitch = orientation[1];
                double roll = orientation[2];

                //put condition for limiting
                /*if(direction==0.0){
                    direction = azimuth + 90;
                    orientationChangeCallback.onOrientationChange(direction);
                }

                if(azimuth+90-direction>=1) {
                    direction = azimuth + 90;
                    orientationChangeCallback.onOrientationChange(direction);
                }*/
                if(azimuth>=90){
                    direction = -270+azimuth;
                }
                else {
                    direction = azimuth + 90;
                }
                orientationChangeCallback.onOrientationChange(direction);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
