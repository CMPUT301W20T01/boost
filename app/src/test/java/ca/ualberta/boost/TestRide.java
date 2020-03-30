package ca.ualberta.boost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import com.google.android.gms.maps.model.LatLng;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.Rider;

public class TestRide {
    private Ride mockRide1() {
        // Nob Hill, San Francisco, CA, USA
        double lat1 = 37.793168;
        double lon1 = -122.415934;
        // Soma, San Francisco, CA, USA
        double lat2 = 37.769833;
        double lon2 = -122.409840;
        LatLng startLocation = new LatLng(lat1, lon1);
        LatLng endLocation = new LatLng(lat2, lon2);
        Ride ride = new Ride(startLocation, endLocation, 10.00, new Rider("Elton",
                                                                                "rocketman",
                                                                                "tinyDancer",
                                                                                "rocketman@gmail.com",
                                                                                "03251947").getUsername());
        return ride;
    }

    @Test
    void testFareCalc() {
        Ride ride = mockRide1();

        assertEquals(10.00, ride.getFare());

        assertEquals(13.83, ride.baseFare());
        
    }


}
