import java.io.Serializable;

public class FlightSerializable implements Serializable {

    private int originAirportId;
    private int destAirportId;
    private float delayTime;
    private int isCancelled;

    public FlightSerializable() {
    }

    
}
