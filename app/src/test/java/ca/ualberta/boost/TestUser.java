package ca.ualberta.boost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import com.google.android.gms.maps.model.LatLng;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.Rider;

public class TestUser {
    private Driver mockDriver() {
        Driver driver = new Driver("Billy",
                                   "pianoman",
                                   "anthony",
                                   "bjoel@gmail.com",
                                   "05091949");
        return driver;
    }

    private Ride mockRide1() {
        // Cluj-Napoca, Romania
        double lat1 = 46.769968;
        double lon1 = 23.620605;
        // Berlin, Germany
        double lat2 = 52.616390;
        double lon2 = 13.447266;
        LatLng startLocation = new LatLng(lat1, lon1);
        LatLng endLocation = new LatLng(lat2, lon2);
        Ride ride = new Ride(startLocation, endLocation, 23.67, new Rider("Elton",
                                                                                "rocketman",
                                                                                "tinyDancer",
                                                                                "rocketman@gmail.com",
                                                                                "03251947"));
        return ride;
    }

    private Ride mockRide2() {
        // Singapore, Singapore
        double lat1 = 1.352083;
        double lon1 = 103.819839;
        // Kuala-Lumpur, Malaysia
        double lat2 = 3.154430;
        double lon2 = 101.715100;
        LatLng startLocation = new LatLng(lat1, lon1);
        LatLng endLocation = new LatLng(lat2, lon2);
        Ride ride = new Ride(startLocation, endLocation, 23.67, new Rider("Elton",
                                                                                "rocketman",
                                                                                "tinyDancer",
                                                                                "rocketman@gmail.com",
                                                                                "03251947"));
        return ride;
    }

    private ArrayList<Ride> mockRides() {
        Ride ride = mockRide1();
        ArrayList<Ride> rides = new ArrayList<>();
        rides.add(ride);

        return rides;
    }

    @Test
    void testSetGet() {
        Driver driver = mockDriver();
        assertEquals(driver.getFirstName(), "Billy");
        assertEquals(driver.getUsername(), "pianoman");

        driver.setFirstName("Bob");
        assertNotEquals(driver.getFirstName(), "Billy");
        assertEquals(driver.getFirstName(), "Bob");
    }

    @Test
    void testAddRide() {
        Driver driver = mockDriver();

        assertEquals(driver.getAllRides().size(), 0);

        Ride ride = mockRide1();
        driver.addRide(ride);
        assertEquals(driver.getAllRides().size(), 1);
    }

    @Test
    void testRating() {
        Driver driver = mockDriver();
        driver.updateRating(50);
        driver.updateRating(100);
        assertEquals(2, driver.getNumRates());
        assertEquals(75, driver.getRating());
    }
}
