package ca.ualberta.boost;

import com.google.android.gms.maps.model.LatLng;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.Rider;

public class TestActiveUser {
    private Rider mockRider() {
        return new Rider("Elton",
                        "rocketman",
                        "tinyDancer",
                        "rocketman@gmail.com",
                        "03251947");
    }

    private Ride mockRide() {
        // Nob Hill, San Francisco, CA, USA
        double lat1 = 37.793168;
        double lon1 = -122.415934;
        // Soma, San Francisco, CA, USA
        double lat2 = 37.769833;
        double lon2 = -122.409840;
        LatLng startLocation = new LatLng(lat1, lon1);
        LatLng endLocation = new LatLng(lat2, lon2);
        return new Ride(startLocation, endLocation, 10.00, "rocketman");
    }

    @Test
    void testLoginLogout() {
        ActiveUser.login(mockRider());
        assertEquals(mockRider(), ActiveUser.getUser());

        ActiveUser.logout();
        assertNull(ActiveUser.getUser());
    }
}
