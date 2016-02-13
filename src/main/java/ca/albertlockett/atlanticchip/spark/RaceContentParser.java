package ca.albertlockett.atlanticchip.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.api.java.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.albertlockett.atlanticchip.model.Race;
import ca.albertlockett.atlanticchip.model.Racer;
import ca.albertlockett.atlanticchip.model.RunningRace;
import ca.albertlockett.atlanticchip.model.Triathalon;

public class RaceContentParser implements Function<String, Race> {

	private static final Logger logger = LoggerFactory
			.getLogger(RaceContentParser.class);
	
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
			
			// find distance
			try {
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
			} catch(Exception e) {
				logger.error("Could not parse distance from race title" + 
						titleLine);
			}
			
		}
		
		
		// name race if couldn't parse from title line
		if(race.getName() == null || "".equals(race.getName())) {
			race.setName(titleLine);
		}
		
		
		// find results header
		int currentIndex = titleLineIndex;
		do {
			currentIndex++;
			if(currentIndex >= lines.length) {
				StringBuilder errorMessage = new StringBuilder();
				errorMessage.append("parsed whole file, could not find results")
					.append(" header. Returning race as is");
				return race;
			}
		} while(!lines[currentIndex].matches("^=+[A-Za-z\\d\\s]+=+\\s*$"));
		
		// try to parse legend lines
		currentIndex++;
		List<String> legendLines = new ArrayList<String>();
		while(!lines[currentIndex].matches("^(=+\\s+)+.*$")) {
			legendLines.add(lines[currentIndex]);
			currentIndex++;
		}
		
		// legend spacer line is next line
		char[] legendSpacerLine = lines[currentIndex].toCharArray();
		List<Integer> fieldStartIndices = new ArrayList<Integer>();
		List<Integer> fieldEndIndices = new ArrayList<Integer>();
		
		// from legend line, need to compute start and end sequences
		for(int i = 0; i < legendSpacerLine.length; i++) {
			if(i == 0) {
				fieldStartIndices.add(0);
				continue;
			}
			
			if(legendSpacerLine[i] == ' ' && legendSpacerLine[i - 1] == '=') {
				fieldEndIndices.add(i);
				continue;
			}
			
			if(legendSpacerLine[i] == '=' && legendSpacerLine[i - 1] == ' ') {
				fieldStartIndices.add(i);
				continue;
			}
			
			if(i == legendSpacerLine.length - 1 && legendSpacerLine[i] == '=') {
				fieldEndIndices.add(i);
			}
		}
		
		// get legend field names from legend lines
		List<String> legendFieldNames = new ArrayList<String>();
		for(int i = 0; i < fieldStartIndices.size(); i++) {
			int startI = fieldStartIndices.get(i);
			int endI = fieldEndIndices.get(i);
			
			StringBuilder fieldName = new StringBuilder();
			
			for(String legendLine : legendLines) {
				
				if(endI > legendLine.length()) {
					endI = legendLine.length();
				}
				try {
					fieldName.append(legendLine.substring(startI, endI).trim())
							.append(" ");
				} catch(Exception e) {
					StringBuilder errorMessage = new StringBuilder();
					errorMessage.append("Could not parse field name from ")
							.append(startI).append(" - ").append(endI)
							.append("from line '").append(legendLine)
							.append("'");
					logger.error(errorMessage.toString());
				}
			}
			legendFieldNames.add(fieldName.toString().trim());
		}
		
		// try to parse racer information
		int racerLineIndex = ++currentIndex;
		List<Racer> racers = new ArrayList<Racer>();
		while(racerLineIndex < lines.length && 
				!lines[racerLineIndex].matches("^\\s$")) {
		
			String racerLine = lines[racerLineIndex];
			racerLineIndex++;
			
			Racer racer = new Racer();
			
			for(int i = 0; i < legendFieldNames.size(); i++) {
				
				int startI = fieldStartIndices.get(i);
				int endI = fieldEndIndices.get(i);
				if(endI > racerLine.length()) {
					endI = racerLine.length();
				}
				
				
				String fieldInfo = "";
				try {
					fieldInfo = racerLine.substring(startI, endI).trim();
				} catch(Exception e) {
					logger.error("error parsing line: " + racerLine);
					break;
				}
				
				String fieldName = legendFieldNames.get(i);
				
				// place
				if(fieldName.toLowerCase().trim().equals("place")) {
					racer.setPlace(fieldInfo);
					continue;
				}
				
				// bib number
				if(fieldName.toLowerCase().trim().equals("no.")) {
					racer.setBibNo(fieldInfo);
					continue;
				}
				if(fieldName.toLowerCase().trim().matches("^.*bib.*no.*$")) {
					racer.setBibNo(fieldInfo);
					continue;
				}
				
				// name
				if(fieldName.toLowerCase().trim().equals("name")) {
					racer.setName(fieldInfo);
					continue;
				}
				
				// city/town
				if(fieldName.toLowerCase().trim().matches("^.*city.*$")){
					racer.setCity(fieldInfo);
					continue;
				}
				if(fieldName.toLowerCase().trim().matches("^.*town.*$")){
					racer.setCity(fieldInfo);
					continue;
				}
				
				// province
				if(fieldName.toLowerCase().trim().equals("prov")) {
					racer.setProv(fieldInfo);
					continue;
				}
				
				// country
				if(fieldName.toLowerCase().trim().equals("co")) {
					racer.setCountry(fieldInfo);
					continue;
				}
				
				// chaleur
				if(fieldName.toLowerCase().trim().equals("chaleur")) {
					continue;
				}
				
				// division
				if(fieldName.toLowerCase().trim().equals("div")) {
					racer.setDiv(fieldInfo);
					continue;
				}
				
				// division place
				if(fieldName.toLowerCase().trim().equals("div total")) {
					racer.setDivPlace(fieldInfo);
					continue;
				}
				
				// gun time
				if(fieldName.toLowerCase().trim().equals("gun time")) {
					racer.setGunTime(fieldInfo);
					continue;
				}
				if(fieldName.toLowerCase().trim().equals("time")) {
					racer.setGunTime(fieldInfo);
					continue;
				}
				
				// pace
				if(fieldName.toLowerCase().trim().matches("pace.*")) {
					racer.setKmpace(fieldInfo);
					continue;
				}
				
				// gender
				if(fieldName.toLowerCase().trim().equals("s")) {
					racer.setGender(fieldInfo);
					continue;
				}
				
				logger.error("Could not racer field: {}", fieldName);
			}
			
			racers.add(racer);
			
			
		}
		
		race.setRacers(racers);
		
		return race;
	}
	
}
