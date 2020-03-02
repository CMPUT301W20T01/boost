package ca.ualberta.boost;

public class RideRequest {

    String pickup;
    String destination;
    String amount;
    String email;

    public RideRequest(String pickup, String destination, String amount, String email) {
        this.pickup = pickup;
        this.destination = destination;
        this.amount = amount;
        this.email = email;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
