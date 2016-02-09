package ca.albertlockett.atlanticchip.scraper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HomePageScraper {

	public static final String URL_PREFIX = 
			"http://www.atlanticchip.ca/events/results-show.php?result=";
	
	public static void main(String[] args) throws Exception {
	
		for(int eventId = 0; eventId < 2622; eventId++) {
			
			System.out.println("getting results for event ID: " + eventId);
			
			URL resultsPage = new URL(URL_PREFIX + eventId);
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(resultsPage.openStream(), 
							StandardCharsets.ISO_8859_1));
			
			File file = new File("pages/" + eventId + ".html");
			if(!file.exists()) {
				file.createNewFile();
			}
			
			OutputStream out = new FileOutputStream(file);

			while(in.ready()) {
				out.write(in.read());
			}
			in.close();
			out.close();
		}
		
	}
}
