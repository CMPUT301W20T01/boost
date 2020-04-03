package ca.ualberta.boost;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Rider;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RiderTest {

    /**
     * The RiderTest class contains tests related to testing the Rider object
     */

    public Rider createRider(){
        Rider newRider = new Rider("Alex","ab345","password","ab@gmail.com","780-123-4567");
        return newRider;
    }

    /**
     * testRiderInformation verifies that rider information is correct
     */

    @Test
    public void testRiderInformation(){
        Rider rider = createRider();
        assertEquals("Alex",rider.getFirstName());
        assertEquals("ab345",rider.getUsername());
        assertEquals("password",rider.getPassword());
        assertEquals("780-123-4567",rider.getPhoneNumber());
    }

}
