import java.io.Serializable;

public class FlightSerializable implements Serializable {

    private int originAirportId;
    private int destAirportId;
    private float delayTime;
    private int isCancelled;

    public FlightSerializable() {
    }

    public FlightSerializable(int originAirportId, int destAirportId, float delayTime, int isCancelled) {
        this.originAirportId = originAirportId;
        this.destAirportId = destAirportId;
        this.delayTime = delayTime;
        this.isCancelled = isCancelled;
    }
}
