package kamehameha.beam.percept.models;

/**
 * Created by sbjr on 10/19/16.
 */

public class CoordinatePoint {

    private double latitude;
    private double longitude;

    private double augmentableX;
    private double augmentableY;

    private String name;

    public CoordinatePoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CoordinatePoint(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLatitude() { return latitude;}

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAugmentableX() {
        return augmentableX;
    }

    public void setAugmentableX(double augmentableX) {
        this.augmentableX = augmentableX;
    }

    public double getAugmentableY() {
        return augmentableY;
    }

    public void setAugmentableY(double augmentableY) {
        this.augmentableY = augmentableY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
