package ca.albertlockett.atlanticchip.spark;

import org.apache.spark.api.java.function.Function;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ParsePreRaceInfoFromHtmlDoc implements Function<String, String>{

	private static final long serialVersionUID = -4427121986155676792L;

	public String call(String pageContent) throws Exception {
		Document doc = Jsoup.parse(pageContent);
		Element pre = doc.select("pre").first();
		try {
			return pre.text();
		} catch(Exception e) {
			return "";
		}
	}

}
