package ca.albertlockett.atlanticchip.spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.albertlockett.atlanticchip.dao.AbstractModelDao;
import ca.albertlockett.atlanticchip.model.Race;
import ca.albertlockett.atlanticchip.model.RunningRace;
import ca.albertlockett.atlanticchip.util.DateTimeUtils;
import ca.albertlockett.atlanticchip.util.MultiLineStringBuilder;

public class SparkApp1 {
	
	private static final Logger logger = LoggerFactory
			.getLogger(SparkApp1.class);

	private static AbstractModelDao modelDao = new AbstractModelDao();
	
	public static void main(String[] args) throws Exception {
		
		// show help menu?
		for(String arg : args) {
			if(arg.equals("--help")) {
				printHelpInformation(); return;
			}
		}
		
		// try to parse application arguements
		Map<String, Object> params = null;
		try {
			params = parseArguements(args);
		} catch(IllegalArgumentException e) {
			logger.error(e.getMessage());
			printHelpInformation();
			return;
		}
		
		// configure spark
		String master = (String) params.get("master");
		SparkConf conf = new SparkConf().setAppName("App1").setMaster(master);
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		// create RDD with event IDs
		List<Integer> eventIds = new ArrayList<Integer>();
		Integer minEventId = (Integer) params.get("minEventId");
		Integer maxEventId = (Integer) params.get("maxEventId");
		for(int i = minEventId; i < maxEventId; i++) {
			eventIds.add(i);
		}
		JavaRDD<Integer> eventIdsRDD = sc.parallelize(eventIds);
		
		// get page content from url
		String baseUrl = (String) params.get("baseUrl");
		JavaRDD<String> content = eventIdsRDD.map(new LoadRaceInfoHtml(baseUrl));
		
		// filter page content for errors
		JavaRDD<String> contentNoErrors = content.filter(
				new Function<String, Boolean>() {
			private static final long serialVersionUID = 5985725493233165226L;
			public Boolean call(String pageContent) throws Exception {
				return !pageContent.toLowerCase().contains("error");
			}
		});
		
		// try to parse out only pre formatted content
		JavaRDD<String> preContent = contentNoErrors
			.map(new ParsePreRaceInfoFromHtmlDoc())
			.filter(new Function<String, Boolean>() { // filter not null
				private static final long serialVersionUID = 
						2654986630629950682L;
				public Boolean call(String content) throws Exception {
					return content != null && !"".equals(content);
				}
		});
		
		// parse race content from preformated race info
		JavaRDD<Race> racesRDD = preContent.map(new RaceContentParser());
		
		// run parsing and time it
		Date before = new Date();
		List<Race> races = racesRDD.collect();
		Date after = new Date();
		logger.info("running time:");
		DateTimeUtils.printDifference(before, after);
		
		// log race event info if user wishes
		if((Boolean) params.get("logEventInfo")) {
			printRaceInfo(races);
		}
		
		// persist race info to db if user wishes
		if((Boolean) params.get("persist")) {
			persistRaces(races);
		}
		
		sc.close();
	}
	
	private static void printRaceInfo(List<Race> races) {
		for(Race race : races) {
			if(race == null) {
				logger.error("Error - race returned null");
				continue;
			}
			
			// build log output for each race
			StringBuilder raceDescriptor = new StringBuilder();
			if(race instanceof RunningRace) {
				raceDescriptor.append("Run").append(",");
			} else {
				raceDescriptor.append("Triathalon").append(",");
			}
			
			raceDescriptor.append(race.getName()).append(",");
			
			if(race instanceof RunningRace) {
				RunningRace run = (RunningRace) race;
				raceDescriptor.append(run.getDistance());
				
				if(race.getRacers() != null) {
					raceDescriptor.append(",")
					.append(race.getRacers().size())
					.append(" racers");
				} else {
					raceDescriptor.append(",Error getting racers");
				}
			}
			
			// log
			logger.info(raceDescriptor.toString());
		}
	}
	
