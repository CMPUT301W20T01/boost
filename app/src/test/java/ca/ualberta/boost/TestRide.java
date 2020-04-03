package ca.ualberta.boost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
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

    private Ride mockRide2() {
        Ride ride = mockRide1();
        String driverUserName = new Driver("Jimi",
                                            "theExperience",
                                            "purpleHaze",
                                            "jimiHendrix@gmail.com",
                                            "2727272727").getUsername();
        ride.setDriverUsername(driverUserName);
        return ride;
    }

    private

    @Test
    void testFareCalc() {
        Ride ride = mockRide1();

        assertEquals(10.00, ride.getFare());

        assertEquals(13.83, ride.baseFare());
        
    }

    @Test
    void testData() {
        Ride ride = mockRide2();
        Map<String, Object> correctMap = new HashMap<>();
        correctMap.put("start_location", new GeoPoint(ride.getStartLocation().latitude,
                                                        ride.getStartLocation().longitude));
        correctMap.put("end_location", new GeoPoint(ride.getEndLocation().latitude,
                                                        ride.getEndLocation().longitude));
        correctMap.put("fare", ride.getFare());
        correctMap.put("driver", ride.getDriverUsername());
        correctMap.put("rider", ride.getRiderUsername());
        correctMap.put("status", ride.getRideStatus().getValue());
        correctMap.put("request_time", ride.getRequestTime());
        Map<String, Object> map = ride.data();
        assertEquals(correctMap, map);
    }

    @Test
    void testBuild() {
        Ride ride = mockRide2();
        Timestamp timestamp = new Timestamp(ride.getRequestTime());
        Long status = new Long(ride.getRideStatus().getValue());
        Map<String, Object> map = ride.data();
        map.put("request_time", timestamp);
        map.put("status", status);
        Ride newRide = Ride.build(map);
        assertEquals(ride.getDriverUsername(), newRide.getDriverUsername());
        assertEquals(ride.getEndLocation(), newRide.getEndLocation());
        assertEquals(ride.getFare(), newRide.getFare());
        assertEquals(ride.getRequestTime(), newRide.getRequestTime());
        assertEquals(ride.getRiderUsername(), newRide.getRiderUsername());
        assertEquals(ride.getRideStatus(), newRide.getRideStatus());
        assertEquals(ride.getStartLocation(), newRide.getStartLocation());
    }


}
