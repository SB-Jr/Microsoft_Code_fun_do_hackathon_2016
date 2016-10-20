package kamehameha.beam.percept.location;

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
    }

    public ArrayList<CoordinatePoint> getAugmentableNearPoints(double deviceAngle,CoordinatePoint deviceLocation){

        for(CoordinatePoint point :nearPoints){
            double pointAngle = Math.atan2(point.getLongitude(),point.getLatitude());
            double diff = deviceAngle - pointAngle;
            if(diff>=HALF_SPAN_VIEW_OPPOSITE&&diff<=HALF_SPAN_VIEW){
                double x = (width/2)+(width/2)*(diff/60);
                point.setAugmentableX(x);
                point.setAugmentableY(height/2);
            }
        }

        return nearPoints;
    }

}
