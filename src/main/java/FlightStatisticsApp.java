import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class FlightStatisticsApp {
    private static final int ORIGIN_AIRPORT_ID = 11;
    private static final int DEST_AIRPORT_ID = 14;
    private static final int DELAY_TIME = 18;
    private static final int IS_CANCELLED = 19;

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> flightFile = sc.textFile("664600583_T_ONTIME_sample.csv");
        JavaRDD<String> airportFile = sc.textFile("L_AIRPORT_ID.csv");

        airportFile = CSVParser.parseAirport(airportFile);

        JavaPairRDD<Integer, String> airportData =  airportFile
                .mapToPair(s -> {
                    int commaIndex = s.indexOf(",");
                    return new Tuple2<>(
                            Integer.parseInt(s.substring(0, commaIndex)),
                            s.substring(commaIndex + 1, s.length())
                    );
                }
                );

        Map<Integer, String> stringAirportDataMap = airportData.collectAsMap();

        final Broadcast<Map<Integer, String>> airportsBroadcasted = sc.broadcast(stringAirportDataMap);

        flightFile = CSVParser.parseFlight(flightFile);

        JavaPairRDD<Tuple2<Integer, Integer>, FlightSerializable> flightData = flightFile
                .mapToPair(s -> {
                    String[] colums = s.split(",");
                    int originAirportId = Integer.parseInt(colums[ORIGIN_AIRPORT_ID]);
                    int destAirportId = Integer.parseInt(colums[DEST_AIRPORT_ID]);
                    float delayTime = (colums[DELAY_TIME].equals("")) ? 0.0f : Float.parseFloat(colums[DELAY_TIME]);
                    return new Tuple2<>(
                            new Tuple2<>(originAirportId, destAirportId),
                            new FlightSerializable(originAirportId, destAirportId, delayTime, (int) Float.parseFloat(colums[IS_CANCELLED]))
                    );
                });

        JavaPairRDD<Tuple2<Integer, Integer>, String> flightResult = flightData
                .combineByKey(
                        p -> new FlightCounter(p.getDelayTime(),
                                p.getDelayTime() > 0.0f ? 1 : 0,
                                p.getIsCancelled() == 1.0f ? 1 : 0,
                                1
                                ),
                        (flightCounter, p) -> FlightCounter.addValue(flightCounter,
                                p.getIsCancelled() == 1.0f,
                                p.getDelayTime() > 0.0f,
                                p.getDelayTime()),
                        FlightCounter::add
                )
                .mapToPair(
                        a -> new Tuple2<>(a._1(), FlightCounter.output(a._2()))
                );

        JavaRDD<String> resultOutput = flightResult
                .map(a -> {
                    Map<Integer, String> airportOriginId = airportsBroadcasted.value();
                    Tuple2<Integer, Integer> key = a._1();
                    String value = a._2();

                    String originAirportName = airportOriginId.get(key._1());
                    String destAirportName = airportOriginId.get(key._2());

                    return originAirportName + " ===> " + destAirportName + "\n" + value;
                });

        resultOutput.saveAsTextFile("output");
    }
}
