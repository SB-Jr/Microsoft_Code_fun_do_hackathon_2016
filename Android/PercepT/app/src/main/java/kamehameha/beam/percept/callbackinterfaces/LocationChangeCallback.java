package kamehameha.beam.percept.callbackinterfaces;

import android.location.Location;
import android.location.LocationManager;

/**
 * Created by sbjr on 10/21/16.
 */

public interface LocationChangeCallback {

    public void onLocationChange(Location location);
}
