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
        JavaSparkContaxt sc = new JavaSparkContaxt(conf);
    }
}
