package ca.albertlockett.atlanticchip.spark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.spark.api.java.function.Function;

import ca.albertlockett.atlanticchip.util.MultiLineStringBuilder;

public class LoadRaceInfoHtml implements Function<Integer, String>{

	private static final long serialVersionUID = -4791311178919932691L;

	private String baseUrl;
	
	/**
	 * Do not use this contstructor - it will throw Illegal Arguement Exception
	 * Use the one that passes all the required params into it
	 */
	@Deprecated
	public LoadRaceInfoHtml() throws IllegalArgumentException {
		throw new IllegalArgumentException("Expe");
	}
	
	public LoadRaceInfoHtml(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	/**
	 * Read raw html of events located at baseUrl + eventId + .html
	 * and return as string
	 */
	public String call(Integer eventId) throws Exception {
		URL resultsPage = new URL(this.baseUrl + eventId + ".html");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				resultsPage.openStream(), StandardCharsets.ISO_8859_1));
		MultiLineStringBuilder pageContent = new MultiLineStringBuilder();
		while(in.ready()) pageContent.addLine(in.readLine());
		return pageContent.toString();
	}
	
}
