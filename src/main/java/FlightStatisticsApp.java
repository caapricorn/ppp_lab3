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

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: <input path Airport> <input path Flight> <output path>");
            System.exit(-1);
        }
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        String flightPath = args[0];
        String airportPath = args[1];
        String outputPath = args[2];

        JavaRDD<String> flightFile = sc.textFile(flightPath);
        JavaRDD<String> airportFile = sc.textFile(airportPath);

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

        flightFile = CSVParser.parseFlight(flightFile);
    }
}
