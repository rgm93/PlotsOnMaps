package Extra;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

public class OrdenarCoordenadas implements Comparator<LatLng> {
    LatLng currentLoc;

    public OrdenarCoordenadas(LatLng current){
        currentLoc = current;
    }
    @Override
    public int compare(final LatLng ll1, final LatLng ll2) {
        double lat1 = ll1.latitude;
        double lon1 = ll1.longitude;
        double lat2 = ll2.latitude;
        double lon2 = ll2.longitude;

        double distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1);
        double distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2);
        return (int) (distanceToPlace1 - distanceToPlace2);
    }

    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }
}
