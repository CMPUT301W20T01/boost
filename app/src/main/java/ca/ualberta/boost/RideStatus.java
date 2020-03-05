package ca.ualberta.boost;

public class RideStatus {
    private Boolean pending;
    private Boolean accepted;
    private Boolean finished;
    private Boolean cancelled;

    // constructors
    public RideStatus(Boolean pending, Boolean accepted, Boolean finished, Boolean cancelled) {
        this.pending = pending;
        this.accepted = accepted;
        this.finished = finished;
        this.cancelled = cancelled;
    }

    public RideStatus(){

    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
}