	private static void persistRaces(List<Race> races) throws Exception {
		
		// TODO: have the app set the race ID
		int raceId = 0;
		for(Race race : races) {
			try {
				if(race.getRaceId() == null) {
					race.setRaceId(raceId++);
					modelDao.save(race);
				}
			} catch(Exception e) {
				logger.error("error saving race {}", race.toString());
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		
		
	}
	
	// parse application argeuments from 
	private static Map<String, Object> parseArguements(String[] args) 
			throws IllegalArgumentException {
		Map<String, Object> params = new HashMap<String, Object>();
		
		// put default configurations in param map for non required args ---
		params.put("logRaceInfo", false);
		params.put("persist", false);
		params.put("minEventId", 0);
		params.put("master", "local");
		
		// try to parse arguments from what's passed at runtime
		for(int i = 0; i < args.length; i++) {
			
			// required arguments -----------------------------------------
			
			// base URL
			if(args[i].equals("--baseUrl")) {
				String baseUrlErrMsg = 
						"Expected valid URL as argument for --baseUrl flag";
				if(i == args.length - 1) {
					throw new IllegalArgumentException(baseUrlErrMsg);
				}
				if(args[i + 1].startsWith("--")) {
					throw new IllegalArgumentException(baseUrlErrMsg);
				}
				params.put("baseUrl", args[i + 1]);
				i++; continue;
			}
			
			if(args[i].equals("--master")) {
				String masterErrMsg = 
						"Expected parameter as arguement to master";
				if(i == args.length - 1) {
					throw new IllegalArgumentException(masterErrMsg);
				}
				if(args[i + 1].startsWith("--")) {
					throw new IllegalArgumentException(masterErrMsg);
				}
				params.put("master", args[i + 1]);
				i++; continue;
				
			}
			
			// parse maxEventID argument
			if(args[i].equals("--maxEventId")) {
				String maxEventIdMsg = 
						"Expected number as arguement to --maxEventId flag";
				if(i == args.length - 1) {
					throw new IllegalArgumentException(maxEventIdMsg);
				}
				try {
					Integer maxEventId = Integer.parseInt(args[i + 1]);
					params.put("maxEventId", maxEventId);
				} catch(NumberFormatException e) {
					throw new IllegalArgumentException(maxEventIdMsg);
				}
			}
			
			// parse minEventId argument
			if(args[i].equals("--minEventId")) {
				String minEventIdMsg = 
						"Expected number as arguement to --minEventId flag";
				if(i == args.length - 1) {
					throw new IllegalArgumentException(minEventIdMsg);
				}
				try {
					Integer minEventId = Integer.parseInt(args[i + 1]);
					params.put("minEventId", minEventId);
					i++; continue;
				} catch(NumberFormatException e) {
					throw new IllegalArgumentException(minEventIdMsg);
				}
			}
			
			// parse logEventInfo argument
			if(args[i].equals("--logEventInfo")) {
				String logEventInfoErrMsg = 
					"Expected true/false as argument to --logEventInfo flag";
				if(i == args.length - 1) {
					throw new IllegalArgumentException(logEventInfoErrMsg);
				}
				String logEventInfo = args[i + 1];
				if(logEventInfo.toLowerCase().equals("true")) {
					params.put("logEventInfo", true);
					i++; continue;
				} else if(logEventInfo.toLowerCase().equals("false")) {
					params.put("logEventInfo", false);
					i++; continue;
				} else {
					throw new IllegalArgumentException(logEventInfoErrMsg);
				}
			}
			
			// parse persist argument
			if(args[i].equals("--persist")) {
				String persistErrMsg = 
						"Expected true/false as arguement to --persist flag";
				if(i == args.length - 1) {
					throw new IllegalArgumentException(persistErrMsg);
				}
				String persist = args[i + 1];
				if(persist.toLowerCase().equals("true")) {
					params.put("persist", true);
					i++; continue;
				} else if(persist.toLowerCase().equals("false")) {
					params.put("persist", false);
					i++; continue;
				} else {
					throw new IllegalArgumentException(persistErrMsg);
				}
			}
			
		}
		
		// check that params contains all required arguements
		List<String> requiredArgs = Arrays.asList("baseUrl", "maxEventId");
		for(String argName : requiredArgs) {
			if(!params.containsKey(argName)) {
				throw new IllegalArgumentException("--" + argName + 
						" is a required argument");
			}
		}
		
		return params;
	}
	
	
	public static void printHelpInformation() {
		MultiLineStringBuilder helpInfo = new MultiLineStringBuilder();
		helpInfo.append("\n")
				.addLine("Albert's atlanticchip.ca scraper")
				.addLine("TODO: Add description of purpose for")
				.addLine("====================================================")
				
				.addLine("REQUIRED ARGUMENTS ---")
				// baseUrl
				.addLine("\t--baseUrl: base url to load race information from")
				// maxEventId
				.addLine("\t--maxEventId: max event Id to load race info for")
				
				.addLine("OPTINOAL ARGUMENTS ---")
				// master
				.addLine("\t--master:url of spark master")
				// minEventId
				.append("\t--minEventId: min event ID to load race info for")
					.append(" - defaults to 0").append("\n")
				// log race info
				.append("\t--logEventInfo: print info for parsed events to ")
					.append("std out - defaults to false").append("\n")
				// persist
				.append("\t--persist=<true/false> - persist races/racers to db")
					.append(" defaults to false").append(" \n")
				
				// credits
				.addLine("----------------------------------------------------")
				.addLine("Author: Albert Lockett - 2016");
		
		logger.info(helpInfo.toString());
	}
	
}
