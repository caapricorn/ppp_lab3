
mvn package
hadoop fs -rm -f -r output
spark-submit --class FlightStatisticsApp --master yarn-client --num-executors 3 ./target/spark-examples-1.0-SNAPSHOT.jar 
rm -rf output
hadoop fs -copyToLocal output
