package kamehameha.beam.percept.location;

import android.location.Location;
import android.location.LocationManager;

/**
 * Created by sbjr on 10/21/16.
 */

public interface LocationChangeCallback {

    public void onLocationChange(Location location);
}
