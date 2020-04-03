package ca.ualberta.boost;

import ca.ualberta.boost.models.Driver;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * DriverTest tests the Driver class
 */

public class DriverTest {

    private Driver createDriver(){
        Driver driver = new Driver("Alex","monster90","password","monster@gmail.com", "780-123-4567");
        return driver;
    }

    /**
     * testDriverInformation tests the if the attributes of a driver created by the createDriver function match
     */

    @Test
    public void testDriverInformation(){
        Driver newDriver = createDriver();
        assertEquals("Alex",newDriver.getFirstName());
        assertEquals("monster90",newDriver.getUsername());
        assertEquals("password",newDriver.getPassword());
        assertEquals("monster@gmail.com",newDriver.getEmail());
        assertEquals("780-123-4567",newDriver.getPhoneNumber());
    }

    /**
     * testDriverRating tests to see if a new driver has 0 positive ratings and 0 negative ratings
     */

    @Test
    public void testDriverRating(){
        Driver newDriver = createDriver();
        //after a new Driver object is created they should have zero positive and negative ratings
        assertEquals(0, newDriver.getPositiveRating());
        assertEquals(0, newDriver.getNegativeRating());
    }


}
