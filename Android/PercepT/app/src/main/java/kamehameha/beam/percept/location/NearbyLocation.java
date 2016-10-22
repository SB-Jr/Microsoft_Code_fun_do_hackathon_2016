package kamehameha.beam.percept.location;

import android.util.Log;

import java.util.ArrayList;

import kamehameha.beam.percept.models.CoordinatePoint;

/**
 * Created by sbjr on 10/20/16.
 */

public class NearbyLocation {

    private ArrayList<CoordinatePoint> nearPoints;
    private int width;
    private int height;

    private static final double HALF_SPAN_VIEW = 60;//angle at max a point can be to be show on the screen
    private static final double HALF_SPAN_VIEW_OPPOSITE = -1*HALF_SPAN_VIEW;
    private static final double MAX_DISTANCE_COVERED=0.01;//diff between 2 latitudes can at max be 0.01


    public NearbyLocation(int width,int height) {
        populatePoints();
        this.width = width;
        this.height = height;
    }

    public void populatePoints(){
        nearPoints = new ArrayList<>();
        nearPoints.add(new CoordinatePoint(13.352274, 74.792898,"AB1"));
        nearPoints.add(new CoordinatePoint(13.345433, 74.794904,"Lipton"));
        nearPoints.add(new CoordinatePoint(13.341942, 74.794597,"Block 10"));
    }

    public ArrayList<CoordinatePoint> getAugmentableNearPoints(double deviceAngle,CoordinatePoint deviceLocation){

        ArrayList<CoordinatePoint> augmentPoints = new ArrayList<>();

        for(CoordinatePoint point :nearPoints){
            double latDiff = point.getLatitude()-deviceLocation.getLatitude();
            double longDiff = point.getLongitude() - deviceLocation.getLongitude();
            double pointAngle = 180*Math.atan2(Math.abs(latDiff),Math.abs(longDiff))/Math.PI;
            Log.d("augmented angle",pointAngle+"");

            if(point.getLongitude()<deviceLocation.getLongitude()){
                if(point.getLongitude()>deviceLocation.getLongitude()) {
                    pointAngle = (90 - pointAngle) * (-1);
                }
                else{
                    pointAngle = (90 + pointAngle) * (-1);
                }
            }
            else{
                if(point.getLongitude()>deviceLocation.getLongitude()) {
                    pointAngle = (90 - pointAngle);
                }
                else{
                    pointAngle = (90 + pointAngle);
                }
            }
            double diff = deviceAngle - pointAngle;
            Log.d("augmented difference",diff+"");
            if(diff>=HALF_SPAN_VIEW_OPPOSITE&&diff<=HALF_SPAN_VIEW){
                double x = (width/2)+(width/2)*(diff/60);
                point.setAugmentableX(x);
                point.setAugmentableY(height/2);
                augmentPoints.add(point);
            }
        }
        Log.d("augmeted Points Set",augmentPoints.size()+"");
        return augmentPoints;
    }

}
