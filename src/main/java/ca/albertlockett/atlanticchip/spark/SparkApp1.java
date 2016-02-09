package ca.albertlockett.atlanticchip.spark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkApp1 {

	
	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("App1").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<Integer> eventIds = new ArrayList<Integer>();
		for(int i = 0; i < 100; i++) {
			eventIds.add(i);
		}
		
		JavaRDD<Integer> eventIdsRDD = sc.parallelize(eventIds);
		
		eventIdsRDD.map(eventId -> {
			URL resultsPage = new URL("http://albertlockett.ca/pages/" + eventId + ".html");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(resultsPage.openStream(), 
							StandardCharsets.ISO_8859_1));
			boolean error = in.lines().anyMatch(s -> s.toLowerCase().contains("error"));
			return error ? -1 : 1;
		}).foreach(f -> System.out.println(f));
		
	}
	
}
