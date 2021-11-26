import java.io.Serializable;

public class FlightCounter implements Serializable {
    private float maxDelay;
    private int delayFlights;
    private int cancelledFlights;
    private int flightCount;

    public FlightCounter() {
    }

    public FlightCounter(float maxDelay, int delayFlights, int cancelledFlights, int flightCount) {
        this.maxDelay = maxDelay;
        this.delayFlights = delayFlights;
        this.cancelledFlights = cancelledFlights;
        this.flightCount = flightCount;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

    public void setDelayFlights(int delayFlights) {
        this.delayFlights = delayFlights;
    }

    public void setCancelledFlights(int cancelledFlights) {
        this.cancelledFlights = cancelledFlights;
    }

    public void setFlightCount(int flightCount) {
        this.flightCount = flightCount;
    }

    public float getMaxDelay() {
        return maxDelay;
    }

    public int getDelayFlights() {
        return delayFlights;
    }

    public int getCancelledFlights() {
        return  cancelledFlights;
    }

    public int getFlightCount() {
        return flightCount;
    }

    public static FlightCounter add(FlightCounter a, FlightCounter b) {
        return new FlightCounter(
                a.getMaxDelay() + b.getMaxDelay(),
                a.getDelayFlights() + b.getDelayFlights(),
                a.getCancelledFlights() + b.getCancelledFlights(),
                a.getFlightCount() + b.getFlightCount()
        );
    }

    
}
