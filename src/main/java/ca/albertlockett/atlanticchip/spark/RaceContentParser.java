package ca.albertlockett.atlanticchip.spark;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.api.java.function.Function;

import ca.albertlockett.atlanticchip.model.Race;
import ca.albertlockett.atlanticchip.model.RunningRace;
import ca.albertlockett.atlanticchip.model.Triathalon;

public class RaceContentParser implements Function<String, Race> {

	private static final long serialVersionUID = 4763990198093654100L;

	public Race call(String flatContent) {
		
		String[] lines = flatContent.split("\n");
		
		// try to find title line
		int titleLineIndex = 0;
		boolean titleFound = false;
		do {
			if("".equals(lines[titleLineIndex])) {
				titleLineIndex++;
			} else {
				titleFound = true;
			}
		} while(!titleFound);
		
		String titleLine = lines[titleLineIndex];
		
		// try to decide if triathalon or road race 
		Race race = null;
		if(titleLine.toLowerCase().contains("tri")) {
			race = new Triathalon();
		} else {
			race = new RunningRace();
			// try to parse distance from race title
			Pattern titlePattern = Pattern
					.compile("^\\s*([a-zA-Z\\d\\s]+)\\s*\\((.*)\\)\\s*$");
			
			Matcher titleMatcher = titlePattern.matcher(titleLine);
			if(titleMatcher.find()) {
				race.setName(titleMatcher.group(1).trim());
			}
			
			String distance = titleMatcher.group(2);
			if(distance.toLowerCase().matches("^.*15.*k.*")) {
				((RunningRace)race).setDistance(15.0); 
			} else if(distance.toLowerCase().matches("^.*10.*k.*")){
				((RunningRace)race).setDistance(10.0);
			} else if(distance.toLowerCase().matches("^.*5.*k.*")){
				((RunningRace)race).setDistance(5.0);
			} else if(distance.toLowerCase().matches("^.1/2.*")){
				((RunningRace)race).setDistance(21.2);
			} else if(distance.toLowerCase().matches("^.*half*")){
				((RunningRace)race).setDistance(21.2);
			} else if(distance.toLowerCase().matches("^.*full.*")){
				((RunningRace)race).setDistance(42.2);
			} else if(distance.toLowerCase().matches("^.*matathon.*")){
				((RunningRace)race).setDistance(42.2);
			}
			
		}
		
		if(race.getName() == null || "".equals(race.getName())) {
			race.setName(titleLine);
		}
		
		return race;
	}
	
}
