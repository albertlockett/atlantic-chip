package ca.albertlockett.atlanticchip.spark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ca.albertlockett.atlanticchip.model.Race;
import ca.albertlockett.atlanticchip.model.RunningRace;
import ca.albertlockett.atlanticchip.util.DateTimeUtils;
import ca.albertlockett.dao.AbstractModelDao;

public class SparkApp1 {
	
	private static final AbstractModelDao modelDao = new AbstractModelDao();
	
	public static void main(String[] args) {
		
		// try to parse application arguements
		Map<String, Object> params = null;
		try {
			params = parseArguements(args);
		} catch(IllegalArgumentException e) {
			printHelpInformation();
			return;
		}
		
		SparkConf conf = new SparkConf().setAppName("App1").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<Integer> eventIds = new ArrayList<Integer>();
		for(int i = 2000; i < 2001; i++) {
			eventIds.add(i);
		}
		
		JavaRDD<Integer> eventIdsRDD = sc.parallelize(eventIds);
		
		// get page content from url
		JavaRDD<String> content = eventIdsRDD.map(new Function<Integer, String>() {
			public String call(Integer eventId) throws Exception {
				URL resultsPage = new URL("http://albertlockett.ca/pages/" 
							+ eventId + ".html");
				
				BufferedReader in = new BufferedReader(
						new InputStreamReader(resultsPage.openStream(), 
								StandardCharsets.ISO_8859_1));
				StringBuilder pageContent = new StringBuilder();
				while(in.ready()) {
					pageContent.append(in.readLine()).append("\n");
					
				}
				return pageContent.toString();
			}
		});
		
		// filter page content for errors
		JavaRDD<String> contentNoErrors = content.filter(new Function<String, Boolean>() {
			public Boolean call(String pageContent) throws Exception {
				return !pageContent.toLowerCase().contains("error");
			}
		});
		
		// try to parse out only pre formatted content
		JavaRDD<String> preContent = contentNoErrors.map(new Function<String, String>() {
			public String call(String pageContent) throws Exception {
				Document doc = Jsoup.parse(pageContent);
				Element pre = doc.select("pre").first();
				try {
					return pre.text();
				} catch(Exception e) {
					return "";
				}
			}
		}).filter(new Function<String, Boolean>() { // filter not null
			public Boolean call(String content) throws Exception {
				return content != null && !"".equals(content);
			}
		});
		
		JavaRDD<Race> races = preContent.map(new RaceContentParser());
		
		// run parsing and time it
		Date before = new Date();
		List<Race> races2 = races.collect();
		Date after = new Date();
		
		// process processed races
		for(Race race : races2) {
			
			if(race == null) {
				System.err.println("Error - race returned null");
				continue;
			}
			
			// build log output for each race
			StringBuilder raceDescriptor = new StringBuilder();
			if(race instanceof RunningRace) {
				raceDescriptor.append("Run").append(",\t");
			} else {
				raceDescriptor.append("Triathalong").append(",\t");
			}
			
			raceDescriptor.append(race.getName()).append(",\t");
			
			if(race instanceof RunningRace) {
				RunningRace run = (RunningRace) race;
				raceDescriptor.append(run.getDistance());
				
				if(race.getRacers() != null) {
					raceDescriptor.append(",\t")
					.append(race.getRacers().size())
					.append(" racers");
				} else {
					raceDescriptor.append(",\t Error getting racers");
				}
				
			}
			
			// log
			System.out.println(raceDescriptor.toString());
			
			// possibly persist
			if(params.get("persist").equals(true)) {
				modelDao.save(race);
			}
		}
		
		// print time to run
		System.out.println("running time:");
		DateTimeUtils.printDifference(before, after);
	}
	
	
	
	private static Map<String, Object> parseArguements(String[] args) 
			throws IllegalArgumentException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("persist", true);
		return params;
	}
	
	public static void printHelpInformation() {
		StringBuilder helpInfo = new StringBuilder();
		helpInfo.append("here is some useful help info\n")
			.append("TODO");
	}
}
